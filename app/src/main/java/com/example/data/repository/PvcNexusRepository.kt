package com.example.data.repository

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PvcNexusRepository(
    private val userDao: UserDao,
    private val transactionDao: TransactionDao,
    private val withdrawalDao: WithdrawalDao,
    private val adCampaignDao: AdCampaignDao,
    private val complaintDao: ComplaintDao,
    private val broadcastDao: BroadcastDao,
    private val systemConfigDao: SystemConfigDao
) {
    // Flow streams
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allWithdrawals: Flow<List<WithdrawalEntity>> = withdrawalDao.getAllWithdrawals()
    val pendingWithdrawals: Flow<List<WithdrawalEntity>> = withdrawalDao.getPendingWithdrawals()
    val allCampaigns: Flow<List<AdCampaignEntity>> = adCampaignDao.getAllCampaigns()
    val activeCampaigns: Flow<List<AdCampaignEntity>> = adCampaignDao.getActiveCampaigns()
    val allComplaints: Flow<List<ComplaintEntity>> = complaintDao.getAllComplaints()
    val openComplaints: Flow<List<ComplaintEntity>> = complaintDao.getOpenComplaints()
    val allBroadcasts: Flow<List<BroadcastEntity>> = broadcastDao.getAllBroadcasts()
    val recentBroadcasts: Flow<List<BroadcastEntity>> = broadcastDao.getRecentBroadcasts()
    val systemConfig: Flow<SystemConfigEntity?> = systemConfigDao.getConfigFlow()

    fun getUserFlow(userId: Long): Flow<UserEntity?> = userDao.getUserByIdFlow(userId)
    fun getUserFlowByUsername(username: String): Flow<UserEntity?> = userDao.getUserByUsernameFlow(username)
    fun getUserTransactions(userId: Long, username: String): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsForUser(userId, username)
    fun getUserWithdrawals(userId: Long, username: String): Flow<List<WithdrawalEntity>> =
        withdrawalDao.getWithdrawalsForUser(userId, username)
    fun getUserComplaints(userId: Long, username: String): Flow<List<ComplaintEntity>> =
        complaintDao.getComplaintsForUser(userId, username)
    fun getDirectReferrals(sponsorUsername: String): Flow<List<UserEntity>> =
        userDao.getDirectReferrals(sponsorUsername)

    suspend fun getUserById(userId: Long): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUserById(userId)
    }

    suspend fun getUserByUsername(username: String): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUserByUsername(username)
    }

    suspend fun updateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    suspend fun getConfig(): SystemConfigEntity = withContext(Dispatchers.IO) {
        systemConfigDao.getConfig() ?: SystemConfigEntity()
    }

    suspend fun updateConfig(config: SystemConfigEntity) = withContext(Dispatchers.IO) {
        systemConfigDao.updateConfig(config)
    }

    // Authentication & Profile
    suspend fun login(usernameOrEmail: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByUsername(usernameOrEmail)
        if (user != null) {
            if (!user.isActive) {
                return@withContext Result.failure(Exception("Account is deactivated. Please contact support."))
            }
            if (user.password == password) {
                return@withContext Result.success(user)
            }
        }
        Result.failure(Exception("Invalid username or password"))
    }

    suspend fun updateUserProfile(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    suspend fun getAvailablePlacementLegs(placementUsername: String): List<String> = withContext(Dispatchers.IO) {
        val cleanPlacement = placementUsername.trim().lowercase()
        if (cleanPlacement.isEmpty()) return@withContext listOf("A", "B", "C")
        val children = userDao.getDirectPlacementChildren(cleanPlacement)
        val occupiedLegs = children.map { it.placementLeg.uppercase() }.toSet()
        listOf("A", "B", "C").filter { it !in occupiedLegs }
    }

    suspend fun getVacantSlotsForUser(placementUsername: String): List<VacantSlot> = withContext(Dispatchers.IO) {
        val cleanRoot = placementUsername.trim().lowercase()
        if (cleanRoot.isEmpty()) return@withContext emptyList()
        val rootUser = userDao.getUserByUsername(cleanRoot) ?: return@withContext emptyList()
        val result = mutableListOf<VacantSlot>()

        // 1. Direct vacant legs of target root node
        val rootChildren = userDao.getDirectPlacementChildren(rootUser.username)
        val occupiedRootLegs = rootChildren.map { it.placementLeg.uppercase() }.toSet()
        listOf("A", "B", "C").forEach { leg ->
            if (leg !in occupiedRootLegs) {
                result.add(VacantSlot(nodeId = rootUser.username, leg = leg))
            }
        }

        // 2. Vacant legs of downline nodes (level 1 children) under root
        rootChildren.forEach { child ->
            val grandChildren = userDao.getDirectPlacementChildren(child.username)
            val occupiedChildLegs = grandChildren.map { it.placementLeg.uppercase() }.toSet()
            listOf("A", "B", "C").forEach { leg ->
                if (leg !in occupiedChildLegs) {
                    result.add(VacantSlot(nodeId = child.username, leg = leg))
                }
            }
        }

        result
    }

    suspend fun updatePaymentSettings(
        userId: Long,
        bKash: String,
        nagad: String,
        rocket: String,
        upay: String,
        bankName: String,
        bankAccount: String,
        bankBranch: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId) ?: return@withContext Result.failure(Exception("User not found"))
        val updated = user.copy(
            bKashNumber = bKash,
            nagadNumber = nagad,
            rocketNumber = rocket,
            upayNumber = upay,
            bankName = bankName,
            bankAccountNo = bankAccount,
            bankBranch = bankBranch
        )
        userDao.updateUser(updated)
        Result.success(Unit)
    }

    // Joining & MLM Financial Engine (5 Generations + Direct Refer + 3-Leg Team updates + Rank Auto-check)
    suspend fun registerNewMember(
        initiatorUserId: Long,
        newUsername: String,
        fullName: String,
        email: String,
        phone: String,
        nidNumber: String = "",
        gender: String = "Male",
        password: String,
        sponsorUsername: String,
        placementUsername: String,
        placementLeg: String, // "A", "B", "C"
        packageId: Int
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanUsername = newUsername.trim().lowercase()
        val cleanSponsor = sponsorUsername.trim().lowercase()
        val cleanPlacement = (if (placementUsername.isBlank()) cleanSponsor else placementUsername).trim().lowercase()
        val leg = placementLeg.trim().uppercase()

        if (cleanUsername.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            return@withContext Result.failure(Exception("All required fields must be filled"))
        }

        // Check if username already exists
        if (userDao.getUserByUsername(cleanUsername) != null) {
            return@withContext Result.failure(Exception("Username @$cleanUsername is already taken. Please choose another username."))
        }

        // Check sponsor exists
        val sponsor = userDao.getUserByUsername(cleanSponsor)
            ?: return@withContext Result.failure(Exception("Sponsor/Reference @$cleanSponsor does not exist"))

        // Check placement exists
        val placement = userDao.getUserByUsername(cleanPlacement)
            ?: return@withContext Result.failure(Exception("Placement user @$cleanPlacement does not exist"))

        // Check if placement leg is already occupied
        val existingChild = userDao.getDirectLegChild(cleanPlacement, leg)
        if (existingChild != null) {
            return@withContext Result.failure(Exception("Team $leg under @$cleanPlacement is already occupied by @${existingChild.username}"))
        }

        val config = getConfig()
        val joiningFee = config.baseJoiningFee

        // If registered from member wallet, verify balance >= joining fee
        val initiator = userDao.getUserById(initiatorUserId)
        if (initiator != null && initiator.role != "ADMIN") {
            if (initiator.walletBalance < joiningFee) {
                return@withContext Result.failure(Exception("Insufficient wallet balance. Account activation requires ৳ ${String.format("%,.0f", joiningFee)} PVC. Available balance: ৳ ${String.format("%,.2f", initiator.walletBalance)} PVC"))
            }
            // Deduct joining fee from opener
            val updatedInitiator = initiator.copy(
                walletBalance = initiator.walletBalance - joiningFee,
                totalJoiningIncome = initiator.totalJoiningIncome + joiningFee
            )
            userDao.updateUser(updatedInitiator)

            transactionDao.insertTransaction(
                TransactionEntity(
                    userId = initiator.id,
                    username = initiator.username,
                    type = "JOINING_FEE",
                    amount = joiningFee,
                    netAmount = joiningFee,
                    description = "Member Registration fee deducted for new user @$cleanUsername",
                    counterpartUser = cleanUsername
                )
            )
        }

        // Create new user entity
        val newUser = UserEntity(
            username = cleanUsername,
            fullName = fullName,
            email = email,
            phone = phone,
            nidNumber = nidNumber,
            gender = gender,
            password = password,
            sponsorUsername = cleanSponsor,
            placementUsername = cleanPlacement,
            placementLeg = leg,
            packageId = packageId,
            walletBalance = 0.0,
            currentRank = "Member"
        )
        val newUserId = userDao.insertUser(newUser)
        val createdUser = newUser.copy(id = newUserId)

        // 1. Direct Referral Bonus (৳ 300 PVC Default, Changeable) credited to Sponsor immediately
        val referBonus = config.directReferBonus
        if (referBonus > 0) {
            val updatedSponsor = sponsor.copy(
                walletBalance = sponsor.walletBalance + referBonus,
                totalIncome = sponsor.totalIncome + referBonus,
                referralIncome = sponsor.referralIncome + referBonus
            )
            userDao.updateUser(updatedSponsor)
            transactionDao.insertTransaction(
                TransactionEntity(
                    userId = updatedSponsor.id,
                    username = updatedSponsor.username,
                    type = "REFER_BONUS",
                    amount = referBonus,
                    netAmount = referBonus,
                    description = "Direct Referral Bonus for sponsoring @$cleanUsername",
                    counterpartUser = cleanUsername
                )
            )
        }

        // 2. 5-Tier Generation Engine Payouts (Changeable: 1st: 100, 2nd: 50, 3rd: 20, 4th: 10, 5th: 10)
        val genBonuses = listOf(
            config.gen1Bonus,
            config.gen2Bonus,
            config.gen3Bonus,
            config.gen4Bonus,
            config.gen5Bonus
        )

        var currentUplineUsername = cleanSponsor
        for (tier in 1..5) {
            val uplineUser = userDao.getUserByUsername(currentUplineUsername) ?: break
            val bonus = genBonuses.getOrElse(tier - 1) { 0.0 }
            if (bonus > 0) {
                val updatedUpline = uplineUser.copy(
                    walletBalance = uplineUser.walletBalance + bonus,
                    totalIncome = uplineUser.totalIncome + bonus,
                    generationIncome = uplineUser.generationIncome + bonus
                )
                userDao.updateUser(updatedUpline)
                transactionDao.insertTransaction(
                    TransactionEntity(
                        userId = updatedUpline.id,
                        username = updatedUpline.username,
                        type = "GEN_BONUS",
                        amount = bonus,
                        netAmount = bonus,
                        description = "Generation $tier Commission for new user @$cleanUsername",
                        counterpartUser = cleanUsername
                    )
                )
            }
            currentUplineUsername = uplineUser.sponsorUsername
            if (currentUplineUsername.isEmpty()) break
        }

        // 3. Increment 3-Leg Downline Team Counts upwards along placement chain
        updateTeamCountsAndRanks(cleanPlacement, leg)

        Result.success(createdUser)
    }

    private suspend fun updateTeamCountsAndRanks(startPlacementUsername: String, startLeg: String) {
        var currentPlacement = startPlacementUsername
        var childLeg = startLeg

        while (currentPlacement.isNotEmpty()) {
            val user = userDao.getUserByUsername(currentPlacement) ?: break
            val updatedUser = when (childLeg) {
                "A" -> user.copy(teamACount = user.teamACount + 1)
                "B" -> user.copy(teamBCount = user.teamBCount + 1)
                "C" -> user.copy(teamCCount = user.teamCCount + 1)
                else -> user
            }
            // Auto calculate Rank according to career matrix
            val evaluatedRank = evaluateRank(
                updatedUser.teamACount,
                updatedUser.teamBCount,
                updatedUser.teamCCount,
                updatedUser.teamAGMsA,
                updatedUser.teamAGMsB,
                updatedUser.teamAGMsC,
                updatedUser.teamGMsA,
                updatedUser.teamGMsB,
                updatedUser.teamGMsC
            )
            val finalUser = updatedUser.copy(currentRank = evaluatedRank)
            userDao.updateUser(finalUser)

            // Move to next upline in placement tree
            childLeg = finalUser.placementLeg
            currentPlacement = finalUser.placementUsername
        }
    }

    fun evaluateRank(
        teamA: Int,
        teamB: Int,
        teamC: Int,
        agmsA: Int = 0,
        agmsB: Int = 0,
        agmsC: Int = 0,
        gmsA: Int = 0,
        gmsB: Int = 0,
        gmsC: Int = 0
    ): String {
        return when {
            gmsA >= 1 && gmsB >= 1 && gmsC >= 1 -> "CEO"
            agmsA >= 1 && agmsB >= 1 && agmsC >= 1 -> "GM"
            teamA >= 1800 && teamB >= 1800 && teamC >= 1800 -> "AGM"
            teamA >= 1200 && teamB >= 1200 && teamC >= 1200 -> "Manager"
            teamA >= 600 && teamB >= 600 && teamC >= 600 -> "Sales Manager"
            teamA >= 300 && teamB >= 300 && teamC >= 300 -> "SR"
            teamA >= 100 && teamB >= 100 && teamC >= 100 -> "Tour Winner"
            else -> "Member"
        }
    }

    // PVC Member Transfer (with 5% platform fee deduction)
    suspend fun transferPvc(
        senderId: Long,
        recipientUsername: String,
        amount: Double,
        pin: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val cleanRecipient = recipientUsername.trim().lowercase()
        if (amount <= 0) return@withContext Result.failure(Exception("Transfer amount must be greater than 0"))

        val sender = userDao.getUserById(senderId)
            ?: return@withContext Result.failure(Exception("Sender not found"))

        if (sender.pin != pin) {
            return@withContext Result.failure(Exception("Invalid Security PIN"))
        }

        if (sender.username == cleanRecipient) {
            return@withContext Result.failure(Exception("Cannot transfer PVC to yourself"))
        }

        val recipient = userDao.getUserByUsername(cleanRecipient)
            ?: return@withContext Result.failure(Exception("Recipient @$cleanRecipient not found"))

        if (!recipient.isActive) {
            return@withContext Result.failure(Exception("Recipient account is deactivated"))
        }

        val config = getConfig()
        val feeRate = config.transferFeePercent / 100.0
        val fee = amount * feeRate
        val totalDeduction = amount // Senders wallet is debited by amount, net received is amount - fee (or sender pays amount, fee deducted)
        val netReceived = amount - fee

        if (sender.walletBalance < totalDeduction) {
            return@withContext Result.failure(Exception("Insufficient PVC balance. Required: ৳ $totalDeduction, Available: ৳ ${sender.walletBalance}"))
        }

        // Deduct from sender
        val updatedSender = sender.copy(walletBalance = sender.walletBalance - totalDeduction)
        userDao.updateUser(updatedSender)

        // Credit to recipient
        val updatedRecipient = recipient.copy(walletBalance = recipient.walletBalance + netReceived)
        userDao.updateUser(updatedRecipient)

        // Sender Transaction record
        transactionDao.insertTransaction(
            TransactionEntity(
                userId = sender.id,
                username = sender.username,
                type = "TRANSFER_OUT",
                amount = amount,
                fee = fee,
                netAmount = netReceived,
                description = "PVC Transfer to @$cleanRecipient (5% platform fee: ৳ $fee)",
                counterpartUser = cleanRecipient
            )
        )

        // Recipient Transaction record
        transactionDao.insertTransaction(
            TransactionEntity(
                userId = recipient.id,
                username = recipient.username,
                type = "TRANSFER_IN",
                amount = netReceived,
                fee = 0.0,
                netAmount = netReceived,
                description = "PVC Transfer received from @${sender.username}",
                counterpartUser = sender.username
            )
        )

        Result.success(Unit)
    }

    // PVC Withdrawal (with 10% VAT deduction)
    suspend fun requestWithdrawal(
        userId: Long,
        gateway: String,
        accountNumber: String,
        accountType: String,
        grossAmount: Double,
        bankName: String = "",
        bankBranch: String = ""
    ): Result<WithdrawalEntity> = withContext(Dispatchers.IO) {
        val config = getConfig()
        if (grossAmount < config.minWithdrawalAmount) {
            return@withContext Result.failure(Exception("Minimum withdrawal amount is ৳ ${String.format("%,.0f", config.minWithdrawalAmount)} PVC"))
        }
        if (accountNumber.isBlank()) {
            return@withContext Result.failure(Exception("Please enter a valid payout account number"))
        }

        val user = userDao.getUserById(userId)
            ?: return@withContext Result.failure(Exception("User not found"))

        if (user.walletBalance < grossAmount) {
            return@withContext Result.failure(Exception("Insufficient wallet balance. Required: ৳ ${String.format("%,.2f", grossAmount)}, Available: ৳ ${String.format("%,.2f", user.walletBalance)} PVC"))
        }

        val vatPercent = config.withdrawVatPercent
        val vatAmount = grossAmount * (vatPercent / 100.0)
        val netAmount = grossAmount - vatAmount

        // Deduct immediately from balance and log withdrawal record
        val updatedUser = user.copy(
            walletBalance = user.walletBalance - grossAmount,
            totalWithdraw = user.totalWithdraw + grossAmount
        )
        userDao.updateUser(updatedUser)

        val withdrawal = WithdrawalEntity(
            userId = user.id,
            username = user.username,
            gateway = gateway,
            accountNumber = accountNumber,
            accountType = accountType,
            bankName = bankName,
            bankBranch = bankBranch,
            grossAmount = grossAmount,
            vatAmount = vatAmount,
            netAmount = netAmount,
            status = "PENDING"
        )
        val withdrawalId = withdrawalDao.insertWithdrawal(withdrawal)

        val bankDetailStr = if (gateway.equals("Bank", ignoreCase = true) && bankName.isNotBlank()) {
            " ($bankName - $bankBranch)"
        } else ""

        transactionDao.insertTransaction(
            TransactionEntity(
                userId = user.id,
                username = user.username,
                type = "WITHDRAWAL",
                amount = grossAmount,
                fee = vatAmount,
                netAmount = netAmount,
                description = "Withdrawal request via $gateway$bankDetailStr to $accountNumber (${String.format("%.0f", vatPercent)}% VAT: ৳ $vatAmount, Net: ৳ $netAmount)",
                reference = "WID-$withdrawalId",
                status = "PENDING"
            )
        )

        Result.success(withdrawal.copy(id = withdrawalId))
    }

    // Daily Ad Work Completion (2 Ads per day default, 5 PVC per ad = 10 PVC daily)
    suspend fun completeDailyAd(
        userId: Long,
        campaignId: Long
    ): Result<Double> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
            ?: return@withContext Result.failure(Exception("User not found"))

        val config = getConfig()
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val isNewDay = user.lastAdWatchDate != todayDate
        val watchedCount = if (isNewDay) 0 else user.adsWatchedToday

        val maxAdsAllowed = config.dailyAdLimit

        if (watchedCount >= maxAdsAllowed) {
            return@withContext Result.failure(Exception("Daily ad limit reached ($maxAdsAllowed ads/day). Please return tomorrow after midnight!"))
        }

        val campaign = adCampaignDao.getCampaignById(campaignId)
        val reward = campaign?.rewardAmount ?: config.adRewardPerUnit

        val updatedUser = user.copy(
            walletBalance = user.walletBalance + reward,
            totalIncome = user.totalIncome + reward,
            dailyWorkIncome = user.dailyWorkIncome + reward,
            adsWatchedToday = watchedCount + 1,
            lastAdWatchDate = todayDate
        )
        userDao.updateUser(updatedUser)

        // Increment campaign impressions
        if (campaign != null) {
            adCampaignDao.updateCampaign(campaign.copy(totalImpressions = campaign.totalImpressions + 1))
        }

        transactionDao.insertTransaction(
            TransactionEntity(
                userId = user.id,
                username = user.username,
                type = "DAILY_WORK",
                amount = reward,
                netAmount = reward,
                description = "Daily Ad Task reward #${watchedCount + 1}/$maxAdsAllowed completed (৳ ${String.format("%.2f", reward)} earned)",
                counterpartUser = "AD_SPONSOR"
            )
        )

        Result.success(reward)
    }

    suspend fun updateAdCampaign(campaign: AdCampaignEntity): Result<Unit> = withContext(Dispatchers.IO) {
        adCampaignDao.updateCampaign(campaign)
        Result.success(Unit)
    }

    suspend fun randomizeAllAdCampaigns(): Result<Unit> = withContext(Dispatchers.IO) {
        val campaigns = adCampaignDao.getAllCampaignsList()
        val possibleDurations = listOf(5, 10, 15)
        val possibleRewards = listOf(5.0, 6.0, 7.5, 10.0)
        campaigns.forEach { camp ->
            val newDuration = possibleDurations.random()
            val newReward = possibleRewards.random()
            adCampaignDao.updateCampaign(
                camp.copy(
                    durationSeconds = newDuration,
                    rewardAmount = newReward
                )
            )
        }
        Result.success(Unit)
    }

    // Admin Operations
    suspend fun approveWithdrawal(withdrawalId: Long, remark: String): Result<Unit> = withContext(Dispatchers.IO) {
        val withdrawal = withdrawalDao.getWithdrawalById(withdrawalId)
            ?: return@withContext Result.failure(Exception("Withdrawal not found"))

        val updated = withdrawal.copy(
            status = "APPROVED",
            adminRemark = remark.ifBlank { "Approved & Payout Disbursed" },
            processedTimestamp = System.currentTimeMillis()
        )
        withdrawalDao.updateWithdrawal(updated)
        Result.success(Unit)
    }

    suspend fun rejectWithdrawal(withdrawalId: Long, reason: String): Result<Unit> = withContext(Dispatchers.IO) {
        val withdrawal = withdrawalDao.getWithdrawalById(withdrawalId)
            ?: return@withContext Result.failure(Exception("Withdrawal not found"))

        val updated = withdrawal.copy(
            status = "REJECTED",
            adminRemark = reason.ifBlank { "Rejected by Administrator" },
            processedTimestamp = System.currentTimeMillis()
        )
        withdrawalDao.updateWithdrawal(updated)

        // Refund PVC back to user wallet
        val user = userDao.getUserById(withdrawal.userId)
        if (user != null) {
            val refundedUser = user.copy(
                walletBalance = user.walletBalance + withdrawal.grossAmount,
                totalWithdraw = (user.totalWithdraw - withdrawal.grossAmount).coerceAtLeast(0.0)
            )
            userDao.updateUser(refundedUser)

            transactionDao.insertTransaction(
                TransactionEntity(
                    userId = user.id,
                    username = user.username,
                    type = "WITHDRAWAL_REFUND",
                    amount = withdrawal.grossAmount,
                    netAmount = withdrawal.grossAmount,
                    description = "Withdrawal rejected & refunded: $reason",
                    reference = "REFUND-WID-$withdrawalId"
                )
            )
        }
        Result.success(Unit)
    }

    suspend fun adminAdjustBalance(
        targetUserId: Long,
        amount: Double,
        isCredit: Boolean,
        remark: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(targetUserId)
            ?: return@withContext Result.failure(Exception("User not found"))

        val newBalance = if (isCredit) {
            user.walletBalance + amount
        } else {
            (user.walletBalance - amount).coerceAtLeast(0.0)
        }

        val updatedUser = user.copy(
            walletBalance = newBalance,
            totalIncome = if (isCredit) user.totalIncome + amount else user.totalIncome
        )
        userDao.updateUser(updatedUser)

        transactionDao.insertTransaction(
            TransactionEntity(
                userId = user.id,
                username = user.username,
                type = if (isCredit) "ADMIN_CREDIT" else "ADMIN_DEBIT",
                amount = amount,
                netAmount = amount,
                description = "Admin ${if (isCredit) "Manual Credit" else "Manual Debit"}: $remark",
                counterpartUser = "MASTER_ADMIN"
            )
        )
        Result.success(Unit)
    }

    suspend fun adminToggleUserStatus(userId: Long): Result<Boolean> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
            ?: return@withContext Result.failure(Exception("User not found"))
        val newStatus = !user.isActive
        userDao.updateUser(user.copy(isActive = newStatus))
        Result.success(newStatus)
    }

    suspend fun adminDeleteUser(userId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
            ?: return@withContext Result.failure(Exception("User not found"))
        if (user.role == "ADMIN") {
            return@withContext Result.failure(Exception("Cannot delete Master Admin account"))
        }
        userDao.deleteUser(user)
        Result.success(Unit)
    }

    // Complaints / Support Tickets
    suspend fun submitComplaint(
        userId: Long,
        category: String,
        subject: String,
        description: String,
        priority: String
    ): Result<ComplaintEntity> = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId)
            ?: return@withContext Result.failure(Exception("User not found"))

        val complaint = ComplaintEntity(
            userId = user.id,
            username = user.username,
            category = category,
            subject = subject,
            description = description,
            priority = priority,
            status = "OPEN"
        )
        val id = complaintDao.insertComplaint(complaint)
        Result.success(complaint.copy(id = id))
    }

    suspend fun replyComplaint(
        complaintId: Long,
        reply: String,
        newStatus: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val complaint = complaintDao.getComplaintById(complaintId)
            ?: return@withContext Result.failure(Exception("Ticket not found"))

        val updated = complaint.copy(
            adminReply = reply,
            status = newStatus,
            replyTimestamp = System.currentTimeMillis()
        )
        complaintDao.updateComplaint(updated)
        Result.success(Unit)
    }

    // Broadcasts
    suspend fun postBroadcast(title: String, message: String, isUrgent: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        broadcastDao.insertBroadcast(
            BroadcastEntity(
                title = title,
                message = message,
                isUrgent = isUrgent
            )
        )
        Result.success(Unit)
    }

    suspend fun deleteBroadcast(id: Long) = withContext(Dispatchers.IO) {
        broadcastDao.deleteBroadcastById(id)
    }

    // Campaigns
    suspend fun saveCampaign(campaign: AdCampaignEntity) = withContext(Dispatchers.IO) {
        if (campaign.id == 0L) {
            adCampaignDao.insertCampaign(campaign)
        } else {
            adCampaignDao.updateCampaign(campaign)
        }
    }

    suspend fun deleteCampaign(id: Long) = withContext(Dispatchers.IO) {
        adCampaignDao.deleteCampaignById(id)
    }

    // Multilevel Tree Query
    suspend fun getGenerationTreeData(rootUsername: String, maxDepth: Int = 4): TreeNode = withContext(Dispatchers.IO) {
        val rootUser = userDao.getUserByUsername(rootUsername)
            ?: return@withContext TreeNode("root", "Unknown", "Member", 1, true, "ROOT", emptyList(), 0.0)

        suspend fun buildNode(user: UserEntity, currentDepth: Int): TreeNode {
            if (currentDepth >= maxDepth) {
                return TreeNode(
                    username = user.username,
                    fullName = user.fullName,
                    rank = user.currentRank,
                    packageId = user.packageId,
                    isActive = user.isActive,
                    leg = user.placementLeg,
                    children = emptyList(),
                    totalIncome = user.totalIncome,
                    teamSize = user.teamACount + user.teamBCount + user.teamCCount
                )
            }
            val directPlacements = userDao.getDirectPlacementChildren(user.username)
            val childrenNodes = directPlacements.map { child ->
                buildNode(child, currentDepth + 1)
            }
            return TreeNode(
                username = user.username,
                fullName = user.fullName,
                rank = user.currentRank,
                packageId = user.packageId,
                isActive = user.isActive,
                leg = user.placementLeg,
                children = childrenNodes,
                totalIncome = user.totalIncome,
                teamSize = user.teamACount + user.teamBCount + user.teamCCount
            )
        }

        buildNode(rootUser, 0)
    }
}

data class TreeNode(
    val username: String,
    val fullName: String,
    val rank: String,
    val packageId: Int,
    val isActive: Boolean,
    val leg: String,
    val children: List<TreeNode>,
    val totalIncome: Double,
    val teamSize: Int = 0
)

data class VacantSlot(
    val nodeId: String,
    val leg: String
)
