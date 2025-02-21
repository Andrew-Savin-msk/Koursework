package com.example.koursework.ui.screens.user

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.koursework.MainActivity
import com.example.koursework.ui.theme.MyAppTheme
import com.example.koursework.R
import com.example.koursework.UserActivity
import com.example.koursework.ui.outbox.AppState
import kotlinx.coroutines.MainScope


@Composable
fun AdditionalScreen() {
    // Корневой ConstraintLayout
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (boxBeforeImageRef, imageRef, boxAfterImageRef, signOutB, changeThemeB) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(boxBeforeImageRef) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .width(330.dp)
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                val (titleRef,
                    addressTitleRef, addressTextRef,
                    workTimeTitleRef, workTimeTextRef
                ) = createRefs()

                Text(
                    text = "Информация о салоне",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Адрес:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(addressTitleRef) {
                        top.linkTo(titleRef.bottom, margin = 5.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Огородный проезд, 1Ас5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.constrainAs(addressTextRef) {
                        top.linkTo(addressTitleRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Время работы:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(workTimeTitleRef) {
                        top.linkTo(addressTextRef.bottom, margin = 5.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Будние дни: 10:00 - 22:00\nСуббота: 10:00 - 20:00\nВоскресенье - выходной",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.constrainAs(workTimeTextRef) {
                        top.linkTo(workTimeTitleRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        // 2. Фото или заглушка
        Box(
            modifier = Modifier
                .constrainAs(imageRef) {
                    top.linkTo(boxBeforeImageRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(200.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.road),
                contentDescription = "Описание картинки",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Card(
            modifier = Modifier
                .constrainAs(boxAfterImageRef) {
                    top.linkTo(imageRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp) // заменено с 10.dp на 16.dp
                    end.linkTo(parent.end, margin = 16.dp)
                },
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .width(330.dp)
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                val (contactTitleRef, phoneRef, emailRef) = createRefs()

                Text(
                    text = "Контактные данные",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(contactTitleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Номер менеджера: +7 (900) 000 00 00",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.constrainAs(phoneRef) {
                        top.linkTo(contactTitleRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Почта салона: saloooon@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.constrainAs(emailRef) {
                        top.linkTo(phoneRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                    }
                )
            }
        }

        Button(
            onClick = {
                // TODO: Очистка стейта пользователя

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(150.dp).constrainAs(signOutB) {
                top.linkTo(boxAfterImageRef.bottom, margin = 32.dp)
                start.linkTo(parent.start, margin = 32.dp)
            }
        ) {
            Text(
                text = "Выйти",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Button(
            onClick = {
                (context as? Activity)?.recreate()
                AppState.switchTheme()
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(150.dp).constrainAs(changeThemeB) {
                top.linkTo(boxAfterImageRef.bottom, margin = 32.dp)
                end.linkTo(parent.end, margin = 32.dp)
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
        AdditionalScreen()
    }
}