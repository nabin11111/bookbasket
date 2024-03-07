package com.chetan.orderdelivery.presentation.user.dashboard.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.MyDate
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun UserHistoryScreen(
    navController: NavHostController,
    state: UserHistoryState,
    event: (event: UserHistoryEvent) -> Unit
) {
    val cardSize = remember {
        mutableFloatStateOf(0f)
    }

    var rateDialog by remember {
        mutableStateOf(false)
    }
    var rateValue by remember {
        mutableFloatStateOf(0f)
    }
    var foodId by remember {
        mutableStateOf("")
    }
    var foodUrl by remember {
        mutableStateOf("")
    }
    if (rateDialog) {
        Dialog(onDismissRequest = { rateDialog = false }, content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = "Rate it", style = MaterialTheme.typography.headlineMedium)
                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(shape = CircleShape),
                        model = foodUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    RatingBar(
                        size = 30.dp,
                        value = rateValue,
                        style = RatingBarStyle.Default,
                        onValueChange = {
                            rateValue = it
                        },
                        onRatingChanged = {},
                        numOfStars = 5,
                        spaceBetween = 1.dp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            enabled = rateValue != 0f,
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            onClick = {
                                event(
                                    UserHistoryEvent.RateIt(
                                        id = foodId,
                                        url = foodUrl,
                                        value = rateValue
                                    )
                                )
                                rateDialog = false
                            },
                        ) {
                            Text(text = "Rate it", color = Color.White)
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                            onClick = {
                                rateDialog = false
                            },
                        ) {
                            Text(text = "Cancel", color = Color.White)
                        }
                    }
                }
            }
        })
    }
    Scaffold(topBar = {

    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (event != null && state.infoMsg.isCancellable == true) {
                        event(UserHistoryEvent.DismissInfoMsg)
                    }
                }, onPositive = { /*TODO*/ }) {

                }
            }


            val colorDash = MaterialTheme.colorScheme.outline
            LazyColumn(content = {
                items(state.historyList) { history ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = history.dateTime,
                                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        IconButton(onClick = {
                            event(UserHistoryEvent.DeleteMyHistory(history.orderId))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                    Column(modifier = Modifier
                        .padding(start = 10.dp)
                        .drawBehind {
                            drawLine(
                                color = colorDash,
                                start = Offset(50f, 0f),
                                end = Offset(size.width - 50, 0f),
                                strokeWidth = 5f
                            )

                            drawLine(
                                color = colorDash,
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height - 10),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 10f), 0f
                                ),
                                strokeWidth = 5f
                            )
                        }
                        .padding(horizontal = 15.dp)
                    ) {
                        history.orderList.forEach { foodDetails ->
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(modifier = Modifier) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp, bottom = 10.dp, end = 20.dp)
                                        .height(170.dp)
                                        .onGloballyPositioned {
                                            cardSize.value = Size(
                                                it.size.width.toFloat(), it.size.height.toFloat()
                                            ).width
                                        },
                                    shape = RoundedCornerShape(topStart = cardSize.value / 4),
                                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                    elevation = CardDefaults.cardElevation(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 10.dp),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            RatingBar(
                                                size = 15.dp,
                                                value = foodDetails.foodRating,
                                                style = RatingBarStyle.Default,
                                                onValueChange = {},
                                                onRatingChanged = {},
                                                numOfStars = 5,
                                                spaceBetween = 1.dp
                                            )
                                            Text(
                                                modifier = Modifier,
                                                text = MyDate.differenceOfDatesNoMultiple(
                                                   history.orderId,
                                                    System.currentTimeMillis().toString()
                                                ),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Text(
                                            text = foodDetails.foodName,
                                            modifier = Modifier,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            modifier = Modifier.fillMaxWidth(0.7f),
                                            text = foodDetails.foodDetails,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.outline
                                            ),
                                            maxLines = 3,
                                            minLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                        .align(Alignment.TopCenter),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Card(
                                        modifier = Modifier.clip(shape = CircleShape),
                                        colors = CardDefaults.cardColors(Color.Transparent),
                                        shape = CircleShape

                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.size(90.dp),
                                            contentScale = ContentScale.Crop,
                                            model = foodDetails.faceImgUrl,
                                            contentDescription = ""
                                        )
                                    }
                                    Card(
                                        modifier = Modifier.padding(end = 5.dp),
                                        elevation = CardDefaults.cardElevation(10.dp),
                                        colors = CardDefaults.cardColors(Color.Transparent)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            tint = if (true) Color(
                                                179, 5, 5
                                            ) else Color(179, 164, 164),
                                            contentDescription = "favourite"
                                        )
                                    }
                                }
                                Button(modifier = Modifier.align(Alignment.BottomEnd),
                                    shape = RoundedCornerShape(
                                        bottomEndPercent = 100,
                                        bottomStartPercent = 10,
                                        topStartPercent = 100
                                    ),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                                    onClick = {
                                        foodUrl = foodDetails.faceImgUrl
                                        foodId = foodDetails.foodId
                                        rateDialog = true
                                    }) {
                                    Text(
                                        text = "Rate this",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            })
        }
    })
}