package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.WithdrawalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WithdrawalDao {
    @Query("SELECT * FROM withdrawals ORDER BY requestTimestamp DESC")
    fun getAllWithdrawals(): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE userId = :userId OR username = :username ORDER BY requestTimestamp DESC")
    fun getWithdrawalsForUser(userId: Long, username: String): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE status = 'PENDING' ORDER BY requestTimestamp DESC")
    fun getPendingWithdrawals(): Flow<List<WithdrawalEntity>>

    @Query("SELECT * FROM withdrawals WHERE id = :id LIMIT 1")
    suspend fun getWithdrawalById(id: Long): WithdrawalEntity?

    @Query("SELECT SUM(grossAmount) FROM withdrawals WHERE status = 'PENDING'")
    fun getTotalPendingWithdrawalAmount(): Flow<Double?>

    @Query("SELECT SUM(netAmount) FROM withdrawals WHERE status = 'APPROVED'")
    fun getTotalApprovedWithdrawalAmount(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: WithdrawalEntity): Long

    @Update
    suspend fun updateWithdrawal(withdrawal: WithdrawalEntity)
}
