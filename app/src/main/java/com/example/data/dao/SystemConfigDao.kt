package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.SystemConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SystemConfigDao {
    @Query("SELECT * FROM system_config WHERE id = 1 LIMIT 1")
    fun getConfigFlow(): Flow<SystemConfigEntity?>

    @Query("SELECT * FROM system_config WHERE id = 1 LIMIT 1")
    suspend fun getConfig(): SystemConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: SystemConfigEntity)

    @Update
    suspend fun updateConfig(config: SystemConfigEntity)
}
