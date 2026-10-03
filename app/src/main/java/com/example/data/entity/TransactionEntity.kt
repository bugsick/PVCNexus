package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Long,
    val username: String,
    val type: String, // "REFER_BONUS", "GEN_BONUS", "DAILY_WORK", "JOINING_FEE", "TRANSFER_OUT", "TRANSFER_IN", "WITHDRAWAL", "WITHDRAWAL_REFUND", "ADMIN_CREDIT", "ADMIN_DEBIT", "SALARY", "INCENTIVE"
    val amount: Double,
    val fee: Double = 0.0,
    val netAmount: Double,
    val description: String,
    val reference: String = "",
    val counterpartUser: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "COMPLETED" // "COMPLETED", "PENDING", "REJECTED"
)
