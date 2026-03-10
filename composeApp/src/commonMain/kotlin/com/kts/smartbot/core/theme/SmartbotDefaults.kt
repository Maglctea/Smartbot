package com.kts.smartbot.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

object SmartbotDefaults {
    val pillShape = RoundedCornerShape(999.dp)
    val cardShape = RoundedCornerShape(28.dp)
    val secondaryCardShape = RoundedCornerShape(24.dp)
    val fieldShape = RoundedCornerShape(18.dp)
    val chipShape = RoundedCornerShape(20.dp)
    val bottomBarShape = RoundedCornerShape(28.dp)

    val cardPadding = 24.dp
    val compactCardPadding = 20.dp
    val screenHorizontalPadding = 20.dp
    val screenVerticalPadding = 24.dp
    val contentHorizontalPadding = 24.dp
    val contentVerticalPadding = 20.dp

    val cardElevation = 6.dp
    val secondaryCardElevation = 5.dp
    val overlayCardElevation = 10.dp
}

@Composable
fun smartbotScreenBackgroundBrush(
    accentAlpha: Float = 0.14f,
): Brush {
    val colors = MaterialTheme.colorScheme
    return Brush.verticalGradient(
        colors = listOf(
            colors.primary.copy(alpha = accentAlpha),
            colors.background,
            colors.background,
        ),
    )
}
