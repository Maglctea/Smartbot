package com.kts.smartbot.feature.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.theme.SmartbotDefaults
import com.kts.smartbot.core.theme.smartbotScreenBackgroundBrush
import com.kts.smartbot.core.ui.SmartbotBrandPill
import com.kts.smartbot.core.ui.SmartbotCard
import com.kts.smartbot.feature.auth.domain.model.AuthFailureReason
import com.kts.smartbot.feature.auth.domain.model.AuthProvider
import com.kts.smartbot.feature.auth.presentation.AuthUiState
import com.kts.smartbot.feature.auth.ui.captcha.SmartCaptchaWidget
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.auth_brand
import smartbot.composeapp.generated.resources.auth_button
import smartbot.composeapp.generated.resources.auth_button_loading
import smartbot.composeapp.generated.resources.auth_captcha_checkbox_label
import smartbot.composeapp.generated.resources.auth_captcha_close
import smartbot.composeapp.generated.resources.auth_captcha_label
import smartbot.composeapp.generated.resources.auth_captcha_title
import smartbot.composeapp.generated.resources.auth_captcha_verified
import smartbot.composeapp.generated.resources.auth_email_label
import smartbot.composeapp.generated.resources.auth_email_placeholder
import smartbot.composeapp.generated.resources.auth_error_captcha_required
import smartbot.composeapp.generated.resources.auth_error_invalid_credentials
import smartbot.composeapp.generated.resources.auth_error_network
import smartbot.composeapp.generated.resources.auth_error_unknown
import smartbot.composeapp.generated.resources.auth_forgot_password
import smartbot.composeapp.generated.resources.auth_password_label
import smartbot.composeapp.generated.resources.auth_password_placeholder
import smartbot.composeapp.generated.resources.auth_provider_elama
import smartbot.composeapp.generated.resources.auth_provider_google
import smartbot.composeapp.generated.resources.auth_provider_section_title
import smartbot.composeapp.generated.resources.auth_provider_telegram
import smartbot.composeapp.generated.resources.auth_provider_vk
import smartbot.composeapp.generated.resources.auth_register
import smartbot.composeapp.generated.resources.social_auth_development_confirm
import smartbot.composeapp.generated.resources.social_auth_development_message
import smartbot.composeapp.generated.resources.social_auth_development_title
import smartbot.composeapp.generated.resources.auth_title
import smartbot.composeapp.generated.resources.oauth_elama_icon
import smartbot.composeapp.generated.resources.oauth_google_icon
import smartbot.composeapp.generated.resources.oauth_telegram_icon
import smartbot.composeapp.generated.resources.oauth_vk_icon

@Composable
fun AuthLoginScreen(
    uiState: AuthUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onCaptchaTokenChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onProviderClicked: (AuthProvider) -> Unit,
    socialAuthPlaceholderProvider: AuthProvider?,
    onDismissSocialAuthPlaceholder: () -> Unit,
    onBrandClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val languageCode = Locale.current.language.takeIf { it.isNotBlank() } ?: "en"
    var isCaptchaFullscreen by rememberSaveable(uiState.captchaResetKey) { mutableStateOf(false) }

    LaunchedEffect(uiState.captchaToken) {
        if (uiState.captchaToken.isNotBlank()) {
            isCaptchaFullscreen = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = smartbotScreenBackgroundBrush(accentAlpha = 0.16f))
            .safeDrawingPadding()
            .padding(
                horizontal = SmartbotDefaults.screenHorizontalPadding,
                vertical = SmartbotDefaults.screenVerticalPadding,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AuthBrandPill(onClick = onBrandClicked)
            AuthHeader()
            AuthFormCard(
                uiState = uiState,
                onOpenCaptcha = {
                    if (uiState.captchaToken.isBlank()) {
                        isCaptchaFullscreen = true
                    }
                },
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
                onLoginClicked = onLoginClicked,
                onProviderClicked = onProviderClicked,
            )
        }

        if (isCaptchaFullscreen) {
            CaptchaFullscreenOverlay(
                languageCode = languageCode,
                resetKey = uiState.captchaResetKey,
                onDismiss = { isCaptchaFullscreen = false },
                onTokenChanged = onCaptchaTokenChanged,
            )
        }

        if (socialAuthPlaceholderProvider != null) {
            SocialAuthPlaceholderDialog(
                provider = socialAuthPlaceholderProvider,
                onDismiss = onDismissSocialAuthPlaceholder,
            )
        }
    }
}

