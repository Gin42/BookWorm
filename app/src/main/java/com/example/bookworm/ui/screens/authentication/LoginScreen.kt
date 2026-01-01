package com.example.bookworm.ui.screens.authentication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookworm.R
import com.example.bookworm.core.data.models.AuthenticationResult
import com.example.bookworm.ui.BookWormRoute
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KSuspendFunction2


@Composable
fun LoginScreen(
    navController: NavController,
    state: LoginState,
    actions: LoginAction,
    onSignIn: KSuspendFunction2<String, String, AuthenticationResult>
) {
    Scaffold(
    )
    { contentPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_logo_desc),
                modifier = Modifier
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_sc_medium)),
                style = MaterialTheme.typography.displaySmall,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                stringResource(R.string.login_message),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Username
            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text(stringResource(R.string.username_label)) },
                placeholder = { Text(stringResource(R.string.username_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password
            OutlinedSecureTextField(
                state = state.password,
                label = { Text(stringResource(R.string.password_label)) },
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth(),
                textObfuscationMode =
                if (state.showPassword) {
                    TextObfuscationMode.Visible
                } else {
                    TextObfuscationMode.RevealLastTyped
                },
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
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (state.canSubmit) {
                        val signInResult = runBlocking {
                            onSignIn(
                                state.username,
                                state.password.text.toString()
                            )
                        }
                        when (signInResult) {
                            AuthenticationResult.Success -> {
                                Log.println(
                                    Log.DEBUG,
                                    TAG,
                                    "Success!"
                                )
                                navController.navigate(BookWormRoute.Home)
                            }

                            AuthenticationResult.WrongCredentials -> {
                                Log.println(
                                    Log.DEBUG,
                                    TAG,
                                    "Nope, wrong credentials"
                                )
                            }

                            else -> {
                                Log.println(
                                    Log.DEBUG,
                                    TAG,
                                    "Nope, login failed"
                                )
                            }
                        }
                    } else {
                        Log.println(
                            Log.DEBUG,
                            TAG,
                            "Nope, unfilled credentials"
                        )
                    }
                },
                modifier = Modifier
            ) {
                Text(
                    stringResource(R.string.sign_in_button_desc),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                buildAnnotatedString {
                    append(stringResource(R.string.sign_up_text))
                    withAnnotation(
                        tag = "sign-up",
                        annotation = "sign-up"
                    ) {

                        pushStyle(
                            SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                        append("Sign-up")
                        pop()
                    }
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .clickable(onClick = {
                        navController.navigate(BookWormRoute.Registration)
                    })
            )
        }
    }

}