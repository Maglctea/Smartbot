package com.kts.smartbot.di

import com.kts.smartbot.feature.auth.data.InMemoryAuthSessionRepository
import com.kts.smartbot.feature.auth.data.RemoteAuthRepository
import com.kts.smartbot.feature.auth.data.createAuthHttpClient
import com.kts.smartbot.feature.auth.domain.repository.AuthRepository
import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository
import com.kts.smartbot.feature.auth.domain.usecase.IsUserAuthorizedUseCase
import com.kts.smartbot.feature.auth.domain.usecase.LoginUseCase
import com.kts.smartbot.feature.auth.domain.usecase.LogoutUseCase
import com.kts.smartbot.feature.conversations.data.ConversationsRepositoryImpl
import com.kts.smartbot.feature.conversations.domain.repository.ConversationsRepository
import com.kts.smartbot.feature.onboarding.data.StaticOnboardingRepository
import com.kts.smartbot.feature.onboarding.domain.repository.OnboardingRepository
import com.kts.smartbot.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single<AuthSessionRepository> { InMemoryAuthSessionRepository() }
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
    single { createAuthHttpClient(sessionRepository = get()) }
    single<AuthRepository> {
        RemoteAuthRepository(
            httpClient = get(),
            sessionRepository = get(),
            json = get(),
        )
    }
    single<ConversationsRepository> {
        ConversationsRepositoryImpl(
            httpClient = get(),
            json = get(),
        )
    }
    factory { LoginUseCase(repository = get()) }
    factory { IsUserAuthorizedUseCase(repository = get()) }
    factory { LogoutUseCase(repository = get()) }
    single<OnboardingRepository> { StaticOnboardingRepository() }
    factory { GetOnboardingPagesUseCase(repository = get()) }
}
