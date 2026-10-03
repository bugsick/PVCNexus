package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.entity.AdCampaignEntity
import com.example.data.entity.BroadcastEntity
import com.example.data.entity.ComplaintEntity
import com.example.data.entity.SystemConfigEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.UserEntity
import com.example.data.entity.WithdrawalEntity
import com.example.data.repository.PvcNexusRepository
import com.example.data.repository.TreeNode
import com.example.data.repository.VacantSlot
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    // Auth
    object Login : ScreenDestination()
    object QuickAccountSelect : ScreenDestination()

    // User Portal Tabs
    object UserDashboard : ScreenDestination()
    object SalesTeam : ScreenDestination()
    object GenerationTree : ScreenDestination()
    object IncomePvc : ScreenDestination()
    object JoiningPvc : ScreenDestination()
    object PaymentSettings : ScreenDestination()
    object PvcWithdraw : ScreenDestination()
    object WithdrawReport : ScreenDestination()
    object PvcTransfer : ScreenDestination()
    object TransferReport : ScreenDestination()
    object DailyWork : ScreenDestination()
    object ReferPvc : ScreenDestination()
    object JoiningForm : ScreenDestination()
    object JoiningList : ScreenDestination()
    object RankGallery : ScreenDestination()
    object SupportTickets : ScreenDestination()
    object AboutUs : ScreenDestination()
    object ManagementSection : ScreenDestination()
    object ContactUs : ScreenDestination()

    // Admin Suite Tabs
    object AdminDashboard : ScreenDestination()
    object AdminUserOps : ScreenDestination()
    object AdminGlobalRates : ScreenDestination()
    object AdminWithdrawalAuth : ScreenDestination()
    object AdminAdManager : ScreenDestination()
    object AdminBroadcasts : ScreenDestination()
    object AdminSupportDesk : ScreenDestination()
}

