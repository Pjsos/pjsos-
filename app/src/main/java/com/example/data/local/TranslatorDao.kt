package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatorDao {

    @Query("SELECT * FROM translated_videos ORDER BY savedTimestamp DESC")
    fun getAllSavedVideos(): Flow<List<TranslatedVideoEntity>>

    @Query("SELECT * FROM translated_videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: String): TranslatedVideoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: TranslatedVideoEntity)

    @Query("DELETE FROM translated_videos WHERE id = :id")
    suspend fun deleteVideoById(id: String)

    @Query("SELECT * FROM saved_words ORDER BY timestamp DESC")
    fun getAllSavedWords(): Flow<List<SavedWordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: SavedWordEntity)

    @Query("DELETE FROM saved_words WHERE id = :id")
    suspend fun deleteWordById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_words WHERE LOWER(word) = LOWER(:word))")
    fun isWordSaved(word: String): Flow<Boolean>
}
