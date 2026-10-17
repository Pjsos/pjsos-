package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.FirestoreSavedVideo
import com.example.data.model.FirestoreSavedWord
import com.example.data.remote.OperationType
import com.example.data.remote.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    /**
     * Real-time stream of saved videos for current authenticated user
     */
    fun observeSavedVideos(userId: String = requireUserId()): Flow<List<FirestoreSavedVideo>> =
        db.collection("users").document(userId).collection("saved_videos")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(FirestoreSavedVideo::class.java) }
            .catch { error ->
                if (error is Exception) {
                    handleFirestoreError(error, OperationType.LIST, "users/$userId/saved_videos")
                }
                throw error
            }

    /**
     * Real-time stream of saved words for current authenticated user
     */
    fun observeSavedWords(userId: String = requireUserId()): Flow<List<FirestoreSavedWord>> =
        db.collection("users").document(userId).collection("saved_words")
            .snapshots()
            .map { snapshot -> snapshot.toObjects(FirestoreSavedWord::class.java) }
            .catch { error ->
                if (error is Exception) {
                    handleFirestoreError(error, OperationType.LIST, "users/$userId/saved_words")
                }
                throw error
            }

    /**
     * Save a translated video to Firestore
     */
    suspend fun saveVideo(
        videoId: String,
        title: String,
        author: String,
        thumbnailUrl: String,
        summaryPersian: String,
        subtitlesJson: String
    ): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid).collection("saved_videos").document(videoId)
            val payload = mapOf(
                "userId" to uid,
                "videoId" to videoId,
                "title" to title,
                "author" to author,
                "thumbnailUrl" to thumbnailUrl,
                "summaryPersian" to summaryPersian,
                "subtitlesJson" to subtitlesJson,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, "users/${auth.currentUser?.uid}/saved_videos/$videoId")
            Result.failure(e)
        }
    }

    /**
     * Delete a saved video
     */
    suspend fun deleteSavedVideo(videoId: String): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid).collection("saved_videos").document(videoId)
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, "users/${auth.currentUser?.uid}/saved_videos/$videoId")
            Result.failure(e)
        }
    }

    /**
     * Save a vocabulary word to Firestore
     */
    suspend fun saveWord(
        word: String,
        phonetic: String,
        persianMeaning: String,
        partOfSpeech: String,
        englishSentence: String,
        sourceVideoTitle: String
    ): Result<String> {
        return try {
            val uid = requireUserId()
            val collectionRef = db.collection("users").document(uid).collection("saved_words")
            val cleanDocId = word.lowercase().trim().replace("[^a-z0-9_\\-]".toRegex(), "_").take(64).ifEmpty { "word_${System.currentTimeMillis()}" }
            val docRef = collectionRef.document(cleanDocId)

            val payload = mapOf(
                "userId" to uid,
                "word" to word,
                "phonetic" to phonetic,
                "persianMeaning" to persianMeaning,
                "partOfSpeech" to partOfSpeech,
                "englishSentence" to englishSentence,
                "sourceVideoTitle" to sourceVideoTitle,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(cleanDocId)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, "users/${auth.currentUser?.uid}/saved_words")
            Result.failure(e)
        }
    }

    /**
     * Delete a saved vocabulary word
     */
    suspend fun deleteSavedWord(wordDocId: String): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid).collection("saved_words").document(wordDocId)
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, "users/${auth.currentUser?.uid}/saved_words/$wordDocId")
            Result.failure(e)
        }
    }
}