@Composable
private fun AuthBrandPill(onClick: () -> Unit) {
    SmartbotBrandPill(
        text = stringResource(Res.string.auth_brand),
        onClick = onClick,
    )
}

@Composable
private fun AuthHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(Res.string.auth_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun AuthFormCard(
    uiState: AuthUiState,
    onOpenCaptcha: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onProviderClicked: (AuthProvider) -> Unit,
) {
    SmartbotCard(
        paddingValues = androidx.compose.foundation.layout.PaddingValues(SmartbotDefaults.compactCardPadding),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AuthCredentialsSection(
                uiState = uiState,
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
            )
            AuthCaptchaSection(
                isVerified = uiState.captchaToken.isNotBlank(),
                onOpenCaptcha = onOpenCaptcha,
            )
            AuthErrorText(error = uiState.error)
            Button(
                onClick = onLoginClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isSubmitEnabled,
            ) {
                Text(
                    if (uiState.isLoading) {
                        stringResource(Res.string.auth_button_loading)
                    } else {
                        stringResource(Res.string.auth_button)
                    },
                )
            }
            AuthProvidersSection(
                providers = uiState.providers,
                isLoading = uiState.isLoading,
                onProviderClicked = onProviderClicked,
            )
            AuthAuxiliaryActions()
        }
    }
}

