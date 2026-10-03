package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "broadcasts")
data class BroadcastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val message: String,
    val isUrgent: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val author: String = "Master Admin"
)
