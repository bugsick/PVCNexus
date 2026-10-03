package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.ComplaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE userId = :userId OR username = :username ORDER BY timestamp DESC")
    fun getComplaintsForUser(userId: Long, username: String): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE status = 'OPEN' ORDER BY timestamp DESC")
    fun getOpenComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE id = :id LIMIT 1")
    suspend fun getComplaintById(id: Long): ComplaintEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity): Long

    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)

    @Delete
    suspend fun deleteComplaint(complaint: ComplaintEntity)
}
