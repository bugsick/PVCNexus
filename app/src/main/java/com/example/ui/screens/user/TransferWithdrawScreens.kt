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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.WithdrawalEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.NexusGradientButton
import com.example.ui.components.StatusBadge
import com.example.ui.components.UserScreenFooterContacts
import com.example.ui.theme.NexusAmber
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PvcWithdrawScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val withdrawals by viewModel.userWithdrawals.collectAsState()
    val systemConfig by viewModel.systemConfig.collectAsState()
    val user = currentUser ?: return

    val gateways = listOf("bKash", "Nagad", "Rocket", "Upay", "Bank")
    var selectedGateway by remember { mutableStateOf("bKash") }
    var accountNumber by remember(selectedGateway, user) {
        mutableStateOf(
            when (selectedGateway) {
                "bKash" -> user.bKashNumber
                "Nagad" -> user.nagadNumber
                "Rocket" -> user.rocketNumber
                "Upay" -> user.upayNumber
                else -> user.bankAccountNo
            }
        )
    }
    var bankName by remember(user) { mutableStateOf(user.bankName) }
    var bankBranch by remember(user) { mutableStateOf(user.bankBranch) }
    var accountType by remember { mutableStateOf("Personal") }
    var amountText by remember { mutableStateOf("") }

    // Payment Section Search & Filtering State
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf("ALL") }
    var gatewayFilter by remember { mutableStateOf("ALL") }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val vatRate = systemConfig.withdrawVatPercent / 100.0
    val vatAmount = amount * vatRate
    val netAmount = (amount - vatAmount).coerceAtLeast(0.0)
    val minWithdraw = systemConfig.minWithdrawalAmount

    val filteredWithdrawals = withdrawals.filter { wid ->
        val matchesSearch = searchQuery.isBlank() ||
            wid.accountNumber.contains(searchQuery, ignoreCase = true) ||
            wid.gateway.contains(searchQuery, ignoreCase = true) ||
            wid.id.toString().contains(searchQuery, ignoreCase = true) ||
            wid.bankName.contains(searchQuery, ignoreCase = true) ||
            wid.adminRemark.contains(searchQuery, ignoreCase = true)
        val matchesStatus = statusFilter == "ALL" || wid.status.equals(statusFilter, ignoreCase = true)
        val matchesGateway = gatewayFilter == "ALL" || wid.gateway.equals(gatewayFilter, ignoreCase = true)
        matchesSearch && matchesStatus && matchesGateway
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
                    Text(text = "PVC Withdrawal", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Automatic 10% VAT Deduction Applied", color = NexusAmber, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Balance Available Card
            GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusCyan.copy(alpha = 0.35f)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "AVAILABLE FOR WITHDRAWAL", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "৳ ${String.format("%,.2f", user.walletBalance)} PVC", color = NexusCyan, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexusAmber.copy(alpha = 0.18f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Min: ৳ ${String.format("%,.0f", minWithdraw)}", color = NexusAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Withdrawal Form Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "SELECT PAYOUT GATEWAY", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Gateway Selector
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        gateways.forEach { gw ->
                            val isSelected = selectedGateway == gw
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) NexusAmber.copy(alpha = 0.22f) else Color(0x331E293B))
                                    .border(1.dp, if (isSelected) NexusAmber else NexusCardBorder, RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedGateway = gw
                                        accountNumber = when (gw) {
                                            "bKash" -> user.bKashNumber
                                            "Nagad" -> user.nagadNumber
                                            "Rocket" -> user.rocketNumber
                                            "Upay" -> user.upayNumber
                                            else -> user.bankAccountNo
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gw,
                                    color = if (isSelected) NexusAmber else NexusTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (selectedGateway == "Bank") {
                        OutlinedTextField(
                            value = bankName,
                            onValueChange = { bankName = it },
                            label = { Text("Bank Name") },
                            placeholder = { Text("e.g. Islami Bank, City Bank, BRAC Bank") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexusAmber,
                                unfocusedBorderColor = NexusCardBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = bankBranch,
                            onValueChange = { bankBranch = it },
                            label = { Text("Branch Name & District") },
                            placeholder = { Text("e.g. Principal Branch, Dhaka") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexusAmber,
                                unfocusedBorderColor = NexusCardBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = accountNumber,
                            onValueChange = { accountNumber = it },
                            label = { Text("Bank Account Number") },
                            placeholder = { Text("e.g. 2050123456789") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexusAmber,
                                unfocusedBorderColor = NexusCardBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary
                            ),
                            singleLine = true
                        )
                    } else {
                        OutlinedTextField(
                            value = accountNumber,
                            onValueChange = { accountNumber = it },
                            label = { Text("$selectedGateway Mobile Account Number") },
                            placeholder = { Text("01XXXXXXXXX") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NexusAmber,
                                unfocusedBorderColor = NexusCardBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Withdrawal Amount (PVC ৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusAmber,
                            unfocusedBorderColor = NexusCardBorder,
                            focusedTextColor = NexusTextPrimary,
                            unfocusedTextColor = NexusTextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // LIVE VAT CALCULATION BREAKDOWN BOX
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NexusAmber.copy(alpha = 0.35f),
                        backgroundColor = Color(0x660F172A)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Gross Requested:", color = NexusTextSecondary, fontSize = 12.sp)
                                Text(text = "৳ ${String.format("%,.2f", amount)}", color = NexusTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "${String.format("%.0f", systemConfig.withdrawVatPercent)}% Govt VAT Deduction:", color = NexusRose, fontSize = 12.sp)
                                Text(text = "-৳ ${String.format("%,.2f", vatAmount)}", color = NexusRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(color = NexusCardBorder)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Net Payout Receivable:", color = NexusEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "৳ ${String.format("%,.2f", netAmount)}", color = NexusEmerald, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val isBankValid = selectedGateway != "Bank" || (bankName.isNotBlank() && bankBranch.isNotBlank())
                    val isFormValid = amount >= minWithdraw && amount <= user.walletBalance && accountNumber.isNotBlank() && isBankValid

                    NexusGradientButton(
                        text = "SUBMIT WITHDRAWAL REQUEST",
                        onClick = {
                            viewModel.withdrawPvc(
                                gateway = selectedGateway,
                                account = accountNumber,
                                accountType = accountType,
                                amount = amount,
                                bankName = if (selectedGateway == "Bank") bankName else "",
                                bankBranch = if (selectedGateway == "Bank") bankBranch else ""
                            ) {
                                amountText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(listOf(NexusAmber, Color(0xFFEA580C))),
                        enabled = isFormValid
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "WITHDRAWAL STATUS & HISTORY", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                Text(text = "${filteredWithdrawals.size} records", color = NexusCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar for Payments
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by Trx ID, account, gateway, or note...", color = NexusTextMuted, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NexusCyan) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexusCyan,
                    unfocusedBorderColor = NexusCardBorder,
                    focusedTextColor = NexusTextPrimary,
                    unfocusedTextColor = NexusTextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status Filter Chips
            Text(text = "Filter by Status:", color = NexusTextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("ALL", "PENDING", "APPROVED", "REJECTED").forEach { st ->
                    val isSel = statusFilter == st
                    val badgeColor = when (st) {
                        "APPROVED" -> NexusEmerald
                        "PENDING" -> NexusAmber
                        "REJECTED" -> NexusRose
                        else -> NexusCyan
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) badgeColor.copy(alpha = 0.25f) else Color(0x331E293B))
                            .border(1.dp, if (isSel) badgeColor else NexusCardBorder, RoundedCornerShape(8.dp))
                            .clickable { statusFilter = st }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = st,
                            color = if (isSel) badgeColor else NexusTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gateway Filter Chips
            Text(text = "Filter by Payment Gateway:", color = NexusTextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("ALL", "bKash", "Nagad", "Rocket", "Bank").forEach { gw ->
                    val isSel = gatewayFilter == gw
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NexusPurpleLight.copy(alpha = 0.25f) else Color(0x331E293B))
                            .border(1.dp, if (isSel) NexusPurpleLight else NexusCardBorder, RoundedCornerShape(8.dp))
                            .clickable { gatewayFilter = gw }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = gw,
                            color = if (isSel) NexusPurpleLight else NexusTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (filteredWithdrawals.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = if (withdrawals.isEmpty()) "No withdrawal requests submitted yet." else "No withdrawals match your search/filter.", color = NexusTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredWithdrawals) { wid ->
                WithdrawalReportItem(wid)
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
fun PvcTransferScreen(
    viewModel: PvcNexusViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val transactions by viewModel.userTransactions.collectAsState()
    val systemConfig by viewModel.systemConfig.collectAsState()
    val user = currentUser ?: return

    var recipientUsername by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var pinText by remember { mutableStateOf("") }

    // Search and filtering state for transfers
    var transferSearchQuery by remember { mutableStateOf("") }
    var transferTypeFilter by remember { mutableStateOf("ALL") }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val feeRate = systemConfig.transferFeePercent / 100.0
    val fee = amount * feeRate
    val netReceived = (amount - fee).coerceAtLeast(0.0)

    val transferReports = transactions.filter { it.type in listOf("TRANSFER_IN", "TRANSFER_OUT") }
    val filteredTransfers = transferReports.filter { trx ->
        val matchesSearch = transferSearchQuery.isBlank() ||
            trx.counterpartUser.contains(transferSearchQuery, ignoreCase = true) ||
            trx.description.contains(transferSearchQuery, ignoreCase = true) ||
            trx.id.toString().contains(transferSearchQuery, ignoreCase = true) ||
            trx.amount.toString().contains(transferSearchQuery, ignoreCase = true)
        val matchesType = when (transferTypeFilter) {
            "SENT" -> trx.type == "TRANSFER_OUT"
            "RECEIVED" -> trx.type == "TRANSFER_IN"
            else -> true
        }
        matchesSearch && matchesType
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
                    Text(text = "PVC Member Transfer", color = NexusTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Automatic 5% Platform Fee Deduction", color = NexusPurpleLight, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Balance Card
            GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusPurple.copy(alpha = 0.4f)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "CURRENT WALLET BALANCE", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "৳ ${String.format("%,.2f", user.walletBalance)} PVC", color = NexusPurpleLight, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexusPurple.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Fee: 5%", color = NexusPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transfer Form Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    OutlinedTextField(
                        value = recipientUsername,
                        onValueChange = { recipientUsername = it.trim().lowercase() },
                        label = { Text("Recipient Username") },
                        placeholder = { Text("e.g. leader_sam", color = NexusTextMuted) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NexusCyan) },
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

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Transfer Amount (PVC ৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

                    OutlinedTextField(
                        value = pinText,
                        onValueChange = { pinText = it },
                        label = { Text("Security PIN (Default: 1234)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NexusPurpleLight) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexusPurpleLight,
                            unfocusedBorderColor = NexusCardBorder,
                            focusedTextColor = NexusTextPrimary,
                            unfocusedTextColor = NexusTextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // LIVE 5% PLATFORM FEE CALCULATION
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NexusPurpleLight.copy(alpha = 0.35f),
                        backgroundColor = Color(0x660F172A)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Transfer Amount:", color = NexusTextSecondary, fontSize = 12.sp)
                                Text(text = "৳ ${String.format("%,.2f", amount)}", color = NexusTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "5% Platform Fee:", color = NexusRose, fontSize = 12.sp)
                                Text(text = "-৳ ${String.format("%,.2f", fee)}", color = NexusRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(color = NexusCardBorder)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Net Credited to Recipient:", color = NexusEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "৳ ${String.format("%,.2f", netReceived)}", color = NexusEmerald, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    NexusGradientButton(
                        text = "CONFIRM PVC TRANSFER",
                        onClick = {
                            viewModel.transferPvc(recipientUsername, amount, pinText) {
                                recipientUsername = ""
                                amountText = ""
                                pinText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        gradient = Brush.horizontalGradient(listOf(NexusPurple, NexusIndigo)),
                        enabled = amount > 0 && amount <= user.walletBalance && recipientUsername.isNotBlank() && pinText.isNotBlank()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "TRANSFER ACTIVITY REPORT", color = NexusTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                Text(text = "${filteredTransfers.size} records", color = NexusPurpleLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar for Transfers
            OutlinedTextField(
                value = transferSearchQuery,
                onValueChange = { transferSearchQuery = it },
                placeholder = { Text("Search by recipient, description, or amount...", color = NexusTextMuted, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NexusPurpleLight) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexusPurpleLight,
                    unfocusedBorderColor = NexusCardBorder,
                    focusedTextColor = NexusTextPrimary,
                    unfocusedTextColor = NexusTextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Type Filter Chips (ALL, SENT, RECEIVED)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Pair("ALL", "All Transfers"),
                    Pair("SENT", "Sent (Out)"),
                    Pair("RECEIVED", "Received (In)")
                ).forEach { (typeKey, label) ->
                    val isSel = transferTypeFilter == typeKey
                    val color = when (typeKey) {
                        "SENT" -> NexusRose
                        "RECEIVED" -> NexusEmerald
                        else -> NexusPurpleLight
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) color.copy(alpha = 0.25f) else Color(0x331E293B))
                            .border(1.dp, if (isSel) color else NexusCardBorder, RoundedCornerShape(8.dp))
                            .clickable { transferTypeFilter = typeKey }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSel) color else NexusTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (filteredTransfers.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = if (transferReports.isEmpty()) "No PVC transfer history found." else "No transfers match your search/filter.", color = NexusTextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredTransfers) { trx ->
                com.example.ui.screens.user.TransactionRowItem(trx)
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
fun WithdrawalReportItem(wid: WithdrawalEntity) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(wid.requestTimestamp))

    GlassCard(modifier = Modifier.fillMaxWidth(), borderColor = NexusCardBorder) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexusAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Payment, contentDescription = null, tint = NexusAmber, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        val displayTitle = if (wid.gateway == "Bank" && wid.bankName.isNotBlank()) {
                            "Bank: ${wid.bankName} - ${wid.accountNumber}"
                        } else {
                            "${wid.gateway} (${wid.accountNumber})"
                        }
                        Text(text = displayTitle, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        if (wid.gateway == "Bank" && wid.bankBranch.isNotBlank()) {
                            Text(text = "Branch: ${wid.bankBranch}", color = NexusAmber, fontSize = 10.sp)
                        }
                        Text(text = dateStr, color = NexusTextMuted, fontSize = 10.sp)
                    }
                }

                StatusBadge(status = wid.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

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

            if (wid.adminRemark.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Remark: ${wid.adminRemark}",
                    color = NexusCyan,
                    fontSize = 11.sp
                )
            }
        }
    }
}
