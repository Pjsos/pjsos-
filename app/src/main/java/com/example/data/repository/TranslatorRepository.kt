package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.SavedWordEntity
import com.example.data.local.TranslatedVideoEntity
import com.example.data.local.TranslatorDao
import com.example.data.model.SubtitleLine
import com.example.data.model.VideoData
import com.example.data.model.VocabularyItem
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import com.example.data.remote.NetworkClient
import com.example.data.remote.YouTubeOEmbedResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TranslatorRepository(
    private val dao: TranslatorDao,
    private val context: Context
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val subtitleListType = Types.newParameterizedType(List::class.java, SubtitleJson::class.java)
    private val subtitleAdapter = moshi.adapter<List<SubtitleJson>>(subtitleListType)

    val allSavedVideos: Flow<List<TranslatedVideoEntity>> = dao.getAllSavedVideos()
    val allSavedWords: Flow<List<SavedWordEntity>> = dao.getAllSavedWords()

    fun getSampleVideos(): List<VideoData> = CuratedVideos.sampleVideos

    fun isWordSaved(word: String): Flow<Boolean> = dao.isWordSaved(word)

    suspend fun saveVideo(video: VideoData) = withContext(Dispatchers.IO) {
        val jsonList = video.subtitles.map {
            SubtitleJson(it.id, it.startSeconds, it.endSeconds, it.englishText, it.persianText)
        }
        val jsonStr = subtitleAdapter.toJson(jsonList)
        val entity = TranslatedVideoEntity(
            id = video.id,
            title = video.title,
            author = video.author,
            thumbnailUrl = video.thumbnailUrl,
            summaryPersian = video.summaryPersian,
            subtitlesJson = jsonStr
        )
        dao.insertVideo(entity)
    }

    suspend fun deleteSavedVideo(id: String) = withContext(Dispatchers.IO) {
        dao.deleteVideoById(id)
    }

    suspend fun saveWord(word: VocabularyItem, sourceVideoTitle: String) = withContext(Dispatchers.IO) {
        dao.insertWord(
            SavedWordEntity(
                word = word.word,
                phonetic = word.phonetic,
                persianMeaning = word.persianMeaning,
                englishSentence = word.englishContext,
                sourceVideoTitle = sourceVideoTitle
            )
        )
    }

    suspend fun deleteSavedWord(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteWordById(id)
    }

    suspend fun fetchYouTubeOEmbed(url: String): YouTubeOEmbedResponse? = withContext(Dispatchers.IO) {
        try {
            NetworkClient.youTubeService.getVideoDetails(url)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun translateCustomTextWithAi(englishText: String): AiTranslationOutput = withContext(Dispatchers.IO) {
        val apiKey = NetworkClient.getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Use intelligent offline translator
            val farsiTranslation = TranslationHelper.translateEnglishSentenceOffline(englishText)
            val vocab = TranslationHelper.extractVocabularyOffline(englishText)
            val keyPoints = listOf(
                "ترجمه بر اساس متن استخراج شده از یوتیوب آماده شد.",
                "واژگان کلیدی برای یادگیری مشخص گردیدند."
            )
            return@withContext AiTranslationOutput(
                persianText = farsiTranslation,
                keyPoints = keyPoints,
                vocabulary = vocab
            )
        }

        try {
            val prompt = """
                You are an expert English to Persian (Farsi) translator for YouTube videos.
                Translate the following English transcript / dialogue into natural, modern Persian:
                
                English Input:
                "$englishText"
                
                Format your exact response strictly in the following sections without extra chat:
                ---TRANSLATION---
                (The natural Persian translation)
                ---SUMMARY---
                - (Key takeaway 1 in Persian)
                - (Key takeaway 2 in Persian)
                - (Key takeaway 3 in Persian)
                ---VOCABULARY---
                Word | Phonetic | Persian Meaning | Part of Speech
                (list 3 to 5 words/idioms, one per line)
            """.trimIndent()

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                )
            )

            val response = NetworkClient.geminiService.generateContent(apiKey, request)
            val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""

            parseAiOutput(rawText, englishText)
        } catch (e: Exception) {
            // Graceful fallback
            val farsi = TranslationHelper.translateEnglishSentenceOffline(englishText)
            val vocab = TranslationHelper.extractVocabularyOffline(englishText)
            AiTranslationOutput(
                persianText = farsi,
                keyPoints = listOf("ترجمه اولیه انجام شد (حالت آفلاین یا اختلال در شبکه)"),
                vocabulary = vocab
            )
        }
    }

    private fun parseAiOutput(aiResponse: String, originalEnglish: String): AiTranslationOutput {
        if (aiResponse.isBlank()) {
            return AiTranslationOutput(
                persianText = TranslationHelper.translateEnglishSentenceOffline(originalEnglish),
                keyPoints = emptyList(),
                vocabulary = TranslationHelper.extractVocabularyOffline(originalEnglish)
            )
        }

        val translationSec = aiResponse.substringAfter("---TRANSLATION---", "")
            .substringBefore("---SUMMARY---").trim()
        val summarySec = aiResponse.substringAfter("---SUMMARY---", "")
            .substringBefore("---VOCABULARY---").trim()
        val vocabSec = aiResponse.substringAfter("---VOCABULARY---", "").trim()

        val summaryPoints = summarySec.lines()
            .map { it.trim().removePrefix("-").trim() }
            .filter { it.isNotEmpty() }

        val vocabList = mutableListOf<VocabularyItem>()
        for (line in vocabSec.lines()) {
            val parts = line.split("|").map { it.trim() }
            if (parts.size >= 3) {
                vocabList.add(
                    VocabularyItem(
                        word = parts[0],
                        phonetic = if (parts.size > 1) parts[1] else "",
                        persianMeaning = if (parts.size > 2) parts[2] else "",
                        partOfSpeech = if (parts.size > 3) parts[3] else "واژه",
                        englishContext = originalEnglish.take(80) + "...",
                        persianExample = "استفاده شده در این ویدیو"
                    )
                )
            }
        }

        val finalPersian = if (translationSec.isNotBlank()) translationSec
        else TranslationHelper.translateEnglishSentenceOffline(originalEnglish)

        return AiTranslationOutput(
            persianText = finalPersian,
            keyPoints = summaryPoints.ifEmpty { listOf("نکات کلیدی برای این بخش ثبت گردید.") },
            vocabulary = vocabList.ifEmpty { TranslationHelper.extractVocabularyOffline(originalEnglish) }
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: TranslatorRepository? = null

        fun getInstance(context: Context): TranslatorRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val repo = TranslatorRepository(db.translatorDao(), context.applicationContext)
                INSTANCE = repo
                repo
            }
        }
    }
}

data class SubtitleJson(
    val id: Int,
    val startSeconds: Float,
    val endSeconds: Float,
    val englishText: String,
    val persianText: String
)

data class AiTranslationOutput(
    val persianText: String,
    val keyPoints: List<String>,
    val vocabulary: List<VocabularyItem>
)
