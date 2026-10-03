package com.example.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.NexusGradientButton
import com.example.ui.components.RankPill
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

@Composable
fun SalesTeamScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val user = currentUser ?: return
    val totalTeamSize = user.teamACount + user.teamBCount + user.teamCCount

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Screen Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = NexusTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Sales Team Matrix",
                        color = NexusTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "3-Leg Balanced Distribution (A • B • C)",
                        color = NexusCyan,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total Team Summary Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusCyan.copy(alpha = 0.4f),
                backgroundColor = Color(0xD9131B2E)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL DOWNLINE NETWORK",
                                color = NexusTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalTeamSize Active Members",
                                color = NexusTextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        RankPill(rank = user.currentRank)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3-Color Team Ratio Distribution Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFF1E293B))
                    ) {
                        val safeTotal = if (totalTeamSize > 0) totalTeamSize.toFloat() else 3f
                        val weightA = if (totalTeamSize > 0) user.teamACount / safeTotal else 0.333f
                        val weightB = if (totalTeamSize > 0) user.teamBCount / safeTotal else 0.333f
                        val weightC = if (totalTeamSize > 0) user.teamCCount / safeTotal else 0.333f

                        if (weightA > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(weightA)
                                    .fillMaxSize()
                                    .background(NexusCyan)
                            )
                        }
                        if (weightB > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(weightB)
                                    .fillMaxSize()
                                    .background(NexusEmerald)
                            )
                        }
                        if (weightC > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(weightC)
                                    .fillMaxSize()
                                    .background(NexusAmber)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3 Leg Breakdown Cards
            Text(
                text = "TEAM LEG BREAKDOWN",
                color = NexusTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LegDetailCard(
                legTitle = "Team A (Power Leg)",
                memberCount = user.teamACount,
                turnoverPvc = user.teamACount * 1500.0,
                agmsCount = user.teamAGMsA,
                gmsCount = user.teamGMsA,
                accentColor = NexusCyan
            )

            Spacer(modifier = Modifier.height(10.dp))

            LegDetailCard(
                legTitle = "Team B (Growth Leg)",
                memberCount = user.teamBCount,
                turnoverPvc = user.teamBCount * 1500.0,
                agmsCount = user.teamAGMsB,
                gmsCount = user.teamGMsB,
                accentColor = NexusEmerald
            )

            Spacer(modifier = Modifier.height(10.dp))

            LegDetailCard(
                legTitle = "Team C (Balancing Leg)",
                memberCount = user.teamCCount,
                turnoverPvc = user.teamCCount * 1500.0,
                agmsCount = user.teamAGMsC,
                gmsCount = user.teamGMsC,
                accentColor = NexusAmber
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Rank Matrix Qualification Targets
            Text(
                text = "CAREER RANK TARGET MATRIX",
                color = NexusTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            RankTargetCard(
                rankTitle = "Tour Winner / ৳ 5,000 Cash",
                requirementText = "100 in Team A • 100 in Team B • 100 in Team C",
                rewardText = "Cox's Bazar 5-Star Tour OR ৳ 5,000 Cash Reward",
                currentA = user.teamACount,
                currentB = user.teamBCount,
                currentC = user.teamCCount,
                targetEach = 100,
                isAchieved = user.teamACount >= 100 && user.teamBCount >= 100 && user.teamCCount >= 100
            )

            Spacer(modifier = Modifier.height(8.dp))

            RankTargetCard(
                rankTitle = "SR (Sales Representative)",
                requirementText = "300 in Team A • 300 in Team B • 300 in Team C",
                rewardText = "৳ 5,000 Monthly Salary for 6 Months (৳ 30,000 Total)",
                currentA = user.teamACount,
                currentB = user.teamBCount,
                currentC = user.teamCCount,
                targetEach = 300,
                isAchieved = user.teamACount >= 300 && user.teamBCount >= 300 && user.teamCCount >= 300
            )

            Spacer(modifier = Modifier.height(8.dp))

            RankTargetCard(
                rankTitle = "Sales Manager",
                requirementText = "600 in Team A • 600 in Team B • 600 in Team C",
                rewardText = "৳ 5,000 Monthly Salary for 12 Months (৳ 60,000 Total)",
                currentA = user.teamACount,
                currentB = user.teamBCount,
                currentC = user.teamCCount,
                targetEach = 600,
                isAchieved = user.teamACount >= 600 && user.teamBCount >= 600 && user.teamCCount >= 600
            )

            Spacer(modifier = Modifier.height(8.dp))

            RankTargetCard(
                rankTitle = "Manager",
                requirementText = "1,200 in Team A • 1,200 in Team B • 1,200 in Team C",
                rewardText = "৳ 10,000 Monthly Salary for 6 Months (৳ 60,000 Total)",
                currentA = user.teamACount,
                currentB = user.teamBCount,
                currentC = user.teamCCount,
                targetEach = 1200,
                isAchieved = user.teamACount >= 1200 && user.teamBCount >= 1200 && user.teamCCount >= 1200
            )

            Spacer(modifier = Modifier.height(8.dp))

            RankTargetCard(
                rankTitle = "AGM (Assistant General Manager)",
                requirementText = "1,800 in Team A • 1,800 in Team B • 1,800 in Team C",
                rewardText = "৳ 10,000 Monthly Salary for 12 Months (৳ 1,20,000 Total)",
                currentA = user.teamACount,
                currentB = user.teamBCount,
                currentC = user.teamCCount,
                targetEach = 1800,
                isAchieved = user.teamACount >= 1800 && user.teamBCount >= 1800 && user.teamCCount >= 1800
            )

            Spacer(modifier = Modifier.height(8.dp))

            RankTargetCard(
                rankTitle = "GM (General Manager)",
                requirementText = "1 AGM in Team A • 1 AGM in Team B • 1 AGM in Team C",
                rewardText = "৳ 1,00,000 Monthly Salary for 12 Months (৳ 12,00,000 Total)",
                currentA = user.teamAGMsA,
                currentB = user.teamAGMsB,
                currentC = user.teamAGMsC,
                targetEach = 1,
                isAchieved = user.teamAGMsA >= 1 && user.teamAGMsB >= 1 && user.teamAGMsC >= 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            RankTargetCard(
                rankTitle = "CEO (Chief Executive Officer)",
                requirementText = "1 GM in Team A • 1 GM in Team B • 1 GM in Team C",
                rewardText = "2% Company Profit Share + Luxury Car",
                currentA = user.teamGMsA,
                currentB = user.teamGMsB,
                currentC = user.teamGMsC,
                targetEach = 1,
                isAchieved = user.teamGMsA >= 1 && user.teamGMsB >= 1 && user.teamGMsC >= 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action CTA
            Row(modifier = Modifier.fillMaxWidth()) {
                NexusGradientButton(
                    text = "REGISTER NEW DOWNLINE",
                    onClick = { viewModel.navigateTo(ScreenDestination.JoiningForm) },
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.PersonAdd
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun LegDetailCard(
    legTitle: String,
    memberCount: Int,
    turnoverPvc: Double,
    agmsCount: Int,
    gmsCount: Int,
    accentColor: Color
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = accentColor.copy(alpha = 0.35f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = legTitle,
                        color = NexusTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "$memberCount Members",
                    color = accentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x660F172A))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Turnover Volume", color = NexusTextMuted, fontSize = 10.sp)
                    Text(
                        text = "৳ ${String.format("%,.0f", turnoverPvc)}",
                        color = NexusTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "AGMs in Leg", color = NexusTextMuted, fontSize = 10.sp)
                    Text(
                        text = "$agmsCount",
                        color = NexusCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "GMs in Leg", color = NexusTextMuted, fontSize = 10.sp)
                    Text(
                        text = "$gmsCount",
                        color = NexusPurpleLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RankTargetCard(
    rankTitle: String,
    requirementText: String,
    rewardText: String,
    currentA: Int,
    currentB: Int,
    currentC: Int,
    targetEach: Int,
    isAchieved: Boolean
) {
    val minAchieved = minOf(currentA, currentB, currentC)
    val progress = (minAchieved.toFloat() / targetEach.toFloat()).coerceIn(0f, 1f)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (isAchieved) NexusEmerald.copy(alpha = 0.5f) else NexusCardBorder,
        backgroundColor = if (isAchieved) Color(0xCC062D24) else Color(0xCC131B2E)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rankTitle,
                    color = if (isAchieved) NexusEmerald else NexusTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                if (isAchieved) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NexusEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "QUALIFIED",
                            color = NexusEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                } else {
                    Text(
                        text = "${(progress * 100).toInt()}% Done",
                        color = NexusCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = requirementText,
                color = NexusTextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "🎁 $rewardText",
                color = NexusAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isAchieved) NexusEmerald else NexusCyan,
                trackColor = Color(0xFF1E293B)
            )
        }
    }
}
