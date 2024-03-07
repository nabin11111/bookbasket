package com.chetan.orderdelivery.presentation.user.dashboard.cart

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.common.components.LoadLottieAnimation
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.CleanNavigate.cleanNavigate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserCartScreen(
    navController: NavHostController, state: UserCartState, event: (event: UserCartEvent) -> Unit
) {
    var showProfileWarning by remember {
        mutableStateOf(false)
    }
    if (showProfileWarning) {
        AlertDialog(title = {
            Text(
                text = "Note", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(text = "Please complete your profile.")
        }, onDismissRequest = {

        }, confirmButton = {
            Button(onClick = {
                navController.navigate(Destination.Screen.UserProfileScreen.route.replace(
                    "{isCompleteBack}",
                    "Y"
                ))
            }) {
                Text(text = "Complete Profile")
            }
        },
            dismissButton = {
                Button(onClick = {
                    showProfileWarning = false
                }) {
                    Text(text = "Later")
                }
            })
    }

    if (!state.deliveryStateShowDialog){
        AlertDialog(title = {
            Text(
                text = "Profile", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
            text = {
            Text(text = "Regretfully, we cannot assist you with your order.")
        },
            onDismissRequest = {

        },
            confirmButton = {
            Button(onClick = {
                navController.cleanNavigate(Destination.Screen.UserDashboardScreen.route)
            }) {
                Text(text = "Okay")
            }
        },
            )
    }

    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    if (!isGpsEnabled) {
        val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val requestCode = 0 // You can choose a unique request code
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            locationSettingsIntent,
            PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE to comply with Android S+
        )
        pendingIntent.send()
    }

    var isShowAlertDialog by remember {
        mutableStateOf(false)
    }
    if (isShowAlertDialog) {
        Dialog(onDismissRequest = {}) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Delete from cart",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
//            AsyncImage(
//                model = message.image,
//                modifier = Modifier.size(145.dp),
//                contentDescription = null
//            )

                LoadLottieAnimation(
                    modifier = Modifier.size(200.dp), image = R.raw.delete
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Are you sure want to delete item(s)?",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.height(34.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                        .also { Arrangement.Center }) {
                    Button(
                        modifier = Modifier.weight(1f), onClick = {
                            event(UserCartEvent.DeleteItems)
                            isShowAlertDialog = false
                        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Yes")
                    }
                    Button(
                        modifier = Modifier.weight(1f), onClick = {
                            isShowAlertDialog = false
                        }, colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer)
                    ) {
                        Text(text = "No")
                    }
                }

            }
        }
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember {
        mutableStateOf(false)
    }
    fun refresh() = refreshScope.launch {
        refreshing = true
        event(UserCartEvent.OnRefresh)
        refreshing = false
    }
    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        if (!refreshing){
            Scaffold(content = {
                state.infoMsg?.let {
                    MessageDialog(message = it, onDismissRequest = {
                        if (event != null && state.infoMsg.isCancellable == true) {
                            event(UserCartEvent.DismissInfoMsg)
                        }
                    }, onPositive = { /*TODO*/ }) {

                    }
                }
                Column(modifier = Modifier.padding(it)) {
                    if (state.cartItemList.isNotEmpty()){
                        Row(
                            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(checked = state.cartItemList.size == state.cartItemList.filter { it.isSelected }.size,
                                    onCheckedChange = {
                                        event(UserCartEvent.SelectAllCheckBox(it))
                                    })
                                Text(text = "Select All")
                            }
                            Button(shape = RoundedCornerShape(10.dp), onClick = {
                                isShowAlertDialog = true
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                                Text(text = "Delete")
                            }

                        }
                    }
                    Divider()
                    LazyColumn(modifier = Modifier
                        .weight(1f)
                        .padding(end = 15.dp, start = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        content = {
                            items(state.cartItemList) { food ->
                                Row(
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = food.isSelected, onCheckedChange = {
                                        event(
                                            UserCartEvent.ItemSelected(
                                                isItemSelected = it, item = food.foodId
                                            )
                                        )

                                    })
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(top = 15.dp, start = 40.dp)
                                                .clickable {
                                                    navController.navigate(
                                                        Destination.Screen.UserFoodOrderDescriptionScreen.route.replace(
                                                            "{foodId}", food.foodId
                                                        )
                                                    )
                                                },
                                            elevation = CardDefaults.cardElevation(10.dp),
                                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(start = 60.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = food.foodName,
                                                        style = MaterialTheme.typography.headlineMedium.copy(
                                                        ),
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = food.foodDetails,
                                                        maxLines = 2,
                                                        minLines = 2,
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            color = MaterialTheme.colorScheme.outline,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Row(
                                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                                            verticalAlignment = Alignment.Bottom
                                                        ) {
                                                            Text(
                                                                text = "${food.foodNewPrice * food.quantity}",
                                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                                    color = Color.Red,
                                                                    fontWeight = FontWeight.Bold,
                                                                    fontSize = 16.sp
                                                                )
                                                            )
                                                            Text(
                                                                text = "${food.foodPrice.toInt() * food.quantity}",
                                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                                    color = MaterialTheme.colorScheme.outline,
                                                                    fontWeight = FontWeight.SemiBold,
                                                                    textDecoration = TextDecoration.LineThrough
                                                                )
                                                            )
                                                        }

                                                        Row(
                                                            modifier = Modifier.padding(
                                                                end = 10.dp, bottom = 5.dp
                                                            ),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                        ) {
                                                            if (food.quantity > 1) {
                                                                Card(
                                                                    modifier = Modifier.size(24.dp),
                                                                    colors = CardDefaults.cardColors(
                                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                                                    ),
                                                                    elevation = CardDefaults.cardElevation(
                                                                        10.dp
                                                                    ),
                                                                ) {
                                                                    IconButton(onClick = {
                                                                        event(
                                                                            UserCartEvent.DecreaseQuantity(
                                                                                foodId = food.foodId,
                                                                                food.quantity
                                                                            )
                                                                        )
                                                                    }) {
                                                                        Icon(
                                                                            imageVector = Icons.Default.Remove,
                                                                            contentDescription = "Remove",
                                                                            tint = Color.White
                                                                        )
                                                                    }
                                                                }
                                                            }

                                                            Text(
                                                                text = "${food.quantity}",
                                                                style = MaterialTheme.typography.headlineSmall
                                                            )
                                                            Card(
                                                                modifier = Modifier.size(24.dp),
                                                                colors = CardDefaults.cardColors(
                                                                    MaterialTheme.colorScheme.primary
                                                                ),
                                                                elevation = CardDefaults.cardElevation(10.dp),
                                                            ) {
                                                                IconButton(onClick = {
                                                                    event(
                                                                        UserCartEvent.IncreaseQuantity(
                                                                            foodId = food.foodId,
                                                                            food.quantity
                                                                        )
                                                                    )
                                                                }) {
                                                                    Icon(
                                                                        imageVector = Icons.Default.Add,
                                                                        contentDescription = "Add"
                                                                    )
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Card(
                                            modifier = Modifier.align(Alignment.TopStart),
                                            shape = CircleShape
                                        ) {
                                            AsyncImage(
                                                modifier = Modifier
                                                    .size(90.dp)
                                                    .border(
                                                        border = BorderStroke(
                                                            width = 2.dp, color = Color.White
                                                        ), shape = CircleShape
                                                    ),
                                                model = food.faceImgUrl,
                                                contentDescription = "",
                                                contentScale = ContentScale.Crop

                                            )
                                        }

                                    }
                                }

                            }
                        })
                    Divider()
                    if (state.cartItemList.filter { it.isSelected }.isNotEmpty()){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.outline,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Delivery: Rs 0\n")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Total: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Red, fontSize = 18.sp, fontWeight = FontWeight.SemiBold

                                    )
                                ) {
                                    append("Rs ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Red, fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append(state.cartItemList.filter { it.isSelected }
                                        .sumOf { it.foodNewPrice * it.quantity }.toString())
                                }

                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.outline, fontSize = 12.sp

                                    )
                                ) {
                                    append("Rs ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.outline,
                                        fontSize = 12.sp,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                ) {
                                    append(state.cartItemList.filter { it.isSelected }.sumOf { it.foodPrice.toInt() * it.quantity }.toString())
                                }

                            })
                            Button(
                                onClick = {
                                    if (state.phoneNo.isNotBlank()) {
                                        event(UserCartEvent.Checkout)
                                        navController.navigate(Destination.Screen.UserOrderCheckoutScreen.route.replace(
                                            "{totalCost}", state.cartItemList.filter { it.isSelected }
                                                .sumOf { it.foodNewPrice * it.quantity }.toString()
                                        ))
                                    }else{
                                        showProfileWarning = true
                                    }

                                }, shape = RoundedCornerShape(10.dp),
                                enabled = state.deliveryState
                            ) {
                                Text(text = "Check out(${state.cartItemList.filter { it.isSelected }.size})")

                            }
                        }
                        Divider()
                    }


                }
            })
        }
        PullRefreshIndicator(
            refreshing = refreshing, state = refreshState, modifier = Modifier.align(
                Alignment.TopCenter
            )
        )

    }


}