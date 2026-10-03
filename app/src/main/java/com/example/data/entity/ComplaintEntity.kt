package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Long,
    val username: String,
    val category: String, // "Wallet", "Withdrawal", "Transfer", "Ad Task", "Account", "Other"
    val subject: String,
    val description: String,
    val priority: String = "Normal", // "Normal", "High", "Urgent"
    val status: String = "OPEN", // "OPEN", "IN_PROGRESS", "RESOLVED"
    val adminReply: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val replyTimestamp: Long = 0L
)
