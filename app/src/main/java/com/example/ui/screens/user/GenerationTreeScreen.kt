package com.example.ui.screens.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import com.example.data.repository.TreeNode
import com.example.ui.components.GlassCard
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
fun GenerationTreeScreen(
    viewModel: PvcNexusViewModel
) {
    val treeData by viewModel.generationTree.collectAsState()
    var selectedNode by remember { mutableStateOf<TreeNode?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadGenerationTree()
    }

    // Member Details Dialog
    if (selectedNode != null) {
        val node = selectedNode!!
        Dialog(onDismissRequest = { selectedNode = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCyan.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Generation Node Details",
                            color = NexusCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        IconButton(
                            onClick = { selectedNode = null },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = NexusTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(NexusCyan, NexusPurple))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = node.fullName.firstOrNull()?.toString() ?: "U",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = node.fullName,
                        color = NexusTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${node.username}",
                        color = NexusCyan,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RankPill(rank = node.rank)

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = NexusCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    NodeDetailRow("Placement Leg", "Team ${node.leg}", NexusAmber)
                    NodeDetailRow("Selected Package", "Package ${node.packageId} (12 Mo)", NexusPurpleLight)
                    NodeDetailRow("Total Network Size", "${node.teamSize} Members", NexusCyan)
                    NodeDetailRow("Total PVC Earned", "৳ ${String.format("%,.2f", node.totalIncome)}", NexusEmerald)
                    NodeDetailRow("Account Status", if (node.isActive) "Active" else "Deactivated", if (node.isActive) NexusEmerald else NexusRose)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        text = "Generation Chart",
                        color = NexusTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Visual 3-Leg Multilevel Tree View",
                        color = NexusCyan,
                        fontSize = 12.sp
                    )
                }
            }

            IconButton(onClick = { viewModel.loadGenerationTree() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Tree",
                    tint = NexusCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tree Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x66131B2E))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem("Leg A", NexusCyan)
            LegendItem("Leg B", NexusEmerald)
            LegendItem("Leg C", NexusAmber)
            LegendItem("Tap node for details", NexusTextMuted)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tree Render Box
        if (treeData == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NexusCyan)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    TreeBranchItem(
                        node = treeData!!,
                        generationLevel = 0,
                        onNodeClick = { selectedNode = it }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun TreeBranchItem(
    node: TreeNode,
    generationLevel: Int,
    onNodeClick: (TreeNode) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    val indent = (generationLevel * 20).dp

    val legColor = when (node.leg) {
        "A" -> NexusCyan
        "B" -> NexusEmerald
        "C" -> NexusAmber
        else -> NexusPurpleLight
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indent, top = 6.dp, bottom = 6.dp)
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (generationLevel == 0) NexusCyan else legColor.copy(alpha = 0.4f),
            backgroundColor = if (generationLevel == 0) Color(0xE6131B2E) else Color(0xCC0F172A),
            onClick = { onNodeClick(node) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(legColor.copy(alpha = 0.2f))
                            .border(1.dp, legColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (generationLevel == 0) "YOU" else node.leg,
                            color = legColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "@${node.username}",
                                color = NexusTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Gen $generationLevel",
                                color = NexusTextMuted,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "${node.fullName} • ${node.rank}",
                            color = NexusTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NexusEmerald.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "৳ ${String.format("%,.0f", node.totalIncome)}",
                            color = NexusEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (node.children.isNotEmpty()) {
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle Children",
                                tint = NexusTextMuted
                            )
                        }
                    }
                }
            }
        }

        // Children branches
        AnimatedVisibility(
            visible = isExpanded && node.children.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                node.children.forEach { childNode ->
                    TreeBranchItem(
                        node = childNode,
                        generationLevel = generationLevel + 1,
                        onNodeClick = onNodeClick
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun NodeDetailRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = NexusTextSecondary, fontSize = 12.sp)
        Text(text = value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
