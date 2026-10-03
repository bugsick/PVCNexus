package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.AdCampaignEntity
import com.example.data.entity.BroadcastEntity
import com.example.data.entity.SystemConfigEntity
import com.example.data.entity.UserEntity
import com.example.data.entity.WithdrawalEntity
import com.example.ui.components.FastActionTile
import com.example.ui.components.GlassCard
import com.example.ui.components.NexusGradientButton
import com.example.ui.components.RankPill
import com.example.ui.components.StatusBadge
import com.example.ui.components.SummaryKpiCard
import com.example.ui.screens.user.DrawerNavRow
import com.example.ui.theme.NexusAmber
import com.example.ui.theme.NexusBackground
import com.example.ui.theme.NexusCardBorder
import com.example.ui.theme.NexusCyan
import com.example.ui.theme.NexusEmerald
import com.example.ui.theme.NexusIndigo
import com.example.ui.theme.NexusPurple
import com.example.ui.theme.NexusPurpleLight
import com.example.ui.theme.NexusRose
import com.example.ui.theme.NexusSurface
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTextMuted
import com.example.ui.theme.NexusTextPrimary
import com.example.ui.theme.NexusTextSecondary
import com.example.ui.viewmodel.PvcNexusViewModel
import com.example.ui.viewmodel.ScreenDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainContainer(
    viewModel: PvcNexusViewModel
) {
    val currentDest by viewModel.currentDestination.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF0B1120),
                modifier = Modifier
                    .width(310.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(NexusRose, NexusPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "ADMIN COMMAND", color = NexusRose, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Text(text = "Ecosystem Operations", color = NexusTextSecondary, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerNavRow("Admin Dashboard", Icons.Default.Dashboard, currentDest == ScreenDestination.AdminDashboard, NexusRose) {
                        viewModel.navigateTo(ScreenDestination.AdminDashboard)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("User Control & Ops", Icons.Default.Group, currentDest == ScreenDestination.AdminUserOps, NexusRose) {
                        viewModel.navigateTo(ScreenDestination.AdminUserOps)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Global VAT & Rates", Icons.Default.Tune, currentDest == ScreenDestination.AdminGlobalRates, NexusRose) {
                        viewModel.navigateTo(ScreenDestination.AdminGlobalRates)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Withdrawal Authorizations", Icons.Default.Payment, currentDest == ScreenDestination.AdminWithdrawalAuth, NexusRose) {
                        viewModel.navigateTo(ScreenDestination.AdminWithdrawalAuth)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Sponsored Ads (Add Dawa)", Icons.Default.PlayCircle, currentDest == ScreenDestination.AdminAdManager, NexusRose) {
                        viewModel.navigateTo(ScreenDestination.AdminAdManager)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Promotional Broadcasts", Icons.Default.Campaign, currentDest == ScreenDestination.AdminBroadcasts, NexusRose) {
                        viewModel.navigateTo(ScreenDestination.AdminBroadcasts)
                        scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerNavRow("Switch to User Portal", Icons.Default.Person, false, NexusCyan) {
                        viewModel.navigateTo(ScreenDestination.UserDashboard)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Logout Session", Icons.Default.Logout, false, NexusRose) {
                        viewModel.logout()
                        scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    ) {
        Scaffold(
            containerColor = NexusBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NexusRose.copy(alpha = 0.2f))
                                    .border(1.dp, NexusRose, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(text = "ADMIN SUITE", color = NexusRose, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "PVC NEXUS", color = NexusTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = NexusTextPrimary)
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NexusCyan.copy(alpha = 0.15f))
                                .clickable { viewModel.navigateTo(ScreenDestination.UserDashboard) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "User Mode →", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F172A))
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF0B1120),
                    modifier = Modifier.navigationBarsPadding(),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.AdminDashboard,
                        onClick = { viewModel.navigateTo(ScreenDestination.AdminDashboard) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Overview") },
                        label = { Text("Overview", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NexusRose, selectedTextColor = NexusRose, indicatorColor = NexusRose.copy(alpha = 0.2f))
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.AdminUserOps,
                        onClick = { viewModel.navigateTo(ScreenDestination.AdminUserOps) },
                        icon = { Icon(Icons.Default.Group, contentDescription = "Users") },
                        label = { Text("Users", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NexusRose, selectedTextColor = NexusRose, indicatorColor = NexusRose.copy(alpha = 0.2f))
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.AdminWithdrawalAuth,
                        onClick = { viewModel.navigateTo(ScreenDestination.AdminWithdrawalAuth) },
                        icon = { Icon(Icons.Default.Payment, contentDescription = "Payouts") },
                        label = { Text("Payouts", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NexusRose, selectedTextColor = NexusRose, indicatorColor = NexusRose.copy(alpha = 0.2f))
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.AdminGlobalRates,
                        onClick = { viewModel.navigateTo(ScreenDestination.AdminGlobalRates) },
                        icon = { Icon(Icons.Default.Tune, contentDescription = "Rates") },
                        label = { Text("Rates", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = NexusRose, selectedTextColor = NexusRose, indicatorColor = NexusRose.copy(alpha = 0.2f))
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(NexusBackground)
            ) {
                when (currentDest) {
                    ScreenDestination.AdminDashboard -> AdminDashboardScreen(viewModel)
                    ScreenDestination.AdminUserOps -> AdminUserOpsScreen(viewModel)
                    ScreenDestination.AdminGlobalRates -> AdminGlobalRatesScreen(viewModel)
                    ScreenDestination.AdminWithdrawalAuth -> AdminWithdrawalAuthScreen(viewModel)
                    ScreenDestination.AdminAdManager -> AdminAdManagerScreen(viewModel)
                    ScreenDestination.AdminBroadcasts -> AdminBroadcastsScreen(viewModel)
                    else -> AdminDashboardScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    viewModel: PvcNexusViewModel
) {
    val allUsers by viewModel.allUsers.collectAsState()
    val allWithdrawals by viewModel.allWithdrawals.collectAsState()
    val pendingWithdrawals = allWithdrawals.filter { it.status == "PENDING" }
    val totalPendingAmount = pendingWithdrawals.sumOf { it.netAmount }
    val totalVolume = allUsers.size * 1500.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "ECOSYSTEM COMMAND CENTER", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryKpiCard(
                    title = "Total Turnover",
                    amount = totalVolume,
                    subtitle = "All subscriptions",
                    icon = Icons.Default.AccountBalance,
                    accentColor = NexusCyan,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                SummaryKpiCard(
                    title = "Pending Payouts",
                    amount = totalPendingAmount,
                    subtitle = "${pendingWithdrawals.size} pending requests",
                    icon = Icons.Default.Payment,
                    accentColor = NexusRose,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenDestination.AdminWithdrawalAuth) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "ADMIN MANAGEMENT SHORTCUTS", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            FastActionTile(
                title = "User Controls & Balances",
                subtitle = "Credit/Debit wallet, toggle active/deactivate, reset credentials",
                icon = Icons.Default.Group,
                color = NexusCyan,
                onClick = { viewModel.navigateTo(ScreenDestination.AdminUserOps) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Withdrawal Authorizations (${pendingWithdrawals.size} Pending)",
                subtitle = "Approve payouts with transaction ref or reject with wallet refund",
                icon = Icons.Default.Payment,
                color = NexusRose,
                onClick = { viewModel.navigateTo(ScreenDestination.AdminWithdrawalAuth) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Global System Rates & 10% VAT",
                subtitle = "Manage VAT %, transfer fees, joining cost, generation bonuses",
                icon = Icons.Default.Tune,
                color = NexusPurpleLight,
                onClick = { viewModel.navigateTo(ScreenDestination.AdminGlobalRates) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Sponsored Ad Campaigns (Add Dawa)",
                subtitle = "Create & monitor 10s video ad impressions and reward caps",
                icon = Icons.Default.PlayCircle,
                color = NexusEmerald,
                onClick = { viewModel.navigateTo(ScreenDestination.AdminAdManager) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Promotional Broadcast Announcements",
                subtitle = "Broadcast push alerts and special offer announcements to members",
                icon = Icons.Default.Campaign,
                color = NexusAmber,
                onClick = { viewModel.navigateTo(ScreenDestination.AdminBroadcasts) }
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun AdminUserOpsScreen(
    viewModel: PvcNexusViewModel
) {
    val allUsers by viewModel.allUsers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var userToEdit by remember { mutableStateOf<UserEntity?>(null) }
    var adjustAmountText by remember { mutableStateOf("") }
    var newPasswordText by remember { mutableStateOf("") }

    val filteredUsers = allUsers.filter {
        searchQuery.isBlank() || it.fullName.contains(searchQuery, true) || it.username.contains(searchQuery, true) || it.phone.contains(searchQuery)
    }

    // User Edit Dialog
    if (userToEdit != null) {
        val target = userToEdit!!
        Dialog(onDismissRequest = { userToEdit = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusRose.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "User Control: @${target.username}", color = NexusRose, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { userToEdit = null }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = NexusTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "${target.fullName} • Current Balance: ৳ ${String.format("%,.2f", target.walletBalance)}", color = NexusTextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = adjustAmountText,
                        onValueChange = { adjustAmountText = it },
                        label = { Text("Balance Adjustment (+ or - PVC)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusGradientButton(
                            text = "+ CREDIT",
                            onClick = {
                                val amt = adjustAmountText.toDoubleOrNull() ?: 0.0
                                if (amt > 0) {
                                    viewModel.adminModifyUserBalance(target.username, amt, true)
                                    adjustAmountText = ""
                                    userToEdit = null
                                }
                            },
                            modifier = Modifier.weight(1f),
                            gradient = Brush.horizontalGradient(listOf(NexusEmerald, Color(0xFF059669)))
                        )
                        NexusGradientButton(
                            text = "- DEBIT",
                            onClick = {
                                val amt = adjustAmountText.toDoubleOrNull() ?: 0.0
                                if (amt > 0) {
                                    viewModel.adminModifyUserBalance(target.username, amt, false)
                                    adjustAmountText = ""
                                    userToEdit = null
                                }
                            },
                            modifier = Modifier.weight(1f),
                            gradient = Brush.horizontalGradient(listOf(NexusRose, Color(0xFFBE123C)))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = newPasswordText,
                        onValueChange = { newPasswordText = it },
                        label = { Text("Reset Account Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusPurpleLight, unfocusedBorderColor = NexusCardBorder)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    NexusGradientButton(
                        text = "UPDATE PASSWORD",
                        onClick = {
                            if (newPasswordText.isNotBlank()) {
                                viewModel.adminResetPassword(target.username, newPasswordText)
                                newPasswordText = ""
                                userToEdit = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(listOf(NexusPurple, NexusIndigo))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NexusGradientButton(
                        text = if (target.isActive) "DEACTIVATE ACCOUNT" else "ACTIVATE ACCOUNT",
                        onClick = {
                            viewModel.adminToggleUserStatus(target.username, !target.isActive)
                            userToEdit = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(if (target.isActive) listOf(NexusAmber, Color(0xFFEA580C)) else listOf(NexusEmerald, Color(0xFF059669)))
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.AdminDashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "User Control & Management", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${allUsers.size} Total Registered Members", color = NexusRose, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name, username or phone...", color = NexusTextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NexusRose) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusRose, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(filteredUsers) { u ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusCardBorder,
                onClick = { userToEdit = u }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (u.isActive) NexusCyan.copy(alpha = 0.2f) else NexusRose.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = u.fullName.firstOrNull()?.toString() ?: "U", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = u.fullName, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "@${u.username} • ৳ ${String.format("%,.0f", u.walletBalance)}", color = NexusCyan, fontSize = 11.sp)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusBadge(status = if (u.isActive) "ACTIVE" else "DEACTIVATED")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = NexusTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun AdminGlobalRatesScreen(
    viewModel: PvcNexusViewModel
) {
    val systemConfig by viewModel.systemConfig.collectAsState()

    var withdrawVat by remember(systemConfig) { mutableStateOf(systemConfig.withdrawVatPercent.toString()) }
    var transferFee by remember(systemConfig) { mutableStateOf(systemConfig.transferFeePercent.toString()) }
    var minWithdrawal by remember(systemConfig) { mutableStateOf(systemConfig.minWithdrawalAmount.toString()) }
    var baseJoining by remember(systemConfig) { mutableStateOf(systemConfig.baseJoiningFee.toString()) }
    var directRefer by remember(systemConfig) { mutableStateOf(systemConfig.directReferBonus.toString()) }
    var adReward by remember(systemConfig) { mutableStateOf(systemConfig.adRewardPerUnit.toString()) }
    var dailyAdLimit by remember(systemConfig) { mutableStateOf(systemConfig.dailyAdLimit.toString()) }
    var gen1 by remember(systemConfig) { mutableStateOf(systemConfig.gen1Bonus.toString()) }
    var gen2 by remember(systemConfig) { mutableStateOf(systemConfig.gen2Bonus.toString()) }
    var gen3 by remember(systemConfig) { mutableStateOf(systemConfig.gen3Bonus.toString()) }
    var gen4 by remember(systemConfig) { mutableStateOf(systemConfig.gen4Bonus.toString()) }
    var gen5 by remember(systemConfig) { mutableStateOf(systemConfig.gen5Bonus.toString()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.AdminDashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Global System Rates & VAT", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Instant Real-Time Policy Update Engine", color = NexusRose, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "FEES, LIMITS & TAX PERCENTAGES", color = NexusRose, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    ConfigInputField("Withdrawal VAT Rate (%)", withdrawVat) { withdrawVat = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Member Transfer Fee (%)", transferFee) { transferFee = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Minimum Withdrawal Amount (PVC ৳)", minWithdrawal) { minWithdrawal = it }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "CORE JOINING & AD WORK REWARDS (PVC ৳)", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    ConfigInputField("Base Member Joining Fee (Deducted from Opener)", baseJoining) { baseJoining = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Direct Referral Sponsor Bonus", directRefer) { directRefer = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Daily Ad Watch Reward (per ad)", adReward) { adReward = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Daily Ad Limit (Max ads per day)", dailyAdLimit) { dailyAdLimit = it }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "5-TIER GENERATION BONUSES (PVC ৳)", color = NexusPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    ConfigInputField("Generation 1 Payout", gen1) { gen1 = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Generation 2 Payout", gen2) { gen2 = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Generation 3 Payout", gen3) { gen3 = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Generation 4 Payout", gen4) { gen4 = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    ConfigInputField("Generation 5 Payout", gen5) { gen5 = it }

                    Spacer(modifier = Modifier.height(24.dp))

                    NexusGradientButton(
                        text = "SAVE SYSTEM CONFIGURATION",
                        onClick = {
                            val updated = systemConfig.copy(
                                withdrawVatPercent = withdrawVat.toDoubleOrNull() ?: 10.0,
                                transferFeePercent = transferFee.toDoubleOrNull() ?: 5.0,
                                minWithdrawalAmount = minWithdrawal.toDoubleOrNull() ?: 300.0,
                                baseJoiningFee = baseJoining.toDoubleOrNull() ?: 1500.0,
                                directReferBonus = directRefer.toDoubleOrNull() ?: 300.0,
                                adRewardPerUnit = adReward.toDoubleOrNull() ?: 5.0,
                                dailyAdLimit = dailyAdLimit.toIntOrNull() ?: 2,
                                gen1Bonus = gen1.toDoubleOrNull() ?: 100.0,
                                gen2Bonus = gen2.toDoubleOrNull() ?: 50.0,
                                gen3Bonus = gen3.toDoubleOrNull() ?: 20.0,
                                gen4Bonus = gen4.toDoubleOrNull() ?: 10.0,
                                gen5Bonus = gen5.toDoubleOrNull() ?: 10.0
                            )
                            viewModel.updateSystemConfig(updated)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Save,
                        gradient = Brush.horizontalGradient(listOf(NexusRose, NexusPurple))
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ConfigInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
        singleLine = true
    )
}

@Composable
fun AdminWithdrawalAuthScreen(
    viewModel: PvcNexusViewModel
) {
    val allWithdrawals by viewModel.allWithdrawals.collectAsState()
    var selectedForAction by remember { mutableStateOf<WithdrawalEntity?>(null) }
    var remarkText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("All") }
    var showExportDialog by remember { mutableStateOf(false) }

    val filterTabs = listOf("All", "bKash", "Nagad", "Rocket", "Upay", "Bank")

    val filteredWithdrawals = allWithdrawals.filter { wid ->
        if (selectedTab == "All") true
        else wid.gateway.equals(selectedTab, ignoreCase = true)
    }

    if (showExportDialog) {
        val csvData = buildString {
            append("ID,Username,Gateway,Account,Bank Name,Bank Branch,Gross,VAT,Net,Status,Date\n")
            filteredWithdrawals.forEach { w ->
                append("${w.id},\"${w.username}\",\"${w.gateway}\",\"${w.accountNumber}\",\"${w.bankName}\",\"${w.bankBranch}\",${w.grossAmount},${w.vatAmount},${w.netAmount},\"${w.status}\",\"${java.util.Date(w.requestTimestamp)}\"\n")
            }
        }

        Dialog(onDismissRequest = { showExportDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Excel (CSV) Report Export", color = NexusCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showExportDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = NexusTextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${filteredWithdrawals.size} records prepared for export ($selectedTab category).",
                        color = NexusTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF030712))
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(text = csvData, color = NexusEmerald, fontSize = 10.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NexusGradientButton(
                        text = "DONE",
                        onClick = { showExportDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(listOf(NexusCyan, NexusIndigo))
                    )
                }
            }
        }
    }

    if (selectedForAction != null) {
        val wid = selectedForAction!!
        Dialog(onDismissRequest = { selectedForAction = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusAmber.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Authorize Payout #${wid.id}", color = NexusAmber, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "User: @${wid.username} • ${wid.gateway} (${wid.accountNumber})", color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    if (wid.gateway.equals("Bank", true)) {
                        Text(text = "Bank: ${wid.bankName} | Branch: ${wid.bankBranch}", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    Text(text = "Net Payable Amount: ৳ ${String.format("%,.2f", wid.netAmount)} (Gross: ৳ ${String.format("%,.2f", wid.grossAmount)}, VAT: ৳ ${String.format("%,.2f", wid.vatAmount)})", color = NexusTextSecondary, fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = remarkText,
                        onValueChange = { remarkText = it },
                        label = { Text("Transaction Reference / Remark") },
                        placeholder = { Text("e.g. bKash TrxID 9K38AJ19 / Bank BEFTN") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusGradientButton(
                            text = "APPROVE",
                            onClick = {
                                viewModel.authorizeWithdrawal(wid.id, true, remarkText)
                                selectedForAction = null
                            },
                            modifier = Modifier.weight(1f),
                            gradient = Brush.horizontalGradient(listOf(NexusEmerald, Color(0xFF059669)))
                        )
                        NexusGradientButton(
                            text = "REJECT & REFUND",
                            onClick = {
                                viewModel.authorizeWithdrawal(wid.id, false, remarkText.ifBlank { "Rejected by admin" })
                                selectedForAction = null
                            },
                            modifier = Modifier.weight(1f),
                            gradient = Brush.horizontalGradient(listOf(NexusRose, Color(0xFFBE123C)))
                        )
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.AdminDashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Withdrawal Report Center", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Review Payouts, Gateways, VAT & CSV Export", color = NexusRose, fontSize = 12.sp)
                }
                IconButton(onClick = { showExportDialog = true }) {
                    Icon(Icons.Default.AccountBalance, contentDescription = "Export CSV", tint = NexusCyan)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Tabs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                filterTabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NexusRose.copy(alpha = 0.25f) else Color(0x331E293B))
                            .border(1.dp, if (isSelected) NexusRose else NexusCardBorder, RoundedCornerShape(10.dp))
                            .clickable { selectedTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) NexusRose else NexusTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (filteredWithdrawals.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "No withdrawal requests found for $selectedTab.", color = NexusTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredWithdrawals) { wid ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (wid.status == "PENDING") NexusAmber.copy(alpha = 0.5f) else NexusCardBorder,
                    onClick = if (wid.status == "PENDING") { { selectedForAction = wid; remarkText = "" } } else null
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "@${wid.username} • ${wid.gateway}", color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Acc: ${wid.accountNumber}", color = NexusCyan, fontSize = 11.sp)
                                if (wid.gateway.equals("Bank", true) && wid.bankName.isNotBlank()) {
                                    Text(text = "Bank: ${wid.bankName} (${wid.bankBranch})", color = NexusAmber, fontSize = 10.sp)
                                }
                            }
                            StatusBadge(status = wid.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x440F172A))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Gross: ৳ ${String.format("%,.2f", wid.grossAmount)}", color = NexusTextSecondary, fontSize = 11.sp)
                            Text(text = "VAT: ৳ ${String.format("%,.2f", wid.vatAmount)}", color = NexusRose, fontSize = 11.sp)
                            Text(text = "Net: ৳ ${String.format("%,.2f", wid.netAmount)}", color = NexusEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        if (wid.status == "PENDING") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "👉 Tap to Approve or Reject", color = NexusAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun AdminAdManagerScreen(
    viewModel: PvcNexusViewModel
) {
    val campaigns by viewModel.activeAdCampaigns.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedForEdit by remember { mutableStateOf<AdCampaignEntity?>(null) }

    // Add dialog state
    var title by remember { mutableStateOf("") }
    var sponsorName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var rewardText by remember { mutableStateOf("5.0") }
    var durationSeconds by remember { mutableStateOf(10) }

    // Create New Campaign Dialog
    if (showAddDialog) {
        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusEmerald.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Create Sponsored Ad Campaign", color = NexusEmerald, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showAddDialog = false }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = NexusTextMuted)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Campaign Title") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = sponsorName, onValueChange = { sponsorName = it }, label = { Text("Sponsor Brand Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Ad Duration (Seconds):", color = NexusTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15, 30).forEach { sec ->
                            val isSel = durationSeconds == sec
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NexusEmerald.copy(alpha = 0.25f) else Color(0x331E293B))
                                    .border(1.dp, if (isSel) NexusEmerald else NexusCardBorder, RoundedCornerShape(8.dp))
                                    .clickable { durationSeconds = sec }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "${sec}s", color = if (isSel) NexusEmerald else NexusTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = rewardText,
                        onValueChange = { rewardText = it },
                        label = { Text("Reward Amount (৳ PVC)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    NexusGradientButton(
                        text = "PUBLISH AD CAMPAIGN",
                        onClick = {
                            if (title.isNotBlank() && sponsorName.isNotBlank()) {
                                val rew = rewardText.toDoubleOrNull() ?: 5.0
                                viewModel.createAdCampaign(title, sponsorName, description, rew, durationSeconds)
                                showAddDialog = false
                                title = ""
                                sponsorName = ""
                                description = ""
                                rewardText = "5.0"
                                durationSeconds = 10
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(listOf(NexusEmerald, Color(0xFF059669)))
                    )
                }
            }
        }
    }

    // Edit Existing Campaign Dialog (Manual Duration & Payout Controls)
    if (selectedForEdit != null) {
        val camp = selectedForEdit!!
        var editTitle by remember(camp) { mutableStateOf(camp.title) }
        var editSponsor by remember(camp) { mutableStateOf(camp.sponsorName) }
        var editDuration by remember(camp) { mutableStateOf(camp.durationSeconds) }
        var editReward by remember(camp) { mutableStateOf(camp.rewardAmount) }
        var editActive by remember(camp) { mutableStateOf(camp.isActive) }

        Dialog(onDismissRequest = { selectedForEdit = null }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCyan.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Customize Ad Parameters", color = NexusCyan, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { selectedForEdit = null }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = NexusTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Campaign Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editSponsor,
                        onValueChange = { editSponsor = it },
                        label = { Text("Sponsor Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // AD DURATION MANUAL CONTROLS
                    Text(text = "Ad Duration: ${editDuration} Seconds", color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Choose a preset or adjust timer for user viewing", color = NexusTextMuted, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15, 20, 30).forEach { sec ->
                            val isSel = editDuration == sec
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NexusCyan.copy(alpha = 0.3f) else Color(0x331E293B))
                                    .border(1.dp, if (isSel) NexusCyan else NexusCardBorder, RoundedCornerShape(8.dp))
                                    .clickable { editDuration = sec }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "${sec}s", color = if (isSel) NexusCyan else NexusTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // AD PAYOUT REWARD AMOUNT CONTROLS
                    Text(text = "Ad Reward Payout: ৳ ${String.format("%.2f", editReward)} PVC", color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Adjust reward amount for completing this ad", color = NexusTextMuted, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Decrease reward button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NexusRose.copy(alpha = 0.2f))
                                .border(1.dp, NexusRose.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .clickable { editReward = (editReward - 1.0).coerceAtLeast(1.0) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "- ৳ 1.00", color = NexusRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Preset 5.0 (standard)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (editReward == 5.0) NexusEmerald.copy(alpha = 0.3f) else Color(0x331E293B))
                                .border(1.dp, if (editReward == 5.0) NexusEmerald else NexusCardBorder, RoundedCornerShape(10.dp))
                                .clickable { editReward = 5.0 }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "৳ 5.00 (Std)", color = if (editReward == 5.0) NexusEmerald else NexusTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Increase reward button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NexusEmerald.copy(alpha = 0.2f))
                                .border(1.dp, NexusEmerald.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .clickable { editReward += 1.0 }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "+ ৳ 1.00", color = NexusEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Campaign Active Status", color = NexusTextSecondary, fontSize = 13.sp)
                        Switch(
                            checked = editActive,
                            onCheckedChange = { editActive = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexusEmerald, checkedTrackColor = NexusEmerald.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    NexusGradientButton(
                        text = "SAVE AD PARAMETERS",
                        onClick = {
                            viewModel.updateAdCampaign(
                                camp.copy(
                                    title = editTitle,
                                    sponsorName = editSponsor,
                                    durationSeconds = editDuration,
                                    rewardAmount = editReward,
                                    isActive = editActive
                                )
                            )
                            selectedForEdit = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Save,
                        gradient = Brush.horizontalGradient(listOf(NexusCyan, NexusIndigo))
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.navigateTo(ScreenDestination.AdminDashboard) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(text = "Sponsored Ad Manager", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Manual Duration & Payout Controls", color = NexusEmerald, fontSize = 12.sp)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Randomize ad parameters button
                    IconButton(onClick = { viewModel.randomizeAdParameters() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Randomize Parameters", tint = NexusCyan)
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Campaign", tint = NexusEmerald)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Information / Instruction Card
            GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusEmerald.copy(alpha = 0.35f)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "AD ENGINE PARAMETERS", color = NexusEmerald, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Users receive 2 ads/day for ৳ 10 total (৳ 5 each default). Admins can manually alter duration (e.g. 5s, 10s, 15s) or adjust payout amount anytime.",
                        color = NexusTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusGradientButton(
                            text = "RANDOMIZE PARAMETERS",
                            onClick = { viewModel.randomizeAdParameters() },
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Refresh,
                            gradient = Brush.horizontalGradient(listOf(NexusPurple, NexusIndigo))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "ACTIVE & CONFIGURED AD CAMPAIGNS", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(campaigns) { camp ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (camp.isActive) NexusEmerald.copy(alpha = 0.3f) else NexusCardBorder,
                onClick = { selectedForEdit = camp }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = camp.title, color = NexusTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Sponsor: ${camp.sponsorName}", color = NexusTextSecondary, fontSize = 11.sp)
                        }
                        StatusBadge(status = if (camp.isActive) "ACTIVE" else "PAUSED")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Parameter Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexusCyan.copy(alpha = 0.18f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "⏱ Duration: ${camp.durationSeconds}s", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexusEmerald.copy(alpha = 0.18f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "৳ Payout: ${String.format("%.2f", camp.rewardAmount)} PVC", color = NexusEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x331E293B))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "${camp.totalImpressions} views", color = NexusTextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Controls Row (Instant 5s / 10s / 15s switch & +/- 1 Tk stepper)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5, 10, 15).forEach { sec ->
                                val isCurrent = camp.durationSeconds == sec
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isCurrent) NexusCyan.copy(alpha = 0.35f) else Color(0x331E293B))
                                        .clickable {
                                            viewModel.updateAdCampaign(camp.copy(durationSeconds = sec))
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "${sec}s", color = if (isCurrent) NexusCyan else NexusTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NexusRose.copy(alpha = 0.2f))
                                    .clickable {
                                        val newRew = (camp.rewardAmount - 1.0).coerceAtLeast(1.0)
                                        viewModel.updateAdCampaign(camp.copy(rewardAmount = newRew))
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "-1 Tk", color = NexusRose, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NexusEmerald.copy(alpha = 0.2f))
                                    .clickable {
                                        viewModel.updateAdCampaign(camp.copy(rewardAmount = camp.rewardAmount + 1.0))
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "+1 Tk", color = NexusEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { selectedForEdit = camp }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Parameters", tint = NexusCyan, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun AdminBroadcastsScreen(
    viewModel: PvcNexusViewModel
) {
    val broadcasts by viewModel.allBroadcasts.collectAsState()
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.AdminDashboard) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Promotional Broadcasts", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Announcements & Special Offer Alerts", color = NexusAmber, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Create Broadcast Form
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "NEW BROADCAST ALERT", color = NexusAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Announcement Title") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message Content") }, modifier = Modifier.fillMaxWidth().height(100.dp))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Mark as Urgent / Priority Alert", color = NexusTextSecondary, fontSize = 12.sp)
                        Switch(
                            checked = isUrgent,
                            onCheckedChange = { isUrgent = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexusRose, checkedTrackColor = NexusRose.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NexusGradientButton(
                        text = "PUBLISH BROADCAST",
                        onClick = {
                            if (title.isNotBlank() && message.isNotBlank()) {
                                viewModel.createBroadcast(title, message, isUrgent)
                                title = ""
                                message = ""
                                isUrgent = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Campaign,
                        gradient = Brush.horizontalGradient(listOf(NexusAmber, Color(0xFFEA580C)))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "PREVIOUS BROADCAST HISTORY", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(broadcasts) { b ->
            val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(b.timestamp))

            GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = if (b.isUrgent) NexusRose.copy(alpha = 0.4f) else NexusCardBorder) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = b.title, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        if (b.isUrgent) {
                            StatusBadge(status = "URGENT")
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = b.message, color = NexusTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = dateStr, color = NexusTextMuted, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
