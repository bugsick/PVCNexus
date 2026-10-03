package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.AdCampaignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdCampaignDao {
    @Query("SELECT * FROM ad_campaigns ORDER BY id ASC")
    fun getAllCampaigns(): Flow<List<AdCampaignEntity>>

    @Query("SELECT * FROM ad_campaigns ORDER BY id ASC")
    suspend fun getAllCampaignsList(): List<AdCampaignEntity>

    @Query("SELECT * FROM ad_campaigns WHERE isActive = 1 ORDER BY id ASC")
    fun getActiveCampaigns(): Flow<List<AdCampaignEntity>>

    @Query("SELECT * FROM ad_campaigns WHERE id = :id LIMIT 1")
    suspend fun getCampaignById(id: Long): AdCampaignEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: AdCampaignEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaigns(campaigns: List<AdCampaignEntity>)

    @Update
    suspend fun updateCampaign(campaign: AdCampaignEntity)

    @Delete
    suspend fun deleteCampaign(campaign: AdCampaignEntity)

    @Query("DELETE FROM ad_campaigns WHERE id = :id")
    suspend fun deleteCampaignById(id: Long)
}
