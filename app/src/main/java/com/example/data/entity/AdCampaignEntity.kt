package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ad_campaigns")
data class AdCampaignEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val sponsorName: String,
    val category: String = "Fintech",
    val description: String = "",
    val rewardAmount: Double = 5.0,
    val durationSeconds: Int = 10,
    val isActive: Boolean = true,
    val totalImpressions: Int = 0,
    val accentColor: Long = 0xFF06B6D4
)
