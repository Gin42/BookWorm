package com.example.bookworm.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import com.example.bookworm.R
import com.example.bookworm.core.data.database.entities.UserEntity
import com.example.bookworm.core.data.models.AuthenticationResults
import com.example.bookworm.core.data.models.Theme
import com.example.bookworm.ui.composables.ImagePickerBottomSheet
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.Size
import com.example.bookworm.ui.screens.settings.ThemeState
import ir.ehsannarmani.compose_charts.ColumnChart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KSuspendFunction1

@Composable
fun RegistrationScreen(

    state: RegistrationState,
    actions: RegistrationActions,
    themeState: ThemeState,
    onSignUp: KSuspendFunction1<UserEntity, AuthenticationResults>,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Scaffold()
    { contentPadding ->
        Column (
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            val iconColor = when (themeState.theme) {
                Theme.Light -> Color.Black
                Theme.Dark -> Color.White
                Theme.System -> if (isSystemInDarkTheme()) Color.White else Color.Black
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_logo_desc),
                modifier = Modifier
                    .height(120.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_sc_medium)),
                style = MaterialTheme.typography.displaySmall,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                stringResource(R.string.registration_message),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Image selection

            if (state.isImagePickerVisible) {
                ImagePickerBottomSheet(
                    onSelected = { image ->
                        actions.setUserPhoto(image)
                    },
                    onDismissRequest = {
                        actions.setPickerVisible(false)
                    }
                )
            }

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                ImageWithPlaceholder(
                    state.userPhoto, Size.Lg,
                    desc = stringResource(R.string.user_profile_picture_desc),
                    CircleShape
                )
                FilledIconButton(
                    onClick = { actions.setPickerVisible(true) },
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.add_image_icon_desc),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Username
            OutlinedTextField(
                value = state.username,
                onValueChange = {
                    actions.setUsername(it)
                    actions.setUsernameError(false)
                    actions.setPasswordError(false)
                },
                label = { Text(stringResource(R.string.username_label)) },
                placeholder = { Text(stringResource(R.string.username_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium,
                supportingText = {
                    if (state.usernameError) {
                        when (state.errorMessage) {
                            AuthenticationResults.CannotSubmit -> {
                                Text(stringResource(R.string.fill_all_fields_error))
                            }

                            AuthenticationResults.UsernameTaken -> {
                                Text(stringResource(R.string.username_taken_error))
                            }

                            else -> {
                            }
                        }
                    } else {
                        Text(stringResource(R.string.required_field_message))
                    }
                },
                isError = state.usernameError,
            )

            // Password
            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    actions.setPassword(it)
                    actions.setUsernameError(false)
                    actions.setPasswordError(false)
                },
                label = { Text(stringResource(R.string.password_label)) },
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                trailingIcon = {
                    Icon(
                        if (state.showPassword) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = stringResource(R.string.password_visibility_icon_desc),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(16.dp)
                            .clickable { actions.setShowPassword(!state.showPassword) }
                    )
                },
                supportingText = {
                    if (state.passwordError && state.errorMessage == AuthenticationResults.CannotSubmit) {
                        Text(stringResource(R.string.fill_all_fields_error))
                    } else {
                        Text(stringResource(R.string.required_field_message))
                    }
                },
                isError = state.passwordError
            )

            Spacer(modifier = Modifier.padding(8.dp))

            val coroutineScope = rememberCoroutineScope()

            // Registration button
            Button(
                onClick = {
                    if (state.canSubmit) {
                        coroutineScope.launch {
                            val signupResult = onSignUp(state.toUser())
                            if (signupResult == AuthenticationResults.Success) {
                                onNavigateToHome()
                            } else if (signupResult == AuthenticationResults.UsernameTaken) {
                                actions.setUsernameError(true)
                                actions.setErrorMessage(AuthenticationResults.UsernameTaken)
                            }
                        }
                    } else {
                        actions.setUsernameError(true)
                        actions.setPasswordError(true)
                        actions.setErrorMessage(AuthenticationResults.CannotSubmit)
                    }
                },
                modifier = Modifier
            ) {
                Text(
                    stringResource(R.string.sign_up_button),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            val signUpText = stringResource(R.string.sign_in_text)
            val signUpLinkText = stringResource(R.string.sign_in_link_text)

            val color = MaterialTheme.colorScheme.primary

            val annotatedString = buildAnnotatedString {
                append(signUpText)
                append(" ")

                withAnnotation(
                    tag = "sign-up",
                    annotation = "sign-up"
                ) {
                    pushStyle(
                        SpanStyle(
                            color = color,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    append(signUpLinkText)
                    pop()
                }
            }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .clickable(onClick = {
                        onNavigateToLogin()
                    })
            )
        }
    }
}

