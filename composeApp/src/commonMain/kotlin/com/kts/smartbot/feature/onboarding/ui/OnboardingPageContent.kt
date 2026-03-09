package com.kts.smartbot.feature.onboarding.ui

import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPage
import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPageType
import org.jetbrains.compose.resources.StringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.onboarding_builder_description
import smartbot.composeapp.generated.resources.onboarding_builder_highlight_1
import smartbot.composeapp.generated.resources.onboarding_builder_highlight_2
import smartbot.composeapp.generated.resources.onboarding_builder_highlight_3
import smartbot.composeapp.generated.resources.onboarding_builder_title
import smartbot.composeapp.generated.resources.onboarding_events_description
import smartbot.composeapp.generated.resources.onboarding_events_highlight_1
import smartbot.composeapp.generated.resources.onboarding_events_highlight_2
import smartbot.composeapp.generated.resources.onboarding_events_highlight_3
import smartbot.composeapp.generated.resources.onboarding_events_title
import smartbot.composeapp.generated.resources.onboarding_inbox_description
import smartbot.composeapp.generated.resources.onboarding_inbox_highlight_1
import smartbot.composeapp.generated.resources.onboarding_inbox_highlight_2
import smartbot.composeapp.generated.resources.onboarding_inbox_highlight_3
import smartbot.composeapp.generated.resources.onboarding_inbox_title

internal data class OnboardingPageContent(
    val title: StringResource,
    val description: StringResource,
    val highlights: List<StringResource>,
)

internal val OnboardingPage.title: StringResource
    get() = content.title

internal val OnboardingPage.description: StringResource
    get() = content.description

internal val OnboardingPage.highlights: List<StringResource>
    get() = content.highlights

private val OnboardingPage.content: OnboardingPageContent
    get() = when (type) {
        OnboardingPageType.Builder -> OnboardingPageContent(
            title = Res.string.onboarding_builder_title,
            description = Res.string.onboarding_builder_description,
            highlights = listOf(
                Res.string.onboarding_builder_highlight_1,
                Res.string.onboarding_builder_highlight_2,
                Res.string.onboarding_builder_highlight_3,
            ),
        )

        OnboardingPageType.Assistant -> OnboardingPageContent(
            title = Res.string.onboarding_inbox_title,
            description = Res.string.onboarding_inbox_description,
            highlights = listOf(
                Res.string.onboarding_inbox_highlight_1,
                Res.string.onboarding_inbox_highlight_2,
                Res.string.onboarding_inbox_highlight_3,
            ),
        )

        OnboardingPageType.Automation -> OnboardingPageContent(
            title = Res.string.onboarding_events_title,
            description = Res.string.onboarding_events_description,
            highlights = listOf(
                Res.string.onboarding_events_highlight_1,
                Res.string.onboarding_events_highlight_2,
                Res.string.onboarding_events_highlight_3,
            ),
        )
    }
