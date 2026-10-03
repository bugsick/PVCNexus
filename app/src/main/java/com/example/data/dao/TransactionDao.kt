package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE userId = :userId OR username = :username ORDER BY timestamp DESC")
    fun getTransactionsForUser(userId: Long, username: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE (userId = :userId OR username = :username) AND type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(userId: Long, username: String, type: String): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'JOINING_FEE'")
    fun getTotalJoiningTurnover(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long
}
