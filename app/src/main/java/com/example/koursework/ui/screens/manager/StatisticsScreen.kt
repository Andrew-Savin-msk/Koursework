package com.example.koursework.ui.screens.manager

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.MainActivity
import com.example.koursework.R
import com.example.koursework.ui.outbox.AppState
import com.example.koursework.ui.theme.MyAppTheme


@Composable
fun StatisticsScreen() {
    val context = LocalContext.current

    // Корневой ConstraintLayout
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Создаём ссылки для элементов верхнего уровня
        val (
            userInfoCardRef,
            closedDealsCardRef,
            soldCarsCardRef,
            signOutButtonRef,
            changeThemeButtonRef
        ) = createRefs()

        // -------------------------
        // 1. Карточка с данными пользователя
        // -------------------------
        Card(
            modifier = Modifier
                .constrainAs(userInfoCardRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            // Внутри карточки — свой ConstraintLayout
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Ссылки для текстовых полей внутри карточки
                val (
                    userTitleRef,
                    positionRef,
                    emailRef,
                    phoneRef
                ) = createRefs()

                Text(
                    text = "Пользователь",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(userTitleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Должность: Менеджер по продажам",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(positionRef) {
                        top.linkTo(userTitleRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Почта: company1@mail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(emailRef) {
                        top.linkTo(positionRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Телефон: +7 (800) 555 35 35",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(phoneRef) {
                        top.linkTo(emailRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        // -------------------------
        // 2. Карточка "Закрыто за месяц"
        // -------------------------
        Card(
            modifier = Modifier
                .constrainAs(closedDealsCardRef) {
                    top.linkTo(userInfoCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (titleRef, dealsCountRef) = createRefs()

                Text(
                    text = "Закрыто за месяц",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "Сделок: 4",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(dealsCountRef) {
                        top.linkTo(titleRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        // -------------------------
        // 3. Карточка "Продано автомобилей"
        // -------------------------
        Card(
            modifier = Modifier
                .constrainAs(soldCarsCardRef) {
                    top.linkTo(closedDealsCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (titleRef, soldCountRef) = createRefs()

                Text(
                    text = "Продано автомобилей",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "30",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.constrainAs(soldCountRef) {
                        top.linkTo(titleRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        Button(
            onClick = {
                AppState.logOut()
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .width(150.dp)
                .constrainAs(signOutButtonRef) {
                    top.linkTo(soldCarsCardRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        ) {
            Text(text = "Выйти")
        }

        Button(
            onClick = {
                (context as? Activity)?.recreate()
                AppState.switchTheme()
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .width(150.dp)
                .constrainAs(changeThemeButtonRef) {
                    top.linkTo(soldCarsCardRef.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Text(text = "Сменить тему")
        }
    }
}


@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun AdditionalPreview() {
    MyAppTheme {
        StatisticsScreen()
    }
}