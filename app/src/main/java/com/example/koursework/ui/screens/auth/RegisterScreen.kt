package com.example.koursework.ui.screens.auth

import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.koursework.ui.theme.MyAppTheme

@Composable
fun RegisterScreen(navController: NavController) {
    // Локальные стейты для ввода почты и пароля
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRep by remember { mutableStateOf("") }
    val context = LocalContext.current

    // ConstraintLayout на весь экран
    ConstraintLayout (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Задаём общий фон
    ) {
        // Создаём "якоря" (Refs) для размещения элементов
        val (cardRef, emailRef, passwordRef, registerButtonRef, loginButtonRef) = createRefs()

        // Карточка, в которой расположены поля ввода
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
                containerColor = MaterialTheme.colorScheme.primaryContainer, //Card background color
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
                    val (emailFieldRef, passwordFieldRef, passwordRepeatFieldRef) = createRefs()

                    // Поле для почты
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

                    // Поле для пароля
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Пароль", color = MaterialTheme.colorScheme.outline) },
//                        placeholder = { Text(text = "qwerty12345[]/", color = MaterialTheme.colorScheme.outline) },
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

                    OutlinedTextField(
                        value = passwordRep,
                        onValueChange = { passwordRep = it },
                        label = { Text(text = "Пароль", color = MaterialTheme.colorScheme.outline) },
//                        placeholder = { Text(text = "qwerty12345[]/", color = MaterialTheme.colorScheme.outline) },
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
                            .constrainAs(passwordRepeatFieldRef) {
                                top.linkTo(passwordFieldRef.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("LoginScreen") {
                    popUpTo("RegisterScreen") { inclusive = true }
                }
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.width(150.dp).constrainAs(registerButtonRef) {
                bottom.linkTo(parent.bottom, margin = 32.dp)
                start.linkTo(parent.start, margin = 32.dp)
            }
        ) {
            Text(text = "Ко входу")
        }

        Button(
            onClick = {
                /// TODO обработка пользователя
                navController.navigate("LoginScreen") {
                    popUpTo("RegisterScreen") { inclusive = true }
                }

                Toast.makeText(context, "Вы успешно зарегестрировались!", Toast.LENGTH_SHORT).show()
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.width(150.dp).constrainAs(loginButtonRef) {
                bottom.linkTo(parent.bottom, margin = 32.dp)
                end.linkTo(parent.end, margin = 32.dp)
            }
        ) {
            Text(text = "Зарегистрироваться")
        }
    }
}


@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun RegisterPreview() {
     MyAppTheme {
         val navController = rememberNavController()

         RegisterScreen(navController)
     }
}
