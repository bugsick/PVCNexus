package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdFlow(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun getUserByUsernameFlow(username: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE sponsorUsername = :sponsorUsername ORDER BY id ASC")
    fun getDirectReferrals(sponsorUsername: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE sponsorUsername = :sponsorUsername")
    suspend fun getDirectReferralsList(sponsorUsername: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE placementUsername = :placementUsername AND placementLeg = :leg LIMIT 1")
    suspend fun getDirectLegChild(placementUsername: String, leg: String): UserEntity?

    @Query("SELECT * FROM users WHERE placementUsername = :placementUsername")
    suspend fun getDirectPlacementChildren(placementUsername: String): List<UserEntity>

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>

    @Query("SELECT SUM(walletBalance) FROM users")
    fun getTotalEcosystemBalance(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Long)
}
