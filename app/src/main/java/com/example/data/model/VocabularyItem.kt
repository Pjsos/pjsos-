package com.example.data.model

data class VocabularyItem(
    val word: String,
    val phonetic: String = "",
    val persianMeaning: String,
    val partOfSpeech: String = "n.",
    val englishContext: String = "",
    val persianExample: String = ""
)
