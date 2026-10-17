package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class FirestoreRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun saveVideo_authenticatedOwner_createsDocument() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repo = FirestoreRepository(firestore)

    val randomVidId = "vid_${UUID.randomUUID().toString().take(8)}"
    val result = withTimeout(DEFAULT_TIMEOUT_MS) {
      repo.saveVideo(
        videoId = randomVidId,
        title = "Learn Kotlin",
        author = "Android Dev",
        thumbnailUrl = "https://img.youtube.com/vi/test/hqdefault.jpg",
        summaryPersian = "خلاصه آموزش",
        subtitlesJson = "[]"
      )
    }
    assertTrue(result.isSuccess)

    val videos = withTimeout(DEFAULT_TIMEOUT_MS) {
      repo.observeSavedVideos(uid).first()
    }
    assertTrue(videos.any { it.videoId == randomVidId })
  }

  @Test
  fun observeSavedVideos_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
    auth.signOut()
    val repo = FirestoreRepository(firestore)

    try {
      withTimeout(FLOW_TIMEOUT_MS) {
        repo.observeSavedVideos("unauthenticated_uid").first()
      }
      fail("Expected exception but succeeded")
    } catch (e: Exception) {
      val hasPermissionDenied = e.toString().contains("PERMISSION_DENIED") ||
              (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) ||
              (e.cause is FirebaseFirestoreException && (e.cause as FirebaseFirestoreException).code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
      assertTrue("Exception should indicate PERMISSION_DENIED: ${e.message}", hasPermissionDenied)
    }
  }

  @Test
  fun saveWord_authenticatedOwner_andDelete() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repo = FirestoreRepository(firestore)

    val testWord = "vocabulary_${UUID.randomUUID().toString().take(6)}"
    val saveResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repo.saveWord(
        word = testWord,
        phonetic = "/vəˈkæbjələri/",
        persianMeaning = "واژگان",
        partOfSpeech = "n.",
        englishSentence = "Expand your vocabulary daily.",
        sourceVideoTitle = "Learning English"
      )
    }
    assertTrue(saveResult.isSuccess)
    val docId = saveResult.getOrThrow()

    val words = withTimeout(DEFAULT_TIMEOUT_MS) {
      repo.observeSavedWords(uid).first()
    }
    assertTrue(words.any { it.word == testWord })

    val deleteResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repo.deleteSavedWord(docId)
    }
    assertTrue(deleteResult.isSuccess)
  }

  private companion object {
    const val ALICE_EMAIL = "alice@example.com"
    const val DEFAULT_TIMEOUT_MS = 6000L
    const val FLOW_TIMEOUT_MS = 3000L
  }
}
