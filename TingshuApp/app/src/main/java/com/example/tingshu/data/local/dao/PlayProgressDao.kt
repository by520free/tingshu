package com.example.tingshu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tingshu.data.local.entity.PlayProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayProgressDao {

    @Query("SELECT * FROM play_progress WHERE bookId = :bookId")
    fun getPlayProgress(bookId: String): Flow<PlayProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayProgress(progress: PlayProgressEntity)

    @Query("DELETE FROM play_progress WHERE bookId = :bookId")
    suspend fun deletePlayProgress(bookId: String)
}
