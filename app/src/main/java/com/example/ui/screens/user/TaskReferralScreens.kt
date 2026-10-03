package com.example.ui.screens.user

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.example.data.entity.AdCampaignEntity
import com.example.data.entity.ComplaintEntity
import com.example.data.entity.UserEntity
import com.example.data.repository.VacantSlot
import com.example.ui.components.GlassCard
import com.example.ui.components.ManagementAndAchieversView
import com.example.ui.components.NexusGradientButton
import com.example.ui.components.RankPill
import com.example.ui.components.StatusBadge
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
fun DailyWorkScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val adCampaigns by viewModel.activeAdCampaigns.collectAsState()
    val user = currentUser ?: return

    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val isNewDay = user.lastAdWatchDate != todayDate
    val watchedCount = if (isNewDay) 0 else user.adsWatchedToday
    val maxAds = 2 // Package system removed: 2 Ads fixed per PPC ID (৳ 5.00 each, ৳ 10.00 total)
    val progress = (watchedCount.toFloat() / maxAds.toFloat()).coerceIn(0f, 1f)
    val remainingAds = (maxAds - watchedCount).coerceAtLeast(0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Daily Work (Ad Tasks)", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "2 Ads Daily • ৳ 5.00 / Ad (৳ 10.00 Daily Payout)", color = NexusEmerald, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task Tracker Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusEmerald.copy(alpha = 0.45f),
                backgroundColor = Color(0xD9062D24)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "TODAY'S WORK PROGRESS", color = Color(0xFF6EE7B7), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "$watchedCount / $maxAds Ads Completed", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexusEmerald.copy(alpha = 0.25f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "+৳ ${String.format("%.2f", watchedCount * 5.0)} Earned", color = Color(0xFF6EE7B7), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NexusEmerald,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (remainingAds > 0) "$remainingAds task(s) remaining today." else "🎉 All daily tasks completed! Check back tomorrow.",
                        color = if (remainingAds > 0) Color(0xFFD1FAE5) else NexusAmber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PPC Member ID Status Card (Package System Removed)
            GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusCyan.copy(alpha = 0.35f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "MEMBERSHIP TYPE", color = NexusTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "PPC Member ID (Active)", color = NexusCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexusEmerald.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "2 Ads Daily • ৳ 10.00 Total", color = NexusEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "AVAILABLE SPONSORED ADS", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(adCampaigns) { campaign ->
            AdCampaignRowCard(
                campaign = campaign,
                canWatch = remainingAds > 0,
                onWatch = { viewModel.startAdWatch(campaign) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            UserScreenFooterContacts(onOpenContactModal = { viewModel.navigateTo(ScreenDestination.ContactUs) })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun AdCampaignRowCard(
    campaign: AdCampaignEntity,
    canWatch: Boolean,
    onWatch: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (canWatch) NexusCyan.copy(alpha = 0.3f) else NexusCardBorder,
        onClick = if (canWatch) onWatch else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NexusCyan.copy(alpha = 0.15f))
                    .border(1.dp, NexusCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = NexusCyan,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = campaign.title,
                    color = NexusTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Sponsor: ${campaign.sponsorName} • ${campaign.durationSeconds} Seconds",
                    color = NexusTextMuted,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (canWatch) NexusEmerald.copy(alpha = 0.2f) else Color(0x331E293B))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (canWatch) "WATCH ৳ ${String.format("%.0f", campaign.rewardAmount)}" else "DONE",
                    color = if (canWatch) NexusEmerald else NexusTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ReferPvcScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val user = currentUser ?: return
    val context = LocalContext.current

    val myDirects = allUsers.filter { it.sponsorUsername.equals(user.username, ignoreCase = true) }
    val referLink = "https://pvcnexus.app/register?ref=${user.username}"
    var copied by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Refer PVC Network", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Earn ৳ 300 Instant Bonus per Direct Referral", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Direct Referral Summary Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusCyan.copy(alpha = 0.45f),
                backgroundColor = Color(0xD90F1E38)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "DIRECT REFERRAL EARNINGS", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "৳ ${String.format("%,.2f", user.referralIncome)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x660F172A))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Direct Members", color = NexusTextMuted, fontSize = 12.sp)
                        Text(text = "${myDirects.size} Members", color = NexusCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Unique Referral Link Box
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "YOUR UNIQUE SPONSOR CODE & LINK", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x440F172A))
                            .border(1.dp, NexusCardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = referLink,
                            color = NexusCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("PVC Referral Link", referLink)
                                clipboard.setPrimaryClip(clip)
                                copied = true
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Copy Link",
                                tint = if (copied) NexusEmerald else NexusCyan
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "DIRECT REFERRAL MEMBERS DIRECTORY", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (myDirects.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "No direct referrals joined yet. Share your sponsor code to start earning ৳ 300!", color = NexusTextMuted, fontSize = 13.sp, textAlign = TextAlign.Center)
                }
            }
        } else {
            items(myDirects) { member ->
                DirectMemberItem(member)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun DirectMemberItem(member: UserEntity) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(member.joinTimestamp))

    GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusCardBorder) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(NexusCyan, NexusPurple))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = member.fullName.firstOrNull()?.toString() ?: "M", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = member.fullName, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "@${member.username} • Team ${member.placementLeg}", color = NexusCyan, fontSize = 11.sp)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                RankPill(rank = member.currentRank)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Joined $dateStr", color = NexusTextMuted, fontSize = 9.sp)
            }
        }
    }
}

