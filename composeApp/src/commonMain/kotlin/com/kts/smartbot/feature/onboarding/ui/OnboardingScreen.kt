package com.kts.smartbot.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.theme.SmartbotDefaults
import com.kts.smartbot.core.theme.smartbotScreenBackgroundBrush
import com.kts.smartbot.core.ui.SmartbotBrandPill
import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPage
import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPageType
import com.kts.smartbot.feature.onboarding.presentation.OnboardingUiState
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.onboarding_finish
import smartbot.composeapp.generated.resources.onboarding_header_title
import smartbot.composeapp.generated.resources.onboarding_illustration_channel_connected
import smartbot.composeapp.generated.resources.onboarding_illustration_live
import smartbot.composeapp.generated.resources.onboarding_illustration_new_reply
import smartbot.composeapp.generated.resources.onboarding_illustration_run
import smartbot.composeapp.generated.resources.onboarding_illustration_scenario_finished
import smartbot.composeapp.generated.resources.onboarding_illustration_telegram
import smartbot.composeapp.generated.resources.onboarding_illustration_vk
import smartbot.composeapp.generated.resources.onboarding_illustration_whatsapp
import smartbot.composeapp.generated.resources.onboarding_next
import smartbot.composeapp.generated.resources.onboarding_site_label
import smartbot.composeapp.generated.resources.onboarding_skip

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    pagerState: PagerState,
    onSkipClicked: () -> Unit,
    onSiteClicked: () -> Unit,
    onPrimaryActionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = smartbotScreenBackgroundBrush(accentAlpha = 0.16f))
            .safeDrawingPadding()
            .padding(horizontal = SmartbotDefaults.screenHorizontalPadding, vertical = 16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            OnboardingHeader(
                onSkipClicked = onSkipClicked,
                onSiteClicked = onSiteClicked,
            )

            OnboardingPagerSection(
                pages = uiState.pages,
                pagerState = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            OnboardingFooter(
                currentPage = uiState.currentPage,
                pageCount = uiState.pages.size,
                isLastPage = uiState.isLastPage,
                onPrimaryActionClicked = onPrimaryActionClicked,
            )
        }
    }
}

@Composable
private fun OnboardingHeader(
    onSkipClicked: () -> Unit,
    onSiteClicked: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmartbotBrandPill(
                text = stringResource(Res.string.onboarding_site_label),
                onClick = onSiteClicked,
            )

            TextButton(onClick = onSkipClicked) {
                Text(stringResource(Res.string.onboarding_skip))
            }
        }

        Text(
            text = stringResource(Res.string.onboarding_header_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun OnboardingPagerSection(
    pages: List<OnboardingPage>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { pageIndex ->
        OnboardingPageCard(
            page = pages[pageIndex],
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )
    }
}

@Composable
private fun OnboardingFooter(
    currentPage: Int,
    pageCount: Int,
    isLastPage: Boolean,
    onPrimaryActionClicked: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            PagerIndicator(
                currentPage = currentPage,
                pageCount = pageCount,
            )
        }

        Button(
            onClick = onPrimaryActionClicked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (isLastPage) {
                    stringResource(Res.string.onboarding_finish)
                } else {
                    stringResource(Res.string.onboarding_next)
                },
            )
        }
    }
}

@Composable
private fun OnboardingPageCard(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(32.dp),
        color = colors.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                OnboardingIllustration(
                    type = page.type,
                    modifier = Modifier.fillMaxWidth(),
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(page.title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(page.description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant,
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                page.highlights.forEach { item ->
                    OnboardingHighlightChip(text = stringResource(item))
                }
            }
        }
    }
}

@Composable
private fun OnboardingHighlightChip(text: String) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.primaryContainer,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onPrimaryContainer,
        )
    }
}

@Composable
private fun OnboardingIllustration(
    type: OnboardingPageType,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colors.primary,
                        colors.secondaryContainer,
                    ),
                ),
            )
            .padding(18.dp),
    ) {
        when (type) {
            OnboardingPageType.Builder -> ScenarioIllustration()
            OnboardingPageType.Assistant -> InboxIllustration()
            OnboardingPageType.Automation -> EventsIllustration()
        }
    }
}

@Composable
private fun ScenarioIllustration() {
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IllustrationCard(
                modifier = Modifier.weight(1f),
                accent = colors.surface,
                titleColor = colors.primary,
            )
            IllustrationCard(
                modifier = Modifier.weight(1f),
                accent = colors.surface.copy(alpha = 0.88f),
                titleColor = colors.tertiary,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            RepeatDot(colors = listOf(Color.White, Color.White.copy(alpha = 0.6f), Color.White))
            Surface(
                shape = CircleShape,
                color = Color.White,
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_illustration_run),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = colors.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun InboxIllustration() {
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MessageBubble(
            label = stringResource(Res.string.onboarding_illustration_telegram),
            bubbleColor = Color.White,
            textColor = colors.primary,
            widthFraction = 0.78f,
        )
        MessageBubble(
            label = stringResource(Res.string.onboarding_illustration_vk),
            bubbleColor = colors.primary.copy(alpha = 0.24f),
            textColor = Color.White,
            widthFraction = 0.62f,
            alignEnd = true,
        )
        MessageBubble(
            label = stringResource(Res.string.onboarding_illustration_whatsapp),
            bubbleColor = Color.White,
            textColor = colors.primary,
            widthFraction = 0.7f,
        )
    }
}

@Composable
private fun EventsIllustration() {
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_illustration_live),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = colors.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            RepeatDot(colors = listOf(Color.White, Color.White.copy(alpha = 0.6f), Color.White))
        }
        EventLine(
            label = stringResource(Res.string.onboarding_illustration_new_reply),
            modifier = Modifier.fillMaxWidth(),
        )
        EventLine(
            label = stringResource(Res.string.onboarding_illustration_scenario_finished),
            modifier = Modifier.fillMaxWidth(0.86f),
        )
        EventLine(
            label = stringResource(Res.string.onboarding_illustration_channel_connected),
            modifier = Modifier.fillMaxWidth(0.72f),
        )
    }
}

@Composable
private fun IllustrationCard(
    modifier: Modifier = Modifier,
    accent: Color,
    titleColor: Color,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = accent,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            RepeatDot(colors = listOf(titleColor, titleColor.copy(alpha = 0.5f), titleColor.copy(alpha = 0.3f)))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(titleColor.copy(alpha = 0.18f)),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(titleColor.copy(alpha = 0.32f)),
            )
        }
    }
}

@Composable
private fun MessageBubble(
    label: String,
    bubbleColor: Color,
    textColor: Color,
    widthFraction: Float,
    alignEnd: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (alignEnd) Arrangement.End else Arrangement.Start,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(widthFraction),
            shape = RoundedCornerShape(22.dp),
            color = bubbleColor,
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun EventLine(
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.9f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun RepeatDot(colors: List<Color>) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

@Composable
private fun PagerIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isSelected) 28.dp else 10.dp, 10.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                    ),
            )
        }
    }
}