class PvcNexusViewModel(
    application: Application,
    private val repository: PvcNexusRepository
) : AndroidViewModel(application) {

    // Current Logged In User ID & Object
    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = _currentUserId.flatMapLatest { id ->
        if (id != null) repository.getUserFlow(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Navigation State
    private val _currentDestination = MutableStateFlow<ScreenDestination>(ScreenDestination.Login)
    val currentDestination: StateFlow<ScreenDestination> = _currentDestination.asStateFlow()

    // Snackbars / Toast events
    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage.asSharedFlow()

    // Global Streams
    val systemConfig: StateFlow<SystemConfigEntity> = repository.systemConfig
        .flatMapLatest { config -> flowOf(config ?: SystemConfigEntity()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SystemConfigEntity())

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCampaigns: StateFlow<List<AdCampaignEntity>> = repository.allCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeAdCampaigns: StateFlow<List<AdCampaignEntity>> = repository.activeCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBroadcasts: StateFlow<List<BroadcastEntity>> = repository.allBroadcasts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingWithdrawals: StateFlow<List<WithdrawalEntity>> = repository.pendingWithdrawals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWithdrawals: StateFlow<List<WithdrawalEntity>> = repository.allWithdrawals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allComplaints: StateFlow<List<ComplaintEntity>> = repository.allComplaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User-specific Streams
    val userTransactions: StateFlow<List<TransactionEntity>> = combine(currentUser, _currentUserId) { user, _ ->
        user
    }.flatMapLatest { user ->
        if (user != null) repository.getUserTransactions(user.id, user.username) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userWithdrawals: StateFlow<List<WithdrawalEntity>> = combine(currentUser, _currentUserId) { user, _ ->
        user
    }.flatMapLatest { user ->
        if (user != null) repository.getUserWithdrawals(user.id, user.username) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userComplaints: StateFlow<List<ComplaintEntity>> = combine(currentUser, _currentUserId) { user, _ ->
        user
    }.flatMapLatest { user ->
        if (user != null) repository.getUserComplaints(user.id, user.username) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userDirectReferrals: StateFlow<List<UserEntity>> = combine(currentUser, _currentUserId) { user, _ ->
        user
    }.flatMapLatest { user ->
        if (user != null) repository.getDirectReferrals(user.username) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Tree Node state
    private val _generationTree = MutableStateFlow<TreeNode?>(null)
    val generationTree: StateFlow<TreeNode?> = _generationTree.asStateFlow()

    // Daily Ad Simulator Timer
    private val _isAdWatching = MutableStateFlow(false)
    val isAdWatching: StateFlow<Boolean> = _isAdWatching.asStateFlow()

    private val _adWatchProgress = MutableStateFlow(0f)
    val adWatchProgress: StateFlow<Float> = _adWatchProgress.asStateFlow()

    private val _adWatchSecondsRemaining = MutableStateFlow(10)
    val adWatchSecondsRemaining: StateFlow<Int> = _adWatchSecondsRemaining.asStateFlow()

    private val _activeWatchingCampaign = MutableStateFlow<AdCampaignEntity?>(null)
    val activeWatchingCampaign: StateFlow<AdCampaignEntity?> = _activeWatchingCampaign.asStateFlow()

    private var adWatchJob: Job? = null

    fun navigateTo(dest: ScreenDestination) {
        _currentDestination.value = dest
        if (dest == ScreenDestination.GenerationTree) {
            loadGenerationTree()
        }
    }

    fun loadGenerationTree() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val root = repository.getGenerationTreeData(user.username)
            _generationTree.value = root
        }
    }

    fun login(username: String, pass: String) {
        viewModelScope.launch {
            val res = repository.login(username.trim().lowercase(), pass)
            res.onSuccess { user ->
                _currentUserId.value = user.id
                if (user.role == "ADMIN") {
                    _currentDestination.value = ScreenDestination.AdminDashboard
                    _uiMessage.emit("Welcome to Master Admin Command Suite, ${user.fullName}!")
                } else {
                    _currentDestination.value = ScreenDestination.UserDashboard
                    _uiMessage.emit("Welcome back, ${user.fullName}!")
                }
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Login failed")
            }
        }
    }

    fun switchAccount(username: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null) {
                _currentUserId.value = user.id
                if (user.role == "ADMIN") {
                    _currentDestination.value = ScreenDestination.AdminDashboard
                } else {
                    _currentDestination.value = ScreenDestination.UserDashboard
                }
                _uiMessage.emit("Switched profile to @${user.username} (${user.role})")
            }
        }
    }

    fun logout() {
        _currentUserId.value = null
        _currentDestination.value = ScreenDestination.Login
    }

    fun changePackage(newPackageId: Int) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val updated = current.copy(packageId = newPackageId)
            repository.updateUser(updated)
            _uiMessage.emit("Switched to Package $newPackageId (${newPackageId * 2} Ads/day, ৳ ${newPackageId * 10}/day potential)!")
        }
    }

    // Available Placement Legs Query
    fun checkAvailableLegs(placementUsername: String, onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            val legs = repository.getAvailablePlacementLegs(placementUsername)
            onResult(legs)
        }
    }

    // Dynamic Vacant Slots Query for Tree Placement (Feature Specification)
    fun queryVacantSlots(placementUsername: String, onResult: (List<VacantSlot>) -> Unit) {
        viewModelScope.launch {
            val slots = repository.getVacantSlotsForUser(placementUsername)
            onResult(slots)
        }
    }

    // Register Member
    fun registerNewMember(
        fullName: String,
        username: String,
        phone: String,
        nidNumber: String = "",
        gender: String = "Male",
        email: String,
        password: String,
        sponsorUsername: String,
        placementUsername: String = "",
        placementLeg: String,
        packageId: Int = 1,
        onSuccess: () -> Unit
    ) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val res = repository.registerNewMember(
                initiatorUserId = current.id,
                newUsername = username,
                fullName = fullName,
                email = email,
                phone = phone,
                nidNumber = nidNumber,
                gender = gender,
                password = password,
                sponsorUsername = sponsorUsername,
                placementUsername = if (placementUsername.isBlank()) sponsorUsername else placementUsername,
                placementLeg = placementLeg,
                packageId = packageId
            )
            res.onSuccess { created ->
                _uiMessage.emit("Member @${created.username} registered successfully! 1,500 PVC deducted & 5-Gen + Refer bonuses distributed.")
                onSuccess()
            }.onFailure { err ->
                _uiMessage.emit("Registration Failed: ${err.message}")
            }
        }
    }

    // Transfer PVC
    fun transferPvc(recipient: String, amount: Double, pin: String, onSuccess: () -> Unit) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val res = repository.transferPvc(current.id, recipient, amount, pin)
            res.onSuccess {
                _uiMessage.emit("Transferred ৳ $amount PVC to @$recipient successfully (5% platform fee applied).")
                onSuccess()
            }.onFailure { err ->
                _uiMessage.emit("Transfer Failed: ${err.message}")
            }
        }
    }

    // Request Withdrawal
    fun withdrawPvc(
        gateway: String,
        account: String,
        accountType: String,
        amount: Double,
        bankName: String = "",
        bankBranch: String = "",
        onSuccess: () -> Unit
    ) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val res = repository.requestWithdrawal(current.id, gateway, account, accountType, amount, bankName, bankBranch)
            res.onSuccess { wid ->
                _uiMessage.emit("Withdrawal request for ৳ ${wid.grossAmount} submitted! Net receivable: ৳ ${wid.netAmount} (10% VAT deducted).")
                onSuccess()
            }.onFailure { err ->
                _uiMessage.emit("Withdrawal Failed: ${err.message}")
            }
        }
    }

    // Daily Ad Watcher
    fun startAdWatch(campaign: AdCampaignEntity) {
        if (_isAdWatching.value) return
        _activeWatchingCampaign.value = campaign
        _isAdWatching.value = true
        _adWatchProgress.value = 0f
        _adWatchSecondsRemaining.value = campaign.durationSeconds

        adWatchJob?.cancel()
        adWatchJob = viewModelScope.launch {
            val duration = campaign.durationSeconds
            for (sec in duration downTo 1) {
                _adWatchSecondsRemaining.value = sec
                _adWatchProgress.value = 1f - (sec.toFloat() / duration.toFloat())
                delay(1000)
            }
            _adWatchProgress.value = 1f
            _adWatchSecondsRemaining.value = 0
            completeActiveAd(campaign)
        }
    }

    private fun completeActiveAd(campaign: AdCampaignEntity) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val res = repository.completeDailyAd(current.id, campaign.id)
            res.onSuccess { reward ->
                _uiMessage.emit("Task Completed! ৳ $reward PVC credited to your wallet balance.")
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Task error")
            }
            _isAdWatching.value = false
            _activeWatchingCampaign.value = null
        }
    }

    fun cancelAdWatch() {
        adWatchJob?.cancel()
        _isAdWatching.value = false
        _activeWatchingCampaign.value = null
        _adWatchProgress.value = 0f
    }

    // Payment settings update
    fun savePaymentSettings(
        bKash: String,
        nagad: String,
        rocket: String,
        upay: String,
        bankName: String,
        bankAcc: String,
        bankBranch: String
    ) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val res = repository.updatePaymentSettings(current.id, bKash, nagad, rocket, upay, bankName, bankAcc, bankBranch)
            res.onSuccess {
                _uiMessage.emit("Payment wallet and bank details updated successfully!")
            }.onFailure { err ->
                _uiMessage.emit(err.message ?: "Update failed")
            }
        }
    }

    // Support ticket submit
    fun submitComplaint(subject: String, desc: String, category: String, priority: String, onSuccess: () -> Unit) {
        val current = currentUser.value ?: return
        viewModelScope.launch {
            val res = repository.submitComplaint(current.id, category, subject, desc, priority)
            res.onSuccess {
                _uiMessage.emit("Support ticket #${it.id} submitted! Support team will respond shortly.")
                onSuccess()
            }.onFailure { err ->
                _uiMessage.emit("Error: ${err.message}")
            }
        }
    }

    // ADMIN ACTIONS
    fun authorizeWithdrawal(withdrawalId: Long, approve: Boolean, remark: String) {
        viewModelScope.launch {
            if (approve) {
                val res = repository.approveWithdrawal(withdrawalId, remark)
                res.onSuccess {
                    _uiMessage.emit("Withdrawal #$withdrawalId APPROVED successfully.")
                }.onFailure { err ->
                    _uiMessage.emit("Error: ${err.message}")
                }
            } else {
                val res = repository.rejectWithdrawal(withdrawalId, remark)
                res.onSuccess {
                    _uiMessage.emit("Withdrawal #$withdrawalId REJECTED and PVC refunded to user.")
                }.onFailure { err ->
                    _uiMessage.emit("Error: ${err.message}")
                }
            }
        }
    }

    fun adminModifyUserBalance(username: String, amount: Double, isCredit: Boolean) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null) {
                val res = repository.adminAdjustBalance(user.id, amount, isCredit, "Admin balance adjustment")
                res.onSuccess {
                    _uiMessage.emit("Balance adjustment of ৳ $amount processed for @$username.")
                }.onFailure { err ->
                    _uiMessage.emit("Adjustment failed: ${err.message}")
                }
            }
        }
    }

    fun adminResetPassword(username: String, newPass: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null) {
                repository.updateUser(user.copy(password = newPass))
                _uiMessage.emit("Password reset successfully for @$username.")
            }
        }
    }

    fun adminToggleUserStatus(username: String, isActive: Boolean) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null) {
                val res = repository.adminToggleUserStatus(user.id)
                res.onSuccess { active ->
                    _uiMessage.emit("User status changed to ${if (active) "ACTIVE" else "DEACTIVATED"}")
                }
            }
        }
    }

    fun updateSystemConfig(config: SystemConfigEntity) {
        viewModelScope.launch {
            repository.updateConfig(config)
            _uiMessage.emit("Global Rates, Fees & VAT updated across ecosystem!")
        }
    }

    fun createAdCampaign(title: String, sponsorName: String, desc: String, reward: Double, durationSeconds: Int = 10) {
        viewModelScope.launch {
            val campaign = AdCampaignEntity(
                title = title,
                sponsorName = sponsorName,
                description = desc,
                rewardAmount = reward,
                durationSeconds = durationSeconds
            )
            repository.saveCampaign(campaign)
            _uiMessage.emit("Ad campaign '$title' published successfully!")
        }
    }

    fun updateAdCampaign(campaign: AdCampaignEntity) {
        viewModelScope.launch {
            repository.updateAdCampaign(campaign)
            _uiMessage.emit("Ad Campaign '${campaign.title}' updated: ${campaign.durationSeconds}s duration, ৳ ${campaign.rewardAmount} PVC payout.")
        }
    }

    fun randomizeAdParameters() {
        viewModelScope.launch {
            repository.randomizeAllAdCampaigns()
            _uiMessage.emit("Ad campaign durations and payout rewards have been randomized!")
        }
    }

    fun createBroadcast(title: String, message: String, isUrgent: Boolean) {
        viewModelScope.launch {
            repository.postBroadcast(title, message, isUrgent)
            _uiMessage.emit("Broadcast published to all members!")
        }
    }

    companion object {
        fun provideFactory(
            application: Application,
            repository: PvcNexusRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PvcNexusViewModel(application, repository) as T
            }
        }
    }
}
