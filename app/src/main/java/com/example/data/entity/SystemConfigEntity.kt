package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "system_config")
data class SystemConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val transferFeePercent: Double = 5.0, // 5%
    val withdrawVatPercent: Double = 10.0, // 10%
    val minWithdrawalAmount: Double = 300.0, // ৳ 300 Minimum withdrawal
    val baseJoiningFee: Double = 1500.0, // ৳ 1,500
    val directReferBonus: Double = 300.0, // ৳ 300
    val adRewardPerUnit: Double = 5.0, // ৳ 5 per ad
    val dailyAdLimit: Int = 2, // 2 ads per day (৳ 10 total)
    val gen1Bonus: Double = 100.0, // ৳ 100
    val gen2Bonus: Double = 50.0,  // ৳ 50
    val gen3Bonus: Double = 20.0,  // ৳ 20
    val gen4Bonus: Double = 10.0,  // ৳ 10
    val gen5Bonus: Double = 10.0   // ৳ 10
)
