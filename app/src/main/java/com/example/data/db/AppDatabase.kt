package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AdCampaignDao
import com.example.data.dao.BroadcastDao
import com.example.data.dao.ComplaintDao
import com.example.data.dao.SystemConfigDao
import com.example.data.dao.TransactionDao
import com.example.data.dao.UserDao
import com.example.data.dao.WithdrawalDao
import com.example.data.entity.AdCampaignEntity
import com.example.data.entity.BroadcastEntity
import com.example.data.entity.ComplaintEntity
import com.example.data.entity.SystemConfigEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.UserEntity
import com.example.data.entity.WithdrawalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        TransactionEntity::class,
        WithdrawalEntity::class,
        AdCampaignEntity::class,
        ComplaintEntity::class,
        BroadcastEntity::class,
        SystemConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun withdrawalDao(): WithdrawalDao
    abstract fun adCampaignDao(): AdCampaignDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun broadcastDao(): BroadcastDao
    abstract fun systemConfigDao(): SystemConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pvc_nexus_database.db"
                ).fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate with essential seed data
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            seedInitialData(database)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(db: AppDatabase) {
            // Seed system config
            db.systemConfigDao().insertConfig(SystemConfigEntity())

            // Seed Master Admin
            val admin = UserEntity(
                username = "admin",
                fullName = "Master Administrator",
                email = "admin@pvcnexus.com",
                phone = "01700000000",
                password = "admin123",
                pin = "9999",
                role = "ADMIN",
                walletBalance = 500000.0,
                totalIncome = 1250000.0,
                totalJoiningIncome = 850000.0,
                totalWithdraw = 150000.0,
                currentRank = "CEO",
                teamACount = 1950,
                teamBCount = 1870,
                teamCCount = 1820,
                teamAGMsA = 12,
                teamAGMsB = 10,
                teamAGMsC = 11,
                teamGMsA = 4,
                teamGMsB = 3,
                teamGMsC = 3
            )

            // Seed Top Leader (demonstrating Sales Manager / multi-leg tree)
            val leader = UserEntity(
                username = "leader_sam",
                fullName = "Imtiaz Sami Bhuiyan",
                email = "mdimtiazsamibhuiyan@gmail.com",
                phone = "01811223344",
                password = "pass123",
                pin = "1234",
                sponsorUsername = "admin",
                placementUsername = "admin",
                placementLeg = "A",
                role = "USER",
                packageId = 5,
                walletBalance = 38500.0,
                totalIncome = 94500.0,
                totalJoiningIncome = 45000.0,
                totalWithdraw = 20000.0,
                referralIncome = 18000.0,
                generationIncome = 24500.0,
                dailyWorkIncome = 7000.0,
                salaryIncome = 35000.0,
                incentiveIncome = 10000.0,
                currentRank = "Sales Manager",
                teamACount = 640,
                teamBCount = 615,
                teamCCount = 605,
                bKashNumber = "01811223344",
                nagadNumber = "01811223344",
                rocketNumber = "018112233448"
            )

            // Seed Active Regular Member
            val regularUser = UserEntity(
                username = "nexus_user",
                fullName = "Tanvir Ahmed",
                email = "tanvir@nexus.com",
                phone = "01999887766",
                password = "pass123",
                pin = "1234",
                sponsorUsername = "leader_sam",
                placementUsername = "leader_sam",
                placementLeg = "A",
                role = "USER",
                packageId = 3,
                walletBalance = 4550.0,
                totalIncome = 12850.0,
                totalJoiningIncome = 6000.0,
                totalWithdraw = 3000.0,
                referralIncome = 3600.0,
                generationIncome = 4250.0,
                dailyWorkIncome = 2000.0,
                salaryIncome = 0.0,
                incentiveIncome = 3000.0,
                currentRank = "Tour Winner",
                teamACount = 112,
                teamBCount = 105,
                teamCCount = 102,
                bKashNumber = "01999887766"
            )

            // Seed Team downlines for leader_sam
            val memberB = UserEntity(
                username = "sabbir_b",
                fullName = "Sabbir Hossain",
                email = "sabbir@nexus.com",
                phone = "01711224455",
                password = "pass123",
                pin = "1234",
                sponsorUsername = "leader_sam",
                placementUsername = "leader_sam",
                placementLeg = "B",
                role = "USER",
                packageId = 4,
                walletBalance = 6200.0,
                totalIncome = 15400.0,
                currentRank = "Tour Winner",
                teamACount = 104,
                teamBCount = 101,
                teamCCount = 100
            )

            val memberC = UserEntity(
                username = "rashid_c",
                fullName = "Rashid Khan",
                email = "rashid@nexus.com",
                phone = "01622334455",
                password = "pass123",
                pin = "1234",
                sponsorUsername = "leader_sam",
                placementUsername = "leader_sam",
                placementLeg = "C",
                role = "USER",
                packageId = 2,
                walletBalance = 2900.0,
                totalIncome = 8700.0,
                currentRank = "Member",
                teamACount = 45,
                teamBCount = 38,
                teamCCount = 42
            )

            // Downline under nexus_user (Gen 2 for leader_sam)
            val subUserA1 = UserEntity(
                username = "karim_pro",
                fullName = "Karim Uddin",
                email = "karim@nexus.com",
                phone = "01555443322",
                password = "pass123",
                pin = "1234",
                sponsorUsername = "nexus_user",
                placementUsername = "nexus_user",
                placementLeg = "A",
                role = "USER",
                packageId = 5,
                walletBalance = 1500.0,
                totalIncome = 3200.0,
                currentRank = "Member",
                teamACount = 15,
                teamBCount = 12,
                teamCCount = 10
            )

            val subUserA2 = UserEntity(
                username = "fatima_growth",
                fullName = "Fatima Zohra",
                email = "fatima@nexus.com",
                phone = "01788990011",
                password = "pass123",
                pin = "1234",
                sponsorUsername = "nexus_user",
                placementUsername = "nexus_user",
                placementLeg = "B",
                role = "USER",
                packageId = 3,
                walletBalance = 2100.0,
                totalIncome = 4500.0,
                currentRank = "Member",
                teamACount = 22,
                teamBCount = 20,
                teamCCount = 18
            )

            db.userDao().insertUsers(listOf(admin, leader, regularUser, memberB, memberC, subUserA1, subUserA2))

            // Seed Ad Campaigns
            val ad1 = AdCampaignEntity(
                title = "Fintech Card Launch - Earn 5% Cashback",
                sponsorName = "NexusPay Global",
                category = "Fintech",
                description = "Discover the ultimate smart virtual card for cross-border digital payments and instant transfers.",
                rewardAmount = 5.0,
                durationSeconds = 10,
                accentColor = 0xFF06B6D4
            )
            val ad2 = AdCampaignEntity(
                title = "Smart Crypto & Forex Staking Protocol",
                sponsorName = "CryptoNexus Labs",
                category = "Crypto & Trading",
                description = "Stake, grow, and secure daily yields with 100% decentralized audited smart contracts.",
                rewardAmount = 5.0,
                durationSeconds = 10,
                accentColor = 0xFF8B5CF6
            )
            val ad3 = AdCampaignEntity(
                title = "High-Growth E-Commerce Affiliate Network",
                sponsorName = "Bazaar Direct",
                category = "E-Commerce",
                description = "Partner with over 500 premium brands and earn automated recurring commissions daily.",
                rewardAmount = 5.0,
                durationSeconds = 10,
                accentColor = 0xFF10B981
            )
            val ad4 = AdCampaignEntity(
                title = "Cloud Mining & AI Computing Grid",
                sponsorName = "QuantumNode Asia",
                category = "Technology",
                description = "Rent decentralized GPU and AI compute power with guaranteed 24/7 uptime and instant payout.",
                rewardAmount = 5.0,
                durationSeconds = 10,
                accentColor = 0xFFF59E0B
            )
            val ad5 = AdCampaignEntity(
                title = "Premier Real Estate Crowdfunding Club",
                sponsorName = "Empire Capital",
                category = "Real Estate",
                description = "Fractional prime commercial property ownership starting from only ৳ 5,000.",
                rewardAmount = 5.0,
                durationSeconds = 10,
                accentColor = 0xFFF43F5E
            )
            db.adCampaignDao().insertCampaigns(listOf(ad1, ad2, ad3, ad4, ad5))

            // Seed Broadcasts
            val b1 = BroadcastEntity(
                title = "Welcome to PVC NEXUS Ecosystem!",
                message = "Connect • Grow • Succeed! Complete daily tasks, build 3-leg sales teams, and unlock unlimited career ranks.",
                isUrgent = false,
                author = "Executive Management"
            )
            val b2 = BroadcastEntity(
                title = "Mega Cox's Bazar Tour & Bonus Sprint",
                message = "Achieve 100 - 100 - 100 balanced members in Team A, B, and C this quarter to claim the 5-Star Cox's Bazar Tour Package or ৳ 5,000 instant cash!",
                isUrgent = true,
                author = "Promotions Dept"
            )
            db.broadcastDao().insertBroadcast(b1)
            db.broadcastDao().insertBroadcast(b2)

            // Seed Transactions
            db.transactionDao().insertTransaction(
                TransactionEntity(
                    userId = 2L,
                    username = "leader_sam",
                    type = "REFER_BONUS",
                    amount = 300.0,
                    netAmount = 300.0,
                    description = "Direct Referral Bonus for registering @nexus_user",
                    counterpartUser = "nexus_user"
                )
            )
            db.transactionDao().insertTransaction(
                TransactionEntity(
                    userId = 2L,
                    username = "leader_sam",
                    type = "SALARY",
                    amount = 5000.0,
                    netAmount = 5000.0,
                    description = "Monthly Sales Manager Rank Salary Disbursal",
                    counterpartUser = "SYSTEM_SALARY"
                )
            )
            db.transactionDao().insertTransaction(
                TransactionEntity(
                    userId = 3L,
                    username = "nexus_user",
                    type = "DAILY_WORK",
                    amount = 30.0,
                    netAmount = 30.0,
                    description = "Daily Ad Task completion reward (Package 3 - 6 ads)",
                    counterpartUser = "AD_ENGINE"
                )
            )

            // Seed Withdrawal sample
            db.withdrawalDao().insertWithdrawal(
                WithdrawalEntity(
                    userId = 2L,
                    username = "leader_sam",
                    gateway = "bKash",
                    accountNumber = "01811223344",
                    accountType = "Personal",
                    grossAmount = 5000.0,
                    vatAmount = 500.0,
                    netAmount = 4500.0,
                    status = "APPROVED",
                    adminRemark = "Processed via bKash TrxID: 9X87KL21"
                )
            )
            db.withdrawalDao().insertWithdrawal(
                WithdrawalEntity(
                    userId = 3L,
                    username = "nexus_user",
                    gateway = "Nagad",
                    accountNumber = "01999887766",
                    accountType = "Personal",
                    grossAmount = 2000.0,
                    vatAmount = 200.0,
                    netAmount = 1800.0,
                    status = "PENDING",
                    adminRemark = "Under verification by accounting team"
                )
            )

            // Seed Complaint sample
            db.complaintDao().insertComplaint(
                ComplaintEntity(
                    userId = 3L,
                    username = "nexus_user",
                    category = "Transfer",
                    subject = "Inquiry regarding 5% platform fee",
                    description = "Hello Admin, is the 5% platform fee waived during promotional events?",
                    priority = "Normal",
                    status = "RESOLVED",
                    adminReply = "Hello Tanvir, during special sprint weekends we occasionally reduce transfer fees. Stay tuned to Broadcasts!"
                )
            )
        }
    }
}
