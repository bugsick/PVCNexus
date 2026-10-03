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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FastActionTile
import com.example.ui.components.GlassCard
import com.example.ui.components.NexusGradientButton
import com.example.ui.components.RankPill
import com.example.ui.components.SummaryKpiCard
import com.example.ui.components.UserScreenFooterContacts
import com.example.ui.theme.NexusAmber
import com.example.ui.theme.NexusCardBorder
import com.example.ui.theme.NexusCyan
import com.example.ui.theme.NexusCyanLight
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserDashboardScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val broadcasts by viewModel.allBroadcasts.collectAsState()
    val user = currentUser ?: return

    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val isNewDay = user.lastAdWatchDate != todayDate
    val watchedCount = if (isNewDay) 0 else user.adsWatchedToday
    val maxAds = 2 // Package system removed: 2 Ads fixed per PPC ID (৳ 5 each, ৳ 10 total daily)
    val dailyEarningPotential = 10.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // User Welcome & Rank Profile Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusCyan.copy(alpha = 0.4f),
                backgroundColor = Color(0xD9131B2E)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(NexusCyan, NexusIndigo))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.fullName.firstOrNull()?.toString() ?: "U",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = user.fullName,
                                    color = NexusTextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "@${user.username}",
                                        color = NexusCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(NexusCyan.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "PPC ID",
                                            color = NexusCyan,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }

                        RankPill(rank = user.currentRank)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x660F172A))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Direct Sponsor",
                                color = NexusTextMuted,
                                fontSize = 10.sp
                            )
                            Text(
                                text = if (user.sponsorUsername.isNotEmpty()) "@${user.sponsorUsername}" else "None (Root)",
                                color = NexusTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Placement Leg",
                                color = NexusTextMuted,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "Team ${user.placementLeg}",
                                color = NexusAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 SUMMARY KPI CARDS (2x2 Grid)
            Text(
                text = "FINANCIAL SUMMARY",
                color = NexusTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryKpiCard(
                    title = "Total Income",
                    amount = user.totalIncome,
                    subtitle = "All streams combined",
                    icon = Icons.Default.TrendingUp,
                    accentColor = NexusEmerald,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenDestination.IncomePvc) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                SummaryKpiCard(
                    title = "Total Joining",
                    amount = user.totalJoiningIncome,
                    subtitle = "Registration turnover",
                    icon = Icons.Default.AccountBalance,
                    accentColor = NexusPurpleLight,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenDestination.JoiningPvc) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                SummaryKpiCard(
                    title = "Total Withdraw",
                    amount = user.totalWithdraw,
                    subtitle = "Payouts processed",
                    icon = Icons.Default.Payment,
                    accentColor = NexusRose,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenDestination.WithdrawReport) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                SummaryKpiCard(
                    title = "Total Balance",
                    amount = user.walletBalance,
                    subtitle = "Available for payout",
                    icon = Icons.Default.AccountBalanceWallet,
                    accentColor = NexusCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(ScreenDestination.PvcTransfer) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3-Leg Sales Team Card Preview
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusPurple.copy(alpha = 0.35f),
                onClick = { viewModel.navigateTo(ScreenDestination.SalesTeam) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                tint = NexusPurpleLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sales Team Distribution",
                                color = NexusTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "View All →",
                            color = NexusCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TeamLegPill("Team A", user.teamACount, NexusCyan)
                        TeamLegPill("Team B", user.teamBCount, NexusEmerald)
                        TeamLegPill("Team C", user.teamCCount, NexusAmber)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Next Rank Target Indicator
                    val (nextRankName, targetEach) = when (user.currentRank) {
                        "Member" -> Pair("Tour Winner / ৳ 5,000 Cash", 100)
                        "Tour Winner" -> Pair("SR (৳ 5,000 / mo Salary)", 300)
                        "SR" -> Pair("Sales Manager (৳ 5,000 / mo)", 600)
                        "Sales Manager" -> Pair("Manager (৳ 10,000 / mo)", 1200)
                        "Manager" -> Pair("AGM (৳ 10,000 / mo)", 1800)
                        "AGM" -> Pair("GM (৳ 1,00,000 / mo)", 1)
                        else -> Pair("CEO (2% Profit Share)", 1)
                    }

                    val minLeg = minOf(user.teamACount, user.teamBCount, user.teamCCount)
                    val progress = if (targetEach > 0) (minLeg.toFloat() / targetEach.toFloat()).coerceIn(0f, 1f) else 1f

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Next Target: $nextRankName",
                            color = NexusTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$minLeg / $targetEach each",
                            color = NexusCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = NexusCyan,
                        trackColor = Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Daily Work / Task Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusEmerald.copy(alpha = 0.4f),
                backgroundColor = Color(0xD9062D24),
                onClick = { viewModel.navigateTo(ScreenDestination.DailyWork) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(NexusEmerald.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = NexusEmerald,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Daily Work PVC Tasks",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$watchedCount / $maxAds Ads Completed Today (৳ ${String.format("%.0f", watchedCount * 5.0)} / ৳ ${String.format("%.0f", dailyEarningPotential)})",
                            color = Color(0xFF6EE7B7),
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = NexusEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Announcements / Broadcast Feed
            if (broadcasts.isNotEmpty()) {
                Text(
                    text = "ANNOUNCEMENTS & PROMOTIONS",
                    color = NexusTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                val topBroadcast = broadcasts.first()
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (topBroadcast.isUrgent) NexusRose.copy(alpha = 0.5f) else NexusCyan.copy(alpha = 0.3f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = if (topBroadcast.isUrgent) NexusRose else NexusCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = topBroadcast.title,
                                    color = NexusTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                if (topBroadcast.isUrgent) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(NexusRose.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "URGENT",
                                            color = NexusRose,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = topBroadcast.message,
                                color = NexusTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // FAST ACTION GRID (8 Core Actions)
            Text(
                text = "FAST OPERATIONS",
                color = NexusTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            FastActionTile(
                title = "Work PVC (Daily Ad Tasks)",
                subtitle = "Complete 10s ads and earn ৳ 5 per ad",
                icon = Icons.Default.PlayCircle,
                color = NexusEmerald,
                onClick = { viewModel.navigateTo(ScreenDestination.DailyWork) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Joining Form (New Member)",
                subtitle = "Register member with ৳ 1,500 & disburse 5-Gen bonuses",
                icon = Icons.Default.PersonAdd,
                color = NexusCyan,
                onClick = { viewModel.navigateTo(ScreenDestination.JoiningForm) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "PVC Withdrawal (10% VAT)",
                subtitle = "Payout via bKash, Nagad, Rocket, or Bank",
                icon = Icons.Default.Payment,
                color = NexusAmber,
                onClick = { viewModel.navigateTo(ScreenDestination.PvcWithdraw) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "PVC Member Transfer (5% Fee)",
                subtitle = "Instant peer-to-peer PVC wallet transfer",
                icon = Icons.Default.SwapHoriz,
                color = NexusPurpleLight,
                onClick = { viewModel.navigateTo(ScreenDestination.PvcTransfer) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Refer PVC (৳ 300 Bonus)",
                subtitle = "Share unique link & track direct team",
                icon = Icons.Default.Share,
                color = NexusCyanLight,
                onClick = { viewModel.navigateTo(ScreenDestination.ReferPvc) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Generation Chart & Tree",
                subtitle = "Multi-tier visual multilevel downline matrix",
                icon = Icons.Default.Hub,
                color = NexusIndigo,
                onClick = { viewModel.navigateTo(ScreenDestination.GenerationTree) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Rank Gallery & Badges",
                subtitle = "View requirements for Tour, SR, SM, Mgr, AGM, GM, CEO",
                icon = Icons.Default.EmojiEvents,
                color = Color(0xFFFFD700),
                onClick = { viewModel.navigateTo(ScreenDestination.RankGallery) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            FastActionTile(
                title = "Complaint & Support Box",
                subtitle = "Submit tickets and get prompt help from admin",
                icon = Icons.Default.SupportAgent,
                color = NexusRose,
                onClick = { viewModel.navigateTo(ScreenDestination.SupportTickets) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            UserScreenFooterContacts(onOpenContactModal = { viewModel.navigateTo(ScreenDestination.ContactUs) })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun TeamLegPill(
    label: String,
    count: Int,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$count Members",
                color = NexusTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
