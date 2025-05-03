package com.example.koursework.ui.screens.auth

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.koursework.ManagerActivity
import com.example.koursework.UserActivity
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.theme.MyAppTheme
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val loginState by viewModel.loginResult.collectAsState()

    LaunchedEffect(loginState) {
        loginState?.let { result ->
            when {
                result.success -> {
                    Toast.makeText(context, "Успешный вход", Toast.LENGTH_SHORT).show()
                    delay(300) // плавный переход
                    val intent = if (AppState.getUser()!!.isAdmin) {
                        Intent(context, ManagerActivity::class.java)
                    } else {
                        Intent(context, UserActivity::class.java)
                    }
                    context.startActivity(intent)
                }
                result.message.isNotBlank() -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (cardRef, registerButtonRef, loginButtonRef, loaderRef) = createRefs()

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(loaderRef) {
                        top.linkTo(parent.top, margin = 150.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }

        Card(
            modifier = Modifier
                .height(300.dp)
                .width(300.dp)
                .constrainAs(cardRef) {
                    top.linkTo(parent.top, margin = 220.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 32.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (emailFieldRef, passwordFieldRef) = createRefs()


                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(text = "example@gmail.com", color = MaterialTheme.colorScheme.outline) },
                        label = { Text(text = "Почта", color = MaterialTheme.colorScheme.outline) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            cursorColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.outline,
                            unfocusedTextColor = MaterialTheme.colorScheme.outline,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(emailFieldRef) {
                                top.linkTo(parent.top, margin = 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Пароль", color = MaterialTheme.colorScheme.outline) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            cursorColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.outline,
                            unfocusedTextColor = MaterialTheme.colorScheme.outline,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(passwordFieldRef) {
                                top.linkTo(emailFieldRef.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("RegisterScreen")
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(150.dp).constrainAs(registerButtonRef) {
                bottom.linkTo(parent.bottom, margin = 32.dp)
                start.linkTo(parent.start, margin = 32.dp)
            }
        ) {
            Text(text = "К регистрации")
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Введите почту и пароль", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(email, password)
                }
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(150.dp).constrainAs(loginButtonRef) {
                bottom.linkTo(parent.bottom, margin = 32.dp)
                end.linkTo(parent.end, margin = 32.dp)
            }
        ) {
            Text(text = "Войти")
        }
    }
}


@Preview(showBackground = true, name = "Login Screen Preview",
    device = "spec:width=1080px,height=2340px,dpi=440"
)
@Composable
fun LoginScreenPreview() {
    MyAppTheme {
        val navController = rememberNavController()

        LoginScreen(navController)
    }
}
