package com.example.data.model

data class VideoData(
    val id: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    val durationSeconds: Int = 180,
    val categoryPersian: String = "آموزشی",
    val summaryPersian: String,
    val keyPointsPersian: List<String> = emptyList(),
    val subtitles: List<SubtitleLine> = emptyList(),
    val vocabulary: List<VocabularyItem> = emptyList(),
    val isCustom: Boolean = false
) {
    val fullYoutubeUrl: String
        get() = "https://www.youtube.com/watch?v=$id"
}
