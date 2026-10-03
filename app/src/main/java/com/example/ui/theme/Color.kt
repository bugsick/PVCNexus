package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// PVC NEXUS Neon Fintech Palette
val NexusBackground = Color(0xFF0B0F19)
val NexusSurface = Color(0xFF131B2E)
val NexusSurfaceElevated = Color(0xFF1E293B)
val NexusCardBg = Color(0xE6131B2E)
val NexusCardBorder = Color(0x3338BDF8)
val NexusCardBorderPurple = Color(0x33A855F7)

val NexusCyan = Color(0xFF06B6D4)
val NexusCyanLight = Color(0xFF38BDF8)
val NexusIndigo = Color(0xFF6366F1)
val NexusPurple = Color(0xFF8B5CF6)
val NexusPurpleLight = Color(0xFFA855F7)
val NexusEmerald = Color(0xFF10B981)
val NexusAmber = Color(0xFFF59E0B)
val NexusRose = Color(0xFFF43F5E)

val NexusTextPrimary = Color(0xFFF8FAFC)
val NexusTextSecondary = Color(0xFF94A3B8)
val NexusTextMuted = Color(0xFF64748B)

// Glassmorphism Gradients
val NexusGlowGradient = Brush.horizontalGradient(
    colors = listOf(NexusCyan, NexusIndigo, NexusPurple)
)

val NexusCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x2B1E293B),
        Color(0x1A0F172A)
    )
)

val NexusPrimaryGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF0284C7), Color(0xFF6366F1))
)

val NexusSuccessGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
)

val NexusWarningGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFD97706), Color(0xFFF59E0B))
)

val NexusGoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFEAB308), Color(0xFFFDE047))
)
