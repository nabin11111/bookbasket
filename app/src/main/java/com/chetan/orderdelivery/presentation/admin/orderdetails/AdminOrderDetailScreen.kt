package com.chetan.orderdelivery.presentation.admin.orderdetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.admin.editfood.EditFoodEvent
import com.chetan.orderdelivery.presentation.admin.food.addfood.OutlinedTextFieldAddFood
import com.chetan.orderdelivery.presentation.common.components.LoadLottieAnimation
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.components.requestpermission.RequestPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AdminOrderDetailScreen(
    navController: NavHostController,
    state: AdminOrderDetailState,
    event: (event: AdminOrderDetailEvent) -> Unit,
    user: String
) {

    var test by remember { mutableStateOf(0.01f) }
    val targetValue = 0.7f
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var hideDialog by remember {
        mutableStateOf(false)
    }

    var locationInfo by remember {
        mutableStateOf("")
    }
    var canOrder by remember {
        mutableStateOf(false)
    }
    var orderId by remember {
        mutableStateOf("")
    }

    if (state.showInformDialog) {
        Dialog(
            onDismissRequest = {

            }) {
            Card(modifier = Modifier.padding(5.dp)) {
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextFieldAddFood(
                    modifier = Modifier.fillMaxWidth(),
                    foodLabel = "Say sth to customer",
                    foodValue = state.msg,
                    onFoodValueChange = {
                        event(AdminOrderDetailEvent.OnMessageChange(it))
                    },
                    foodMaxLine = 2
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            event(AdminOrderDetailEvent.OnShowHideMsgDialog(false))
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            event(AdminOrderDetailEvent.OnMessageSend(orderId))
                        }) {
                        Text(text = "Send")
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }

    if (canOrder) {
        if (!isGpsEnabled && !hideDialog) {
            AlertDialog(title = {
                Text(
                    text = "Enable GPS", style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            }, text = {
                Text("Please enable GPS to use this app.")
            }, onDismissRequest = { }, confirmButton = {
                Button(onClick = {
                    // Redirect the user to GPS settings
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                    hideDialog = true
                }) {
                    Text("Enable GPS")
                }
            })
        } else {
            LaunchedEffect(key1 = canOrder, block = {
                scope.launch(Dispatchers.IO) {
                    val priority = Priority.PRIORITY_HIGH_ACCURACY
                    val result =
                        locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                            .await()
                    result?.let { fetchedLocation ->
                        locationInfo = "${fetchedLocation.latitude},${fetchedLocation.longitude}"
                    }
                }
            })
        }
    }
    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION) {
        canOrder = it
    }

    LaunchedEffect(targetValue) {
        test = targetValue
    }

    val animatedSize by animateFloatAsState(
        targetValue = test, animationSpec = tween(
            durationMillis = 2000, delayMillis = 100
        ), label = ""
    )

    state.user.ifBlank {
        event(AdminOrderDetailEvent.GetOrderDetails(user))
    }


    var showAlert by remember {
        mutableStateOf(false)
    }
    if (showAlert) {
        AlertDialog(title = {
            Text(
                text = "Food Delivery", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
            text = {
                Text(text = "Is Delivered?")
            }, onDismissRequest = {

            }, confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                    onClick = {
                        showAlert = false
                        event(AdminOrderDetailEvent.Delivered(orderId))
                    }) {
                    Text(text = "Confirm")
                }
            }, dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Red.copy(alpha = 0.7f)),
                    onClick = {
                        showAlert = false
                    }) {
                    Text(text = "Cancel")
                }
            })
    }

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "Order Details",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier,
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }

        })
    }, content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (event != null && state.infoMsg.isCancellable == true) {
                        event(AdminOrderDetailEvent.DismissInfoMsg)
                    }
                }, onPositive = { }, onNegative = {

                })
            }
            if (state.orderDetails.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(animatedSize),
                        colors = CardDefaults.cardColors(Color.Blue),
                        shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(shape = CircleShape)
                                    .background(color = Color.White)
                                    .border(
                                        border = BorderStroke(
                                            width = 2.dp, color = Color.White
                                        )
                                    ),
                                model = state.orderDetails.first().googleProfileUrl,
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = state.orderDetails.first().googleUserName)
                        Row {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "")
                            Text(text = state.orderDetails.first().branch)
                        }
                    }
                }
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = state.orderDetails.size.toString(),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Items", style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .height(50.dp)
                                .width(2.dp)
                        )
                        Divider(
                            modifier = Modifier
                                .height(50.dp)
                                .width(2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    state.orderDetails.forEach { orders ->
                        Box {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp)
                                    .padding(top = 40.dp), colors = CardDefaults.cardColors()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = orders.dateTime,
                                                style = MaterialTheme.typography.headlineMedium
                                            )

                                            Text(
                                                text = orders.distance,
                                                style = MaterialTheme.typography.headlineMedium
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            IconButton(onClick = {
                                                orderId = orders.orderId
                                                event(AdminOrderDetailEvent.OnShowHideMsgDialog(true))
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Message,
                                                    contentDescription = ""
                                                )

                                            }
                                            Column(
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = orders.orderList.sumOf { it.quantity * it.foodNewPrice }
                                                        .toString(),
                                                    style = MaterialTheme.typography.headlineMedium
                                                )
                                                Text(
                                                    text = orders.orderList.sumOf { it.quantity * it.foodPrice.toInt() }
                                                        .toString(),
                                                    style = MaterialTheme.typography.headlineSmall.copy(
                                                        textDecoration = TextDecoration.LineThrough,
                                                        color = MaterialTheme.colorScheme.outline
                                                    )
                                                )
                                            }
                                        }

                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Order Details",
                                        textAlign = TextAlign.Left,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Card(
                                        modifier = Modifier.clickable {
                                            orderId = orders.orderId
                                            showAlert = true
                                        }, colors = CardDefaults.cardColors(Color.Transparent)
                                    ) {
                                        LoadLottieAnimation(
                                            modifier = Modifier.size(80.dp), image = R.raw.delivered
                                        )
                                    }
                                }
                                orders.orderList.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(
                                                text = item.foodName,
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                            Text(
                                                text = item.quantity.toString() + " * " + item.foodNewPrice.toString(),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            )
                                        }
                                        Text(
                                            text = (item.quantity * item.foodNewPrice).toString(),
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                    Divider(modifier = Modifier.padding(horizontal = 20.dp))
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = (100 - animatedSize * 100).dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(modifier = Modifier.size(60.dp), onClick = {
                                    val uri =
                                        Uri.parse("google.navigation:q=${orders.locationLat},${orders.locationLng}&origin=${locationInfo}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    // Start the navigation intent
                                    context.startActivity(mapIntent)


                                }) {
                                    Icon(
                                        modifier = Modifier.size(60.dp),
                                        imageVector = Icons.Default.DeliveryDining,
                                        contentDescription = "",
                                        tint = Color.Blue
                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    })
}
