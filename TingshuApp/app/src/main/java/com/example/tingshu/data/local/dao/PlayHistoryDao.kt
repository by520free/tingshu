package com.example.tingshu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tingshu.data.local.entity.PlayHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayHistoryDao {

    @Query("SELECT * FROM play_history ORDER BY lastPlayTime DESC")
    fun getAllPlayHistory(): Flow<List<PlayHistoryEntity>>

    @Query("SELECT * FROM play_history WHERE bookId = :bookId")
    suspend fun getPlayHistoryByBookId(bookId: String): PlayHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayHistory(history: PlayHistoryEntity)

    @Query("DELETE FROM play_history WHERE bookId = :bookId")
    suspend fun deletePlayHistoryByBookId(bookId: String)

    @Query("DELETE FROM play_history")
    suspend fun clearAllPlayHistory()
}
