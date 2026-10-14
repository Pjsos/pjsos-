package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translated_videos")
data class TranslatedVideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    val summaryPersian: String,
    val subtitlesJson: String,
    val savedTimestamp: Long = System.currentTimeMillis()
)
