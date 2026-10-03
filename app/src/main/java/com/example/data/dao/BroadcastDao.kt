package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.BroadcastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BroadcastDao {
    @Query("SELECT * FROM broadcasts ORDER BY timestamp DESC")
    fun getAllBroadcasts(): Flow<List<BroadcastEntity>>

    @Query("SELECT * FROM broadcasts ORDER BY timestamp DESC LIMIT 5")
    fun getRecentBroadcasts(): Flow<List<BroadcastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBroadcast(broadcast: BroadcastEntity): Long

    @Delete
    suspend fun deleteBroadcast(broadcast: BroadcastEntity)

    @Query("DELETE FROM broadcasts WHERE id = :id")
    suspend fun deleteBroadcastById(id: Long)
}
