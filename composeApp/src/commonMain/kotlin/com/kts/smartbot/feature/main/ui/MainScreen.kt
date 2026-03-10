package com.kts.smartbot.feature.main.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.theme.SmartbotDefaults
import com.kts.smartbot.core.theme.smartbotScreenBackgroundBrush
import com.kts.smartbot.feature.conversations.ui.ConversationsRoute
import com.kts.smartbot.feature.home.ui.HomeScreen
import com.kts.smartbot.feature.profile.ui.ProfileScreen
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.home_tab_conversations
import smartbot.composeapp.generated.resources.home_tab_home
import smartbot.composeapp.generated.resources.home_tab_profile

@Composable
fun MainScreen(
    onOpenOnboarding: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Home.name) }
    val currentTab = MainTab.valueOf(selectedTab)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            MainBottomBar(
                selectedTab = currentTab,
                onTabSelected = { tab -> selectedTab = tab.name },
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = smartbotScreenBackgroundBrush(accentAlpha = 0.12f))
                .safeDrawingPadding()
                .padding(contentPadding)
                .padding(
                    horizontal = SmartbotDefaults.contentHorizontalPadding,
                    vertical = SmartbotDefaults.contentVerticalPadding,
                ),
        ) {
            when (currentTab) {
                MainTab.Home -> HomeScreen(onOpenOnboarding = onOpenOnboarding)
                MainTab.Conversations -> ConversationsRoute()
                MainTab.Profile -> ProfileScreen(onLogoutClicked = onLogoutClicked)
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = SmartbotDefaults.bottomBarShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = SmartbotDefaults.overlayCardElevation,
        shadowElevation = SmartbotDefaults.overlayCardElevation,
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
        ) {
            MainTab.entries.forEach { tab ->
                NavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    icon = { MainTabIcon(tab = tab, selected = selectedTab == tab) },
                    label = { Text(tab.label()) },
                )
            }
        }
    }
}

@Composable
private fun MainTabIcon(
    tab: MainTab,
    selected: Boolean,
) {
    val colors = MaterialTheme.colorScheme
    val tint = if (selected) colors.primary else colors.onSurfaceVariant

    when (tab) {
        MainTab.Home -> MainHomeIcon(tint = tint)
        MainTab.Conversations -> MainConversationIcon(tint = tint)
        MainTab.Profile -> MainProfileIcon(tint = tint)
    }
}

@Composable
private fun MainHomeIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = size.minDimension * 0.1f)
        val roof = Path().apply {
            moveTo(size.width * 0.18f, size.height * 0.5f)
            lineTo(size.width * 0.5f, size.height * 0.2f)
            lineTo(size.width * 0.82f, size.height * 0.5f)
        }

        drawPath(path = roof, color = tint, style = stroke)
        drawRoundRect(
            color = tint,
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.26f, size.height * 0.48f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.48f, size.height * 0.32f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.minDimension * 0.08f),
            style = stroke,
        )
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.8f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.58f),
            strokeWidth = size.minDimension * 0.1f,
        )
    }
}

@Composable
private fun MainConversationIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = size.minDimension * 0.1f)
        val bubble = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.28f)
            lineTo(size.width * 0.8f, size.height * 0.28f)
            quadraticTo(size.width * 0.9f, size.height * 0.28f, size.width * 0.9f, size.height * 0.4f)
            lineTo(size.width * 0.9f, size.height * 0.62f)
            quadraticTo(size.width * 0.9f, size.height * 0.74f, size.width * 0.78f, size.height * 0.74f)
            lineTo(size.width * 0.48f, size.height * 0.74f)
            lineTo(size.width * 0.34f, size.height * 0.86f)
            lineTo(size.width * 0.36f, size.height * 0.74f)
            lineTo(size.width * 0.22f, size.height * 0.74f)
            quadraticTo(size.width * 0.1f, size.height * 0.74f, size.width * 0.1f, size.height * 0.62f)
            lineTo(size.width * 0.1f, size.height * 0.4f)
            quadraticTo(size.width * 0.1f, size.height * 0.28f, size.width * 0.2f, size.height * 0.28f)
            close()
        }

        drawPath(path = bubble, color = tint, style = stroke)
        listOf(0.34f, 0.5f, 0.66f).forEach { x ->
            drawCircle(
                color = tint,
                radius = size.minDimension * 0.045f,
                center = androidx.compose.ui.geometry.Offset(size.width * x, size.height * 0.5f),
            )
        }
    }
}

@Composable
private fun MainProfileIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = size.minDimension * 0.1f)
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.18f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.34f),
            style = stroke,
        )
        val shoulders = Path().apply {
            moveTo(size.width * 0.22f, size.height * 0.78f)
            cubicTo(
                size.width * 0.28f,
                size.height * 0.58f,
                size.width * 0.72f,
                size.height * 0.58f,
                size.width * 0.78f,
                size.height * 0.78f,
            )
        }
        drawPath(path = shoulders, color = tint, style = stroke)
    }
}

private enum class MainTab {
    Home,
    Conversations,
    Profile,
}

@Composable
private fun MainTab.label(): String {
    return when (this) {
        MainTab.Home -> stringResource(Res.string.home_tab_home)
        MainTab.Conversations -> stringResource(Res.string.home_tab_conversations)
        MainTab.Profile -> stringResource(Res.string.home_tab_profile)
    }
}
