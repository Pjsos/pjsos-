package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreSavedVideo(
    @DocumentId val id: String = "",
    val userId: String = "",
    val videoId: String = "",
    val title: String = "",
    val author: String = "",
    val thumbnailUrl: String = "",
    val summaryPersian: String = "",
    val subtitlesJson: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreSavedWord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val word: String = "",
    val phonetic: String = "",
    val persianMeaning: String = "",
    val partOfSpeech: String = "n.",
    val englishSentence: String = "",
    val sourceVideoTitle: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
