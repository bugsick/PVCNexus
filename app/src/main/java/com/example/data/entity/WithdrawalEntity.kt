package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "withdrawals")
data class WithdrawalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Long,
    val username: String,
    val gateway: String, // "bKash", "Nagad", "Rocket", "Upay", "Bank"
    val accountNumber: String,
    val accountType: String = "Personal", // "Personal", "Agent", "Savings"
    val bankName: String = "",
    val bankBranch: String = "",
    val grossAmount: Double,
    val vatAmount: Double, // 10%
    val netAmount: Double, // 90%
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val adminRemark: String = "",
    val requestTimestamp: Long = System.currentTimeMillis(),
    val processedTimestamp: Long = 0L
)
