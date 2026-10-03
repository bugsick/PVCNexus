package com.example.ui.screens.user

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdWatcherOverlay
import com.example.ui.components.ContactUsModalSheet
import com.example.ui.components.ContinuousAnnouncementMarquee
import com.example.ui.components.GlassCard
import com.example.ui.components.RankPill
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserMainContainer(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentDest by viewModel.currentDestination.collectAsState()
    val broadcasts by viewModel.allBroadcasts.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isContactSheetOpen by remember { mutableStateOf(false) }

    // Contact Us Universal Sheet (Available on Every User Screen)
    ContactUsModalSheet(
        isOpen = isContactSheetOpen,
        onDismiss = { isContactSheetOpen = false }
    )

    // Ad Watcher state
    val isWatchingAd by viewModel.isAdWatching.collectAsState()
    val activeCampaign by viewModel.activeWatchingCampaign.collectAsState()
    val secondsRemaining by viewModel.adWatchSecondsRemaining.collectAsState()
    val adProgress by viewModel.adWatchProgress.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    if (isWatchingAd && activeCampaign != null) {
        AdWatcherOverlay(
            campaign = activeCampaign!!,
            secondsRemaining = secondsRemaining,
            progress = adProgress,
            onCancel = { viewModel.cancelAdWatch() }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF0F172A),
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

                    // User Profile Drawer Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(NexusCyan, NexusPurple))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (currentUser?.fullName?.firstOrNull() ?: 'U').toString(),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentUser?.fullName ?: "Member",
                                color = NexusTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "@${currentUser?.username ?: "user"}",
                                color = NexusCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    currentUser?.currentRank?.let { rank ->
                        RankPill(rank = rank)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "CORE MODULES",
                        color = NexusTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    DrawerNavRow("Dashboard", Icons.Default.Dashboard, currentDest == ScreenDestination.UserDashboard) {
                        viewModel.navigateTo(ScreenDestination.UserDashboard)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Sales Team", Icons.Default.Groups, currentDest == ScreenDestination.SalesTeam) {
                        viewModel.navigateTo(ScreenDestination.SalesTeam)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Generation Chart", Icons.Default.Hub, currentDest == ScreenDestination.GenerationTree) {
                        viewModel.navigateTo(ScreenDestination.GenerationTree)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Daily Work (Ad Tasks)", Icons.Default.PlayCircle, currentDest == ScreenDestination.DailyWork) {
                        viewModel.navigateTo(ScreenDestination.DailyWork)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Income PVC Breakdown", Icons.Default.MonetizationOn, currentDest == ScreenDestination.IncomePvc) {
                        viewModel.navigateTo(ScreenDestination.IncomePvc)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Joining PVC Ledger", Icons.Default.AccountBalance, currentDest == ScreenDestination.JoiningPvc) {
                        viewModel.navigateTo(ScreenDestination.JoiningPvc)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("PVC Withdraw (10% VAT)", Icons.Default.Payment, currentDest == ScreenDestination.PvcWithdraw) {
                        viewModel.navigateTo(ScreenDestination.PvcWithdraw)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("PVC Transfer (5% Fee)", Icons.Default.SwapHoriz, currentDest == ScreenDestination.PvcTransfer) {
                        viewModel.navigateTo(ScreenDestination.PvcTransfer)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Joining Form (New Member)", Icons.Default.PersonAdd, currentDest == ScreenDestination.JoiningForm) {
                        viewModel.navigateTo(ScreenDestination.JoiningForm)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Joining List Directory", Icons.Default.ListAlt, currentDest == ScreenDestination.JoiningList) {
                        viewModel.navigateTo(ScreenDestination.JoiningList)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Refer PVC (৳ 300 Bonus)", Icons.Default.Share, currentDest == ScreenDestination.ReferPvc) {
                        viewModel.navigateTo(ScreenDestination.ReferPvc)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Rank Gallery & Badges", Icons.Default.EmojiEvents, currentDest == ScreenDestination.RankGallery) {
                        viewModel.navigateTo(ScreenDestination.RankGallery)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Payment Wallet Settings", Icons.Default.Payment, currentDest == ScreenDestination.PaymentSettings) {
                        viewModel.navigateTo(ScreenDestination.PaymentSettings)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Complaint & Support Box", Icons.Default.SupportAgent, currentDest == ScreenDestination.SupportTickets) {
                        viewModel.navigateTo(ScreenDestination.SupportTickets)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Management & About Us", Icons.Default.Help, currentDest == ScreenDestination.AboutUs || currentDest == ScreenDestination.ManagementSection) {
                        viewModel.navigateTo(ScreenDestination.AboutUs)
                        scope.launch { drawerState.close() }
                    }

                    DrawerNavRow("Contact Us (24/7 Helpline)", Icons.Default.HeadsetMic, currentDest == ScreenDestination.ContactUs, accentColor = NexusEmerald) {
                        isContactSheetOpen = true
                        scope.launch { drawerState.close() }
                    }

                    if (currentUser?.role == "ADMIN") {
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = NexusCardBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        DrawerNavRow("Master Admin Command", Icons.Default.AdminPanelSettings, false, accentColor = NexusRose) {
                            viewModel.navigateTo(ScreenDestination.AdminDashboard)
                            scope.launch { drawerState.close() }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DrawerNavRow("Logout Session", Icons.Default.Logout, false, accentColor = NexusRose) {
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
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(listOf(NexusCyan, NexusIndigo))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Hub,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "PVC NEXUS",
                                    color = NexusTextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Connect • Grow • Succeed",
                                    color = NexusCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Drawer",
                                tint = NexusTextPrimary
                            )
                        }
                    },
                    actions = {
                        // Contact Us fast access (Available on every user screen)
                        IconButton(onClick = { isContactSheetOpen = true }) {
                            Icon(
                                imageVector = Icons.Default.HeadsetMic,
                                contentDescription = "Contact Us",
                                tint = NexusCyan
                            )
                        }

                        // Fast balance pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(NexusCyan.copy(alpha = 0.15f))
                                .border(1.dp, NexusCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "৳ ${String.format("%,.0f", currentUser?.walletBalance ?: 0.0)}",
                                color = NexusCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xEE0B0F19)
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF0F172A),
                    modifier = Modifier.navigationBarsPadding(),
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.UserDashboard,
                        onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Home", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NexusCyan,
                            selectedTextColor = NexusCyan,
                            unselectedIconColor = NexusTextMuted,
                            unselectedTextColor = NexusTextMuted,
                            indicatorColor = NexusCyan.copy(alpha = 0.2f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.SalesTeam,
                        onClick = { viewModel.navigateTo(ScreenDestination.SalesTeam) },
                        icon = { Icon(Icons.Default.Groups, contentDescription = "Sales Team") },
                        label = { Text("Team", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NexusPurpleLight,
                            selectedTextColor = NexusPurpleLight,
                            unselectedIconColor = NexusTextMuted,
                            unselectedTextColor = NexusTextMuted,
                            indicatorColor = NexusPurpleLight.copy(alpha = 0.2f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.DailyWork,
                        onClick = { viewModel.navigateTo(ScreenDestination.DailyWork) },
                        icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Daily Work") },
                        label = { Text("Tasks", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NexusEmerald,
                            selectedTextColor = NexusEmerald,
                            unselectedIconColor = NexusTextMuted,
                            unselectedTextColor = NexusTextMuted,
                            indicatorColor = NexusEmerald.copy(alpha = 0.2f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.PvcWithdraw,
                        onClick = { viewModel.navigateTo(ScreenDestination.PvcWithdraw) },
                        icon = { Icon(Icons.Default.Payment, contentDescription = "Withdraw") },
                        label = { Text("Payout", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NexusAmber,
                            selectedTextColor = NexusAmber,
                            unselectedIconColor = NexusTextMuted,
                            unselectedTextColor = NexusTextMuted,
                            indicatorColor = NexusAmber.copy(alpha = 0.2f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentDest == ScreenDestination.GenerationTree,
                        onClick = { viewModel.navigateTo(ScreenDestination.GenerationTree) },
                        icon = { Icon(Icons.Default.Hub, contentDescription = "Tree") },
                        label = { Text("Gen Tree", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NexusCyan,
                            selectedTextColor = NexusCyan,
                            unselectedIconColor = NexusTextMuted,
                            unselectedTextColor = NexusTextMuted,
                            indicatorColor = NexusCyan.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(NexusBackground)
            ) {
                // Continuous scrolling text announcement banner
                ContinuousAnnouncementMarquee(
                    broadcasts = broadcasts,
                    onContactClick = { isContactSheetOpen = true }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (currentDest) {
                        ScreenDestination.UserDashboard -> UserDashboardScreen(viewModel)
                        ScreenDestination.SalesTeam -> SalesTeamScreen(viewModel)
                        ScreenDestination.GenerationTree -> GenerationTreeScreen(viewModel)
                        ScreenDestination.IncomePvc -> IncomePvcScreen(viewModel)
                        ScreenDestination.JoiningPvc -> JoiningPvcScreen(viewModel)
                        ScreenDestination.PaymentSettings -> PaymentSettingsScreen(viewModel)
                        ScreenDestination.PvcWithdraw, ScreenDestination.WithdrawReport -> PvcWithdrawScreen(viewModel)
                        ScreenDestination.PvcTransfer, ScreenDestination.TransferReport -> PvcTransferScreen(viewModel)
                        ScreenDestination.DailyWork -> DailyWorkScreen(viewModel)
                        ScreenDestination.ReferPvc -> ReferPvcScreen(viewModel)
                        ScreenDestination.JoiningForm -> JoiningFormScreen(viewModel)
                        ScreenDestination.JoiningList -> JoiningListScreen(viewModel)
                        ScreenDestination.RankGallery -> RankGalleryScreen(viewModel)
                        ScreenDestination.SupportTickets -> SupportTicketsScreen(viewModel)
                        ScreenDestination.AboutUs, ScreenDestination.ManagementSection -> AboutUsScreen(viewModel)
                        ScreenDestination.ContactUs -> {
                            isContactSheetOpen = true
                            UserDashboardScreen(viewModel)
                        }
                        else -> UserDashboardScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerNavRow(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    accentColor: Color = NexusCyan,
    onClick: () -> Unit
) {
    val bgModifier = if (isSelected) {
        Modifier.background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
    } else Modifier

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(bgModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) accentColor else NexusTextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            color = if (isSelected) NexusTextPrimary else NexusTextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
