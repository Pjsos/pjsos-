package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_words")
data class SavedWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val phonetic: String = "",
    val persianMeaning: String,
    val englishSentence: String = "",
    val sourceVideoTitle: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
