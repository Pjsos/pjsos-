package com.example.data.model

data class SubtitleLine(
    val id: Int,
    val startSeconds: Float,
    val endSeconds: Float,
    val englishText: String,
    val persianText: String
) {
    val formattedTimestamp: String
        get() {
            val totalSec = startSeconds.toInt()
            val min = totalSec / 60
            val sec = totalSec % 60
            return String.format("%02d:%02d", min, sec)
        }
}