@Composable
fun JoiningFormScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val user = currentUser ?: return

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var nidNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var sponsorUsername by remember { mutableStateOf(user.username) }
    var placementUsername by remember { mutableStateOf(user.username) }
    var selectedLeg by remember { mutableStateOf("A") }

    var showVacantSlots by remember { mutableStateOf(false) }
    var vacantSlots by remember { mutableStateOf<List<VacantSlot>>(emptyList()) }
    var isLoadingSlots by remember { mutableStateOf(false) }

    // Fetch vacant positions when toggle is activated or target placement changes
    fun fetchSlots(targetUser: String) {
        if (targetUser.isBlank()) return
        isLoadingSlots = true
        viewModel.queryVacantSlots(targetUser.trim().lowercase()) { slots ->
            vacantSlots = slots
            isLoadingSlots = false
        }
    }

    val passwordsMatch = password.isNotBlank() && password == confirmPassword
    val isOpenerBalanceSufficient = user.walletBalance >= 1500.0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "New Member Registration", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "PPC Creation Cost: ৳ 1,500 PVC • 5-Gen Engine", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wallet Balance Warning / Status
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isOpenerBalanceSufficient) NexusCyan.copy(alpha = 0.35f) else NexusRose.copy(alpha = 0.6f),
                backgroundColor = if (isOpenerBalanceSufficient) Color(0xD90D1F38) else Color(0xD9330C19)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "YOUR PVC WALLET BALANCE", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "৳ ${String.format("%,.2f", user.walletBalance)}",
                            color = if (isOpenerBalanceSufficient) NexusCyan else NexusRose,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isOpenerBalanceSufficient) NexusEmerald.copy(alpha = 0.2f) else NexusRose.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isOpenerBalanceSufficient) "Deduction: ৳ 1,500" else "Insufficient Balance (< ৳ 1,500)",
                            color = if (isOpenerBalanceSufficient) NexusEmerald else NexusRose,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Registration Inputs Form
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "MEMBER PERSONAL INFORMATION", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name (পুরো নাম)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NexusCyan) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it.trim().lowercase() },
                        label = { Text("Unique Desired Username (ইউজারনেম)") },
                        placeholder = { Text("e.g. kiam46") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Number (মোবাইল নম্বর)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = NexusPurpleLight) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusPurpleLight, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = nidNumber,
                        onValueChange = { nidNumber = it },
                        label = { Text("National ID (NID নম্বর)") },
                        placeholder = { Text("10 or 17 digit NID number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusPurpleLight, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gender Selector (Male / Female)
                    Text(text = "Gender (লিঙ্গ)", color = NexusTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Male", "Female").forEach { g ->
                            val isSelected = gender == g
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) NexusCyan.copy(alpha = 0.25f) else Color(0x331E293B))
                                    .border(1.dp, if (isSelected) NexusCyan else NexusCardBorder, RoundedCornerShape(10.dp))
                                    .clickable { gender = g }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (g == "Male") "Male (পুরুষ)" else "Female (মহিলা)",
                                    color = if (isSelected) NexusCyan else NexusTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address (ইমেইল ঠিকানা)") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NexusPurpleLight) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusPurpleLight, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password (পাসওয়ার্ড)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NexusAmber) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusAmber, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password (কনফার্ম পাসওয়ার্ড)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = if (passwordsMatch) NexusEmerald else NexusRose) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (confirmPassword.isEmpty()) NexusAmber else if (passwordsMatch) NexusEmerald else NexusRose,
                            unfocusedBorderColor = NexusCardBorder,
                            focusedTextColor = NexusTextPrimary,
                            unfocusedTextColor = NexusTextPrimary
                        ),
                        singleLine = true
                    )

                    if (confirmPassword.isNotBlank() && !passwordsMatch) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "⚠️ Passwords do not match!", color = NexusRose, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // SPONSOR & PLACEMENT SECTION
                    Text(text = "SPONSOR & PLACEMENT (রেফার ও প্লেসমেন্ট)", color = NexusAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Sponsor Field
                    OutlinedTextField(
                        value = sponsorUsername,
                        onValueChange = { sponsorUsername = it.trim().lowercase() },
                        label = { Text("Sponsor / Reference ID (স্পন্সর আইডি)") },
                        placeholder = { Text("Username of inviter") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusAmber, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Placement ID Field (প্লেসমেন্ট আইডি)
                    OutlinedTextField(
                        value = placementUsername,
                        onValueChange = {
                            placementUsername = it.trim().lowercase()
                            if (showVacantSlots) {
                                fetchSlots(placementUsername)
                            }
                        },
                        label = { Text("Placement ID Field (প্লেসমেন্ট আইডি)") },
                        placeholder = { Text("alex") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Vacant Slots Toggle Control (খালি জায়গা)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x331E293B))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "খালি জায়গা (Vacant Positions)", color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (showVacantSlots) "দেখাচ্ছে (Showing Available Slots)" else "লুকানো (Hidden)",
                                color = if (showVacantSlots) NexusEmerald else NexusTextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = showVacantSlots,
                            onCheckedChange = { checked ->
                                showVacantSlots = checked
                                if (checked) {
                                    fetchSlots(placementUsername)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NexusEmerald,
                                checkedTrackColor = NexusEmerald.copy(alpha = 0.5f)
                            )
                        )
                    }

                    // Available Position Chips (Grid View)
                    if (showVacantSlots) {
                        Spacer(modifier = Modifier.height(12.dp))

                        if (isLoadingSlots) {
                            Text(text = "Finding vacant slots under @$placementUsername...", color = NexusCyan, fontSize = 12.sp)
                        } else if (vacantSlots.isEmpty()) {
                            Text(text = "No vacant positions available under @$placementUsername.", color = NexusAmber, fontSize = 12.sp)
                        } else {
                            Text(text = "Tap a vacant slot to select Node & Leg:", color = NexusTextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            // 3-Column Grid Array of Clickable Pill-Buttons
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                vacantSlots.chunked(3).forEach { rowSlots ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        rowSlots.forEach { slot ->
                                            val isSelected = placementUsername.equals(slot.nodeId, ignoreCase = true) && selectedLeg.equals(slot.leg, ignoreCase = true)
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(if (isSelected) NexusEmerald.copy(alpha = 0.35f) else Color(0x330F172A))
                                                    .border(1.dp, if (isSelected) NexusEmerald else NexusCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                                    .clickable {
                                                        placementUsername = slot.nodeId
                                                        selectedLeg = slot.leg
                                                    }
                                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "${slot.nodeId} · ${slot.leg}",
                                                    color = if (isSelected) Color.White else NexusEmerald,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        // Fill remaining slots in row if less than 3
                                        repeat(3 - rowSlots.size) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Selected Leg indicator / manual switcher
                    Text(text = "Selected Position / Leg (পজিশন): Team $selectedLeg", color = NexusCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("A", "B", "C").forEach { leg ->
                            val isSelected = selectedLeg == leg
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) NexusCyan.copy(alpha = 0.25f) else Color(0x331E293B))
                                    .border(1.dp, if (isSelected) NexusCyan else NexusCardBorder, RoundedCornerShape(10.dp))
                                    .clickable { selectedLeg = leg }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Leg $leg",
                                    color = if (isSelected) NexusCyan else NexusTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    NexusGradientButton(
                        text = "CREATE PPC ID & DEDUCT ৳ 1,500",
                        onClick = {
                            viewModel.registerNewMember(
                                fullName = fullName,
                                username = username,
                                phone = phone,
                                nidNumber = nidNumber,
                                gender = gender,
                                email = email,
                                password = password,
                                sponsorUsername = sponsorUsername,
                                placementUsername = placementUsername,
                                placementLeg = selectedLeg,
                                packageId = 1
                            ) {
                                viewModel.navigateTo(ScreenDestination.JoiningList)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isOpenerBalanceSufficient && fullName.isNotBlank() && username.isNotBlank() && passwordsMatch && sponsorUsername.isNotBlank()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            UserScreenFooterContacts(onOpenContactModal = { viewModel.navigateTo(ScreenDestination.ContactUs) })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun JoiningListScreen(
    viewModel: PvcNexusViewModel
) {
    val allUsers by viewModel.allUsers.collectAsState()
    var selectedLegFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = allUsers.filter {
        (selectedLegFilter == "ALL" || it.placementLeg == selectedLegFilter) &&
        (searchQuery.isBlank() || it.fullName.contains(searchQuery, true) || it.username.contains(searchQuery, true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Joining List Directory", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "All Activated Downline Members", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name or username...", color = NexusTextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NexusCyan) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexusCyan,
                    unfocusedBorderColor = NexusCardBorder,
                    focusedTextColor = NexusTextPrimary,
                    unfocusedTextColor = NexusTextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Leg Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x66131B2E))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                com.example.ui.screens.user.FilterTabButton("All Legs", selectedLegFilter == "ALL") { selectedLegFilter = "ALL" }
                com.example.ui.screens.user.FilterTabButton("Team A", selectedLegFilter == "A") { selectedLegFilter = "A" }
                com.example.ui.screens.user.FilterTabButton("Team B", selectedLegFilter == "B") { selectedLegFilter = "B" }
                com.example.ui.screens.user.FilterTabButton("Team C", selectedLegFilter == "C") { selectedLegFilter = "C" }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        items(filteredList) { member ->
            DirectMemberItem(member)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun RankGalleryScreen(
    viewModel: PvcNexusViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Rank Gallery & Badges", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "7 Elite Career Ranks & Monthly Salaries", color = Color(0xFFFFD700), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            RankBadgeCard("Tour Winner", "100 - 100 - 100", "Cox's Bazar 5-Star Tour OR ৳ 5,000 Cash Reward", Color(0xFF38BDF8))
            Spacer(modifier = Modifier.height(10.dp))
            RankBadgeCard("SR (Sales Representative)", "300 - 300 - 300", "৳ 5,000 / month salary for 6 months (৳ 30,000 Total)", NexusCyan)
            Spacer(modifier = Modifier.height(10.dp))
            RankBadgeCard("Sales Manager", "600 - 600 - 600", "৳ 5,000 / month salary for 12 months (৳ 60,000 Total)", NexusAmber)
            Spacer(modifier = Modifier.height(10.dp))
            RankBadgeCard("Manager", "1,200 - 1,200 - 1,200", "৳ 10,000 / month salary for 6 months (৳ 60,000 Total)", NexusEmerald)
            Spacer(modifier = Modifier.height(10.dp))
            RankBadgeCard("AGM (Assistant General Manager)", "1,800 - 1,800 - 1,800", "৳ 10,000 / month salary for 12 months (৳ 1,20,000 Total)", NexusCyanLight)
            Spacer(modifier = Modifier.height(10.dp))
            RankBadgeCard("GM (General Manager)", "1 AGM in Leg A • 1 in B • 1 in C", "৳ 1,00,000 / month salary for 12 months (৳ 12,00,000 Total)", NexusPurpleLight)
            Spacer(modifier = Modifier.height(10.dp))
            RankBadgeCard("CEO (Chief Executive Officer)", "1 GM in Leg A • 1 in B • 1 in C", "2% Lifetime Company Profit Share + Luxury Executive Car", Color(0xFFFFD700))

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun RankBadgeCard(
    rankName: String,
    requirement: String,
    rewards: String,
    color: Color
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = color.copy(alpha = 0.4f)
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = rankName, color = NexusTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Official Rank", color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Requirement: $requirement", color = NexusTextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "🎁 Reward: $rewards", color = NexusAmber, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SupportTicketsScreen(
    viewModel: PvcNexusViewModel
) {
    val complaints by viewModel.allComplaints.collectAsState()
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("FINANCIAL") }
    var priority by remember { mutableStateOf("MEDIUM") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Support & Complaint Box", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "24/7 Dedicated Member Ticket Resolution", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ticket Submission Form
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "OPEN NEW SUPPORT TICKET", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Issue Subject") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Detailed Description of Issue") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NexusCyan, unfocusedBorderColor = NexusCardBorder, focusedTextColor = NexusTextPrimary, unfocusedTextColor = NexusTextPrimary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NexusGradientButton(
                        text = "SUBMIT SUPPORT TICKET",
                        onClick = {
                            viewModel.submitComplaint(subject, description, category, priority) {
                                subject = ""
                                description = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Send,
                        enabled = subject.isNotBlank() && description.isNotBlank()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "YOUR TICKET HISTORY", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (complaints.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "No open support tickets.", color = NexusTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(complaints) { ticket ->
                TicketItemCard(ticket)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun TicketItemCard(ticket: ComplaintEntity) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(ticket.timestamp))

    GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusCardBorder) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = ticket.subject, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                StatusBadge(status = ticket.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = ticket.description, color = NexusTextSecondary, fontSize = 12.sp)

            if (ticket.adminReply.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x3310B981))
                        .border(1.dp, NexusEmerald.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(text = "Admin Response:", color = NexusEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = ticket.adminReply, color = Color(0xFFD1FAE5), fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = dateStr, color = NexusTextMuted, fontSize = 10.sp)
        }
    }
}

@Composable
fun AboutUsScreen(
    viewModel: PvcNexusViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "About PVC NEXUS", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Connect • Grow • Succeed", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Management & Weekly Achievers Section
            ManagementAndAchieversView(
                onContactClick = { viewModel.navigateTo(ScreenDestination.ContactUs) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Ecosystem Vision", color = NexusCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "PVC NEXUS is a next-generation decentralized fintech and direct sales community network built to empower entrepreneurs across Bangladesh. Through a transparent 3-leg generation matrix, sponsored advertising ecosystems, and guaranteed rank salaries, members build sustainable lifelong financial independence.",
                        color = NexusTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Security & Compliance", color = NexusPurpleLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• 10% Government VAT withholding on all withdrawals\n• Real-time anti-fraud transaction hashing\n• 5% Platform transfer fee for network stability\n• 24/7 Fast automated settlement support",
                        color = NexusTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            UserScreenFooterContacts(onOpenContactModal = { viewModel.navigateTo(ScreenDestination.ContactUs) })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
