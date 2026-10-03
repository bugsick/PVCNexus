package com.example.ui.screens.user

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.TransactionEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.NexusGradientButton
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
fun IncomePvcScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val transactions by viewModel.userTransactions.collectAsState()
    val user = currentUser ?: return
    var selectedTab by remember { mutableStateOf("ALL") }

    val filteredTransactions = transactions.filter {
        when (selectedTab) {
            "REFER" -> it.type == "REFER_BONUS"
            "GEN" -> it.type == "GEN_BONUS"
            "WORK" -> it.type == "DAILY_WORK"
            "SALARY" -> it.type == "SALARY" || it.type == "INCENTIVE"
            else -> it.type in listOf("REFER_BONUS", "GEN_BONUS", "DAILY_WORK", "SALARY", "INCENTIVE")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Screen Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.navigateTo(ScreenDestination.UserDashboard) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = NexusTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = "Income PVC Streams", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Detailed Breakdown of All PVC Earnings", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total Income Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusEmerald.copy(alpha = 0.45f),
                backgroundColor = Color(0xD9062D24)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "TOTAL ACCUMULATED PVC INCOME", color = Color(0xFF6EE7B7), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "৳ ${String.format("%,.2f", user.totalIncome)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5 Stream Breakdown Tiles
            IncomeStreamTile("Direct Referral Bonus (৳ 300 / member)", user.referralIncome, Icons.Default.Share, NexusCyan)
            Spacer(modifier = Modifier.height(8.dp))
            IncomeStreamTile("5-Tier Generation Payouts (Gen 1-5)", user.generationIncome, Icons.Default.MonetizationOn, NexusPurpleLight)
            Spacer(modifier = Modifier.height(8.dp))
            IncomeStreamTile("Daily Work (Ad Tasks @ ৳ 5 / ad)", user.dailyWorkIncome, Icons.Default.Work, NexusEmerald)
            Spacer(modifier = Modifier.height(8.dp))
            IncomeStreamTile("Monthly Rank Salaries (SR to GM)", user.salaryIncome, Icons.Default.Stars, NexusAmber)
            Spacer(modifier = Modifier.height(8.dp))
            IncomeStreamTile("Special Incentives & Profit Shares", user.incentiveIncome, Icons.Default.AttachMoney, Color(0xFFFFD700))

            Spacer(modifier = Modifier.height(20.dp))

            // Tab Filter for Transactions
            Text(text = "INCOME HISTORY LOG", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x66131B2E))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilterTabButton("All", selectedTab == "ALL") { selectedTab = "ALL" }
                FilterTabButton("Refer", selectedTab == "REFER") { selectedTab = "REFER" }
                FilterTabButton("Generation", selectedTab == "GEN") { selectedTab = "GEN" }
                FilterTabButton("Ad Work", selectedTab == "WORK") { selectedTab = "WORK" }
                FilterTabButton("Salary", selectedTab == "SALARY") { selectedTab = "SALARY" }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No income records found for this category.", color = NexusTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredTransactions) { trx ->
                TransactionRowItem(trx)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            UserScreenFooterContacts(onOpenContactModal = { viewModel.navigateTo(ScreenDestination.ContactUs) })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun JoiningPvcScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val transactions by viewModel.userTransactions.collectAsState()
    val user = currentUser ?: return

    val joiningTransactions = transactions.filter {
        it.type in listOf("JOINING_FEE", "ADMIN_CREDIT", "ADMIN_DEBIT", "TRANSFER_IN", "TRANSFER_OUT")
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
                    Text(text = "Joining PVC Ledger", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Registration Wallet Turnover & Transfers", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wallet Summary Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NexusPurple.copy(alpha = 0.45f),
                backgroundColor = Color(0xD91E1338)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "AVAILABLE PVC WALLET BALANCE", color = NexusPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "৳ ${String.format("%,.2f", user.walletBalance)}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x660F172A))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Joining Disbursed", color = NexusTextMuted, fontSize = 10.sp)
                            Text(text = "৳ ${String.format("%,.2f", user.totalJoiningIncome)}", color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Joining Cost / Member", color = NexusTextMuted, fontSize = 10.sp)
                            Text(text = "৳ 1,500 PVC", color = NexusCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fast Actions
            Row(modifier = Modifier.fillMaxWidth()) {
                NexusGradientButton(
                    text = "REGISTER MEMBER",
                    onClick = { viewModel.navigateTo(ScreenDestination.JoiningForm) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                NexusGradientButton(
                    text = "TRANSFER PVC",
                    onClick = { viewModel.navigateTo(ScreenDestination.PvcTransfer) },
                    modifier = Modifier.weight(1f),
                    gradient = Brush.horizontalGradient(listOf(NexusPurple, NexusIndigo))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "LEDGER ACTIVITY STATEMENT", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (joiningTransactions.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "No wallet transfer/joining records yet.", color = NexusTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(joiningTransactions) { trx ->
                TransactionRowItem(trx)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            UserScreenFooterContacts(onOpenContactModal = { viewModel.navigateTo(ScreenDestination.ContactUs) })
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun PaymentSettingsScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val user = currentUser ?: return

    var bKash by remember(user) { mutableStateOf(user.bKashNumber) }
    var nagad by remember(user) { mutableStateOf(user.nagadNumber) }
    var rocket by remember(user) { mutableStateOf(user.rocketNumber) }
    var upay by remember(user) { mutableStateOf(user.upayNumber) }
    var bankName by remember(user) { mutableStateOf(user.bankName) }
    var bankAcc by remember(user) { mutableStateOf(user.bankAccountNo) }
    var bankBranch by remember(user) { mutableStateOf(user.bankBranch) }

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
                    Text(text = "Payment Configurations", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Configure Wallets & Bank Details for Instant Withdrawals", color = NexusCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "MOBILE FINANCIAL WALLETS", color = NexusCyan, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(14.dp))

                    PaymentTextField(value = bKash, onValueChange = { bKash = it }, label = "bKash Account Number (11 digits)", placeholder = "01XXXXXXXXX")
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentTextField(value = nagad, onValueChange = { nagad = it }, label = "Nagad Account Number", placeholder = "01XXXXXXXXX")
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentTextField(value = rocket, onValueChange = { rocket = it }, label = "Rocket Account Number (12 digits)", placeholder = "01XXXXXXXXXX")
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentTextField(value = upay, onValueChange = { upay = it }, label = "Upay Account Number", placeholder = "01XXXXXXXXX")

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "BANK ACCOUNT DETAILS", color = NexusPurpleLight, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(14.dp))

                    PaymentTextField(value = bankName, onValueChange = { bankName = it }, label = "Bank Name", placeholder = "e.g. Islami Bank / City Bank / BRAC Bank")
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentTextField(value = bankAcc, onValueChange = { bankAcc = it }, label = "Bank Account Number", placeholder = "e.g. 2050123456789")
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentTextField(value = bankBranch, onValueChange = { bankBranch = it }, label = "Branch Name & District", placeholder = "e.g. Dhanmondi Branch, Dhaka")

                    Spacer(modifier = Modifier.height(24.dp))

                    NexusGradientButton(
                        text = "SAVE PAYMENT SETTINGS",
                        onClick = { viewModel.savePaymentSettings(bKash, nagad, rocket, upay, bankName, bankAcc, bankBranch) },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Save
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
fun PaymentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = NexusTextMuted) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NexusCyan,
            unfocusedBorderColor = NexusCardBorder,
            focusedTextColor = NexusTextPrimary,
            unfocusedTextColor = NexusTextPrimary,
            focusedLabelColor = NexusCyan,
            unfocusedLabelColor = NexusTextSecondary
        ),
        singleLine = true
    )
}

@Composable
fun IncomeStreamTile(
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = color.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "Guaranteed Instant Settlement", color = NexusTextMuted, fontSize = 10.sp)
            }

            Text(
                text = "৳ ${String.format("%,.2f", amount)}",
                color = color,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun FilterTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) NexusCyan.copy(alpha = 0.25f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) NexusCyan else NexusTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun TransactionRowItem(trx: TransactionEntity) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(trx.timestamp))
    val isCredit = trx.type in listOf("REFER_BONUS", "GEN_BONUS", "DAILY_WORK", "SALARY", "INCENTIVE", "TRANSFER_IN", "ADMIN_CREDIT", "WITHDRAWAL_REFUND")

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (isCredit) NexusEmerald.copy(alpha = 0.25f) else NexusRose.copy(alpha = 0.25f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (isCredit) NexusEmerald.copy(alpha = 0.18f) else NexusRose.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCredit) Icons.Default.CallReceived else Icons.Default.CallMade,
                    contentDescription = null,
                    tint = if (isCredit) NexusEmerald else NexusRose,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = trx.description, color = NexusTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                Text(text = dateStr, color = NexusTextMuted, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isCredit) "+" else "-"}৳ ${String.format("%,.2f", trx.amount)}",
                    color = if (isCredit) NexusEmerald else NexusRose,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
                if (trx.fee > 0) {
                    Text(text = "Fee: ৳ ${String.format("%.2f", trx.fee)}", color = NexusTextMuted, fontSize = 9.sp)
                }
            }
        }
    }
}
