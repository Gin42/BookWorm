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
import com.example.bookworm.ui.composables.ImageWithPlaceholder
import com.example.bookworm.ui.composables.Size

@Composable
fun RegistrationScreen(
    navController: NavController,
    state: RegistrationState,
    actions: RegistrationActions,
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 70.dp, bottom = 30.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App logo",
                    modifier = Modifier
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "BookWorm",
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_sc_medium)),
                    style = MaterialTheme.typography.displaySmall,
                )
            }


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    "Create your account",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Image selection
                ImageWithPlaceholder(
                    null, Size.Sm,
                    desc = "User photo",
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Username
                OutlinedTextField(
                    value = state.username,
                    onValueChange = actions::setUsername,
                    label = { Text("Username") },
                    placeholder = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Password
                var showPassword: Boolean = false

                OutlinedSecureTextField(
                    state = state.password,
                    label = { Text("Password") },
                    placeholder = { Text("password") },
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
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(16.dp)
                                .clickable { actions.setShowPassword(!state.showPassword) }
                        )
                    },
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Registration button
                Button(
                    onClick = {
                        if (state.canSubmit) {
                            actions.performRegistration { result ->
                                when (result) {
                                    AuthenticationResult.Success -> {
                                        Log.println(
                                            Log.DEBUG,
                                            TAG,
                                            "Success!"
                                        )
                                        navController.navigate(BookWormRoute.Home)
                                    }
                                    AuthenticationResult.UsernameTaken -> {
                                        Log.println(
                                            Log.DEBUG,
                                            TAG,
                                            "Nope, username taken"
                                        )
                                    }

                                    else -> {
                                        Log.println(
                                            Log.DEBUG,
                                            TAG,
                                            "Nope, registration failed"
                                        )
                                    }
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
                    Text("Sign up", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Go to login
                Text(
                    buildAnnotatedString {
                        append("You already have an account? ")
                        withAnnotation(
                            tag = "login",
                            annotation = "login"
                        ) {

                            pushStyle(
                                SpanStyle(
                                    color = Color.Blue,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                            append("Login")
                            pop()
                        }
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .clickable(onClick = {
                            navController.navigate(BookWormRoute.Login) // Replace with your actual route
                        })
                )
            }
        }
    }
}
