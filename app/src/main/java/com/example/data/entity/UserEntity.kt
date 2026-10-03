package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val username: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val nidNumber: String = "",
    val gender: String = "Male", // "Male", "Female"
    val password: String,
    val pin: String = "1234",
    val sponsorUsername: String = "",
    val placementUsername: String = "",
    val placementLeg: String = "A", // "A", "B", "C"
    val role: String = "USER", // "USER", "ADMIN"
    val isActive: Boolean = true,
    val joinTimestamp: Long = System.currentTimeMillis(),
    val packageId: Int = 1, // 1..5
    val currentRank: String = "Member",
    val walletBalance: Double = 0.0, // Available PVC
    val totalIncome: Double = 0.0,
    val totalJoiningIncome: Double = 0.0,
    val totalWithdraw: Double = 0.0,
    val referralIncome: Double = 0.0,
    val generationIncome: Double = 0.0,
    val dailyWorkIncome: Double = 0.0,
    val salaryIncome: Double = 0.0,
    val incentiveIncome: Double = 0.0,
    // 3-Leg team counts
    val teamACount: Int = 0,
    val teamBCount: Int = 0,
    val teamCCount: Int = 0,
    // Rank prerequisites
    val teamAGMsA: Int = 0,
    val teamAGMsB: Int = 0,
    val teamAGMsC: Int = 0,
    val teamGMsA: Int = 0,
    val teamGMsB: Int = 0,
    val teamGMsC: Int = 0,
    // Daily task limits
    val adsWatchedToday: Int = 0,
    val lastAdWatchDate: String = "",
    // Payment configurations
    val bKashNumber: String = "",
    val nagadNumber: String = "",
    val rocketNumber: String = "",
    val upayNumber: String = "",
    val bankName: String = "",
    val bankAccountNo: String = "",
    val bankBranch: String = ""
)
