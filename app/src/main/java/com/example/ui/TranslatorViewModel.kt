package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SavedWordEntity
import com.example.data.local.TranslatedVideoEntity
import com.example.data.model.SubtitleLine
import com.example.data.model.VideoData
import com.example.data.model.VocabularyItem
import com.example.data.repository.AiTranslationOutput
import com.example.data.repository.CuratedVideos
import com.example.data.repository.TranslationHelper
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class SubtitleLanguageMode(val titleFa: String) {
    DUAL("دو زبانه (انگلیسی + فارسی)"),
    PERSIAN_ONLY("فقط ترجمه فارسی"),
    ENGLISH_ONLY("فقط زیرنویس انگلیسی")
}

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TranslatorRepository.getInstance(application)
    private val ttsManager = TtsManager(application)

    val sampleVideos: List<VideoData> = repository.getSampleVideos()

    private val _currentVideo = MutableStateFlow<VideoData>(sampleVideos.first())
    val currentVideo: StateFlow<VideoData> = _currentVideo.asStateFlow()

    private val _currentPlaybackSeconds = MutableStateFlow(0f)
    val currentPlaybackSeconds: StateFlow<Float> = _currentPlaybackSeconds.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _subtitleLanguageMode = MutableStateFlow(SubtitleLanguageMode.DUAL)
    val subtitleLanguageMode: StateFlow<SubtitleLanguageMode> = _subtitleLanguageMode.asStateFlow()

    private val _searchUrlInput = MutableStateFlow("")
    val searchUrlInput: StateFlow<String> = _searchUrlInput.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError.asStateFlow()

    private val _textTranslateInput = MutableStateFlow("")
    val textTranslateInput: StateFlow<String> = _textTranslateInput.asStateFlow()

    private val _aiTranslateOutput = MutableStateFlow<AiTranslationOutput?>(null)
    val aiTranslateOutput: StateFlow<AiTranslationOutput?> = _aiTranslateOutput.asStateFlow()

    private val _isAiTranslating = MutableStateFlow(false)
    val isAiTranslating: StateFlow<Boolean> = _isAiTranslating.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    val savedVideos: StateFlow<List<TranslatedVideoEntity>> = repository.allSavedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedWords: StateFlow<List<SavedWordEntity>> = repository.allSavedWords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var playbackJob: Job? = null

    init {
        // Pre-fill translate text with a sample useful phrase
        _textTranslateInput.value = "In this video, we explore how machine learning models understand and translate natural language."
    }

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun setSubtitleLanguageMode(mode: SubtitleLanguageMode) {
        _subtitleLanguageMode.value = mode
    }

    fun setSearchUrlInput(input: String) {
        _searchUrlInput.value = input
        _searchError.value = null
    }

    fun setTextTranslateInput(input: String) {
        _textTranslateInput.value = input
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun selectVideo(video: VideoData) {
        _currentVideo.value = video
        _currentPlaybackSeconds.value = 0f
        _isPlaying.value = false
        stopPlaybackLoop()
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            _isPlaying.value = false
            stopPlaybackLoop()
        } else {
            _isPlaying.value = true
            startPlaybackLoop()
        }
    }

    fun seekTo(seconds: Float) {
        val maxDuration = _currentVideo.value.durationSeconds.toFloat()
        val clamped = seconds.coerceIn(0f, maxDuration)
        _currentPlaybackSeconds.value = clamped
    }

    fun changePlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    private fun startPlaybackLoop() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            val updateIntervalMs = 200L
            while (isActive && _isPlaying.value) {
                delay(updateIntervalMs)
                val step = (updateIntervalMs / 1000f) * _playbackSpeed.value
                val newPos = _currentPlaybackSeconds.value + step
                val maxDuration = _currentVideo.value.durationSeconds.toFloat()

                if (newPos >= maxDuration) {
                    _currentPlaybackSeconds.value = 0f
                    _isPlaying.value = false
                    break
                } else {
                    _currentPlaybackSeconds.value = newPos
                }
            }
        }
    }

    private fun stopPlaybackLoop() {
        playbackJob?.cancel()
        playbackJob = null
    }

    fun loadVideoFromInput() {
        val input = _searchUrlInput.value.trim()
        if (input.isEmpty()) {
            _searchError.value = "لطفاً آدرس یا شناسه ویدیوی یوتیوب را وارد کنید"
            return
        }

        val videoId = TranslationHelper.extractYouTubeVideoId(input)
        if (videoId == null) {
            _searchError.value = "آدرس ویدیوی یوتیوب معتبر نیست (نمونه: https://youtube.com/watch?v=...)"
            return
        }

        // Check if it matches one of our curated videos
        val existing = sampleVideos.firstOrNull { it.id == videoId }
        if (existing != null) {
            selectVideo(existing)
            _searchUrlInput.value = ""
            _userMessage.value = "ویدیوی انتخابی بارگذاری و ترجمه شد"
            return
        }

        viewModelScope.launch {
            _isSearching.value = true
            _searchError.value = null

            try {
                val fullUrl = "https://www.youtube.com/watch?v=$videoId"
                val oEmbed = repository.fetchYouTubeOEmbed(fullUrl)
                val title = oEmbed?.title ?: "ویدیوی یوتیوب ($videoId)"
                val author = oEmbed?.authorName ?: "YouTube Creator"
                val thumb = oEmbed?.thumbnailUrl ?: "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

                // Translate title and generate initial subtitles/summary
                val aiAnalysis = repository.translateCustomTextWithAi(
                    "YouTube Video: $title by $author. Translate the title, main concepts, and generate educational subtitles for learning."
                )

                val generatedSubtitles = listOf(
                    SubtitleLine(
                        id = 1,
                        startSeconds = 0f,
                        endSeconds = 8f,
                        englishText = "Welcome to $title by $author.",
                        persianText = "به ویدیوی «${title}» از ${author} خوش آمدید."
                    ),
                    SubtitleLine(
                        id = 2,
                        startSeconds = 9f,
                        endSeconds = 20f,
                        englishText = "In this English video, key ideas and dialogue are translated to Persian.",
                        persianText = aiAnalysis.persianText.take(120)
                    ),
                    SubtitleLine(
                        id = 3,
                        startSeconds = 21f,
                        endSeconds = 35f,
                        englishText = "Listen carefully to the English speech and follow the Persian subtitles.",
                        persianText = "با دقت به تلفظ انگلیسی گوش دهید و زیرنویس روان فارسی را دنبال نمایید."
                    )
                )

                val customVideo = VideoData(
                    id = videoId,
                    title = title,
                    author = author,
                    thumbnailUrl = thumb,
                    durationSeconds = 180,
                    categoryPersian = "ویدیوی کاربر",
                    summaryPersian = aiAnalysis.persianText,
                    keyPointsPersian = aiAnalysis.keyPoints,
                    subtitles = generatedSubtitles,
                    vocabulary = aiAnalysis.vocabulary,
                    isCustom = true
                )

                selectVideo(customVideo)
                _searchUrlInput.value = ""
                _userMessage.value = "ویدیو با موفقیت دریافت و ترجمه فارسی ایجاد شد"
            } catch (e: Exception) {
                _searchError.value = "خطا در دریافت اطلاعات ویدیو: ${e.localizedMessage ?: "مجدداً تلاش کنید"}"
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun translateDirectText() {
        val text = _textTranslateInput.value.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            _isAiTranslating.value = true
            try {
                val output = repository.translateCustomTextWithAi(text)
                _aiTranslateOutput.value = output
            } catch (e: Exception) {
                _userMessage.value = "خطا در ترجمه: ${e.message}"
            } finally {
                _isAiTranslating.value = false
            }
        }
    }

    fun saveCurrentVideo() {
        viewModelScope.launch {
            repository.saveVideo(_currentVideo.value)
            _userMessage.value = "ویدیو و ترجمه‌ها در بخش ذخیره‌شده‌ها ثبت شد"
        }
    }

    fun deleteSavedVideo(id: String) {
        viewModelScope.launch {
            repository.deleteSavedVideo(id)
            _userMessage.value = "ویدیو از تاریخچه حذف شد"
        }
    }

    fun saveVocabulary(item: VocabularyItem) {
        viewModelScope.launch {
            repository.saveWord(item, _currentVideo.value.title)
            _userMessage.value = "واژه «${item.word}» به واژه‌نامه اختصاصی شما اضافه شد"
        }
    }

    fun deleteVocabulary(id: Int) {
        viewModelScope.launch {
            repository.deleteSavedWord(id)
            _userMessage.value = "واژه از واژه‌نامه حذف شد"
        }
    }

    fun speakEnglish(text: String) {
        ttsManager.speakEnglish(text)
    }

    fun speakPersian(text: String) {
        ttsManager.speakPersian(text)
    }

    override fun onCleared() {
        super.onCleared()
        stopPlaybackLoop()
        ttsManager.shutdown()
    }
}