@Composable
private fun AuthProvidersSection(
    providers: List<AuthProvider>,
    isLoading: Boolean,
    onProviderClicked: (AuthProvider) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(Res.string.auth_provider_section_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            providers.forEach { provider ->
                OutlinedButton(
                    onClick = { onProviderClicked(provider) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    shape = SmartbotDefaults.fieldShape,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(provider.icon()),
                            contentDescription = provider.label(),
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = provider.label(),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.width(34.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthCredentialsSection(
    uiState: AuthUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = uiState.email,
        onValueChange = onEmailChanged,
        label = { Text(stringResource(Res.string.auth_email_label)) },
        placeholder = { Text(stringResource(Res.string.auth_email_placeholder)) },
        modifier = Modifier.fillMaxWidth(),
        shape = SmartbotDefaults.fieldShape,
        singleLine = true,
        enabled = !uiState.isLoading,
    )
    OutlinedTextField(
        value = uiState.password,
        onValueChange = onPasswordChanged,
        label = { Text(stringResource(Res.string.auth_password_label)) },
        placeholder = { Text(stringResource(Res.string.auth_password_placeholder)) },
        modifier = Modifier.fillMaxWidth(),
        shape = SmartbotDefaults.fieldShape,
        singleLine = true,
        enabled = !uiState.isLoading,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                PasswordVisibilityIcon(
                    isVisible = isPasswordVisible,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
private fun AuthCaptchaSection(
    isVerified: Boolean,
    onOpenCaptcha: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(Res.string.auth_captcha_label),
            style = MaterialTheme.typography.titleMedium,
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = !isVerified,
                    onClick = onOpenCaptcha,
                ),
            shape = SmartbotDefaults.fieldShape,
            color = if (isVerified) {
                colors.primaryContainer.copy(alpha = 0.5f)
            } else {
                colors.surfaceContainerLow
            },
            tonalElevation = 2.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(Res.string.auth_captcha_label),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = if (isVerified) {
                            stringResource(Res.string.auth_captcha_verified)
                        } else {
                            stringResource(Res.string.auth_captcha_checkbox_label)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isVerified) {
                            colors.primary
                        } else {
                            colors.onSurfaceVariant
                        },
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isVerified) {
                        colors.primary
                    } else {
                        colors.surface
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isVerified) {
                            colors.primary
                        } else {
                            colors.outline.copy(alpha = 0.6f)
                        },
                    ),
                ) {
                    Box(
                        modifier = Modifier.size(22.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isVerified) {
                            Text(
                                text = "\u2713",
                                style = MaterialTheme.typography.labelLarge,
                                color = colors.onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CaptchaFullscreenOverlay(
    languageCode: String,
    resetKey: Int,
    onDismiss: () -> Unit,
    onTokenChanged: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.auth_captcha_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.auth_captcha_close))
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = SmartbotDefaults.secondaryCardShape,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                SmartCaptchaWidget(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    languageCode = languageCode,
                    resetKey = resetKey,
                    onTokenChanged = onTokenChanged,
                )
            }
        }
    }
}

@Composable
private fun AuthErrorText(error: AuthFailureReason?) {
    if (error == null) {
        return
    }

    Text(
        text = when (error) {
            AuthFailureReason.InvalidCredentials -> stringResource(Res.string.auth_error_invalid_credentials)
            AuthFailureReason.CaptchaRequired -> stringResource(Res.string.auth_error_captcha_required)
            AuthFailureReason.Network -> stringResource(Res.string.auth_error_network)
            AuthFailureReason.Unknown -> stringResource(Res.string.auth_error_unknown)
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
private fun AuthAuxiliaryActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TextButton(onClick = {}) {
            Text(stringResource(Res.string.auth_forgot_password))
        }
        TextButton(onClick = {}) {
            Text(stringResource(Res.string.auth_register))
        }
    }
}

@Composable
private fun AuthProvider.label(): String {
    return when (this) {
        AuthProvider.Google -> stringResource(Res.string.auth_provider_google)
        AuthProvider.Vk -> stringResource(Res.string.auth_provider_vk)
        AuthProvider.Telegram -> stringResource(Res.string.auth_provider_telegram)
        AuthProvider.ELama -> stringResource(Res.string.auth_provider_elama)
    }
}

private fun AuthProvider.icon() = when (this) {
    AuthProvider.Google -> Res.drawable.oauth_google_icon
    AuthProvider.Vk -> Res.drawable.oauth_vk_icon
    AuthProvider.Telegram -> Res.drawable.oauth_telegram_icon
    AuthProvider.ELama -> Res.drawable.oauth_elama_icon
}

@Composable
private fun PasswordVisibilityIcon(
    isVisible: Boolean,
    tint: androidx.compose.ui.graphics.Color,
) {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(20.dp)) {
        val strokeWidth = size.minDimension * 0.09f
        val eye = androidx.compose.ui.graphics.Path().apply {
            moveTo(size.width * 0.14f, size.height * 0.5f)
            quadraticTo(size.width * 0.5f, size.height * 0.16f, size.width * 0.86f, size.height * 0.5f)
            quadraticTo(size.width * 0.5f, size.height * 0.84f, size.width * 0.14f, size.height * 0.5f)
            close()
        }

        drawPath(
            path = eye,
            brush = SolidColor(tint),
            style = Stroke(width = strokeWidth),
        )
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.11f,
            center = center,
        )
        if (!isVisible) {
            drawLine(
                color = tint,
                start = androidx.compose.ui.geometry.Offset(size.width * 0.18f, size.height * 0.82f),
                end = androidx.compose.ui.geometry.Offset(size.width * 0.82f, size.height * 0.18f),
                strokeWidth = strokeWidth,
            )
        }
    }
}

@Composable
private fun SocialAuthPlaceholderDialog(
    provider: AuthProvider,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.social_auth_development_confirm))
            }
        },
        title = {
            Text(
                text = stringResource(
                    Res.string.social_auth_development_title,
                    provider.label(),
                ),
            )
        },
        text = {
            Text(
                text = stringResource(
                    Res.string.social_auth_development_message,
                    provider.label(),
                ),
            )
        },
    )
}
