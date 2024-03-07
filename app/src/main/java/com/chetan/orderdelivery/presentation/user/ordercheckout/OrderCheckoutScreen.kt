package com.chetan.orderdelivery.presentation.user.ordercheckout

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.chetan.orderdelivery.presentation.common.components.LoadLottieAnimation
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.components.requestpermission.RequestPermission
import com.chetan.orderdelivery.presentation.user.dashboard.home.UserHomeEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun OrderCheckoutScreen(
    navController: NavHostController,
    onEvent: (event: OrderCheckoutEvent) -> Unit,
    state: OrderCheckoutState,
    totalCost: String
) {

    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }
    var openMap by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.cameraLocation, 18f)
    }

    val ctx = LocalContext.current
    val mGeocoder = Geocoder(ctx, Locale.getDefault())

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(ctx)
    }

    val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    var locationInfo by remember {
        mutableStateOf("")
    }
    var canOrder by remember {
        mutableStateOf(false)
    }
    var hideDialog by remember {
        mutableStateOf(false)
    }
    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION, permissionGranted = {
        canOrder = it
    })
    val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    val requestCode = 0 // You can choose a unique request code
    val pendingIntent = PendingIntent.getActivity(
        ctx,
        requestCode,
        locationSettingsIntent,
        PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE to comply with Android S+
    )
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
                Text(text = "Please enable GPS for Ordering food.")
            }, onDismissRequest = {

            }, confirmButton = {
                Button(onClick = {
                    pendingIntent.send()
                    hideDialog = true
                }) {
                    Text(text = "Enable GPS")
                }
            })
        } else {
            LaunchedEffect(key1 = Unit, block = {
                scope.launch(Dispatchers.IO) {
                    val priority = Priority.PRIORITY_HIGH_ACCURACY
                    val result =
                        locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                            .await()
                    result?.let { currentLocation ->
                        locationInfo = "${currentLocation.latitude},${currentLocation.longitude}"

                    }
                }
            })
        }
    }
    if (canOrder && isGpsEnabled && locationInfo.isBlank()) {
        AlertDialog(title = {

        }, icon = {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "")
        }, text = {
            Text(text = "Location Updating...")
        }, onDismissRequest = {

        }, confirmButton = {

        })
    }
    if (showConfirmDialog) {
        Dialog(onDismissRequest = { showConfirmDialog = false }, content = {
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
                    Text(
                        text = "Confirm Order", style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = state.locationAddress,
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
                    )

                    LoadLottieAnimation(
                        modifier = Modifier.size(200.dp), image = R.raw.order_now
                    )


                    Text(
                        text = buildAnnotatedString {
                            append("Total Rs. ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.error
                                )
                            ) {
                                append(totalCost)
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "After confirming we will again call and confirm it. After confirming we will manage to deliver this item as soon as possible.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Justify
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            onClick = {
                                onEvent(OrderCheckoutEvent.OrderNow)
                                showConfirmDialog = false
                                navController.navigate(Destination.Screen.UserDashboardScreen.route)
                            },
                        ) {
                            Text(text = "Confirm", color = Color.White)
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                            onClick = {
                                showConfirmDialog = false
                            },
                        ) {
                            Text(text = "Cancel", color = Color.White)
                        }
                    }
                }
            }
        })
    }
    if (!openMap) {
        LaunchedEffect(key1 = Unit, block = {
            if (!cameraPositionState.isMoving) {
                try {
                    onEvent(OrderCheckoutEvent.Location("${cameraPositionState.position.target.latitude},${cameraPositionState.position.target.longitude}"))
                    val addressList = mGeocoder.getFromLocation(
                        cameraPositionState.position.target.latitude,
                        cameraPositionState.position.target.longitude,
                        5
                    )
                    if (addressList != null && addressList.isNotEmpty()) {
                        val address = addressList[2].getAddressLine(0)
                        onEvent(OrderCheckoutEvent.LocationAddress(address))
                    }
                } catch (e: IOException) {
                    Toast.makeText(ctx, "Unable connect to Geocoder", Toast.LENGTH_LONG).show()
                }
            }


        })
    }

    if (openMap) {
        Dialog(onDismissRequest = { }, content = {
            Column(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.padding(5.dp)
                ) {
                    GoogleMap(modifier = Modifier.size(screenWidth),
                        cameraPositionState = cameraPositionState,
                        properties = remember {
                            MapProperties(
                                mapType = MapType.NORMAL, isMyLocationEnabled = true
                            )
                        })
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                openMap = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimary),
                    enabled = true
                ) {
                    Text(text = "Update", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(modifier = Modifier.height(5.dp))

            }

        })
    }
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "Check Out",
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
                .padding(it)
                .fillMaxSize()
        ) {

            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (onEvent != null && state.infoMsg.isCancellable == true) {
                        onEvent(OrderCheckoutEvent.DismissInfoMsg)
                    }
                }, onPositive = { /*TODO*/ }) {

                }
            }


            Divider()
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = if (canOrder){
                        state.distance + "  "+ state.locationAddress
                    }else "Sorry we cannot give delivery here", modifier = Modifier.weight(1f))
                    LoadLottieAnimation(modifier = Modifier
                        .clickable {
                        openMap = true
                    }.height(80.dp), image = R.raw.location_up_down)
//                    IconButton(onClick = {
//
//                    }) {
//                        Icon(
//                            imageVector = Icons.Default.LocationOn,
//                            contentDescription = "location"
//                        )
//                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 15.dp, start = 5.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                state.orderList.forEach { food ->
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 15.dp, start = 50.dp),
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
                                        style = MaterialTheme.typography.headlineMedium.copy(),
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
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp),
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
                                                    fontSize = 18.sp
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
                                        Text(
                                            text = "Qty: ${food.quantity}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Card(
                            modifier = Modifier.align(Alignment.TopStart), shape = CircleShape
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(100.dp)
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            )
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Items Total",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Rs. $totalCost",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Delivery Fee",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Rs. 0",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                        }

                        Divider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Payment",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Rs. $totalCost",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
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
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold

                        )
                    ) {
                        append("Rs")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(totalCost)
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
                        append(state.orderList.sumOf { it.foodPrice.toInt() * it.quantity }
                            .toString())
                    }
                })
                Button(
                    enabled = state.canOrder,
                    onClick = {
                        showConfirmDialog = true
                    }, shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Order Now (${state.orderList.size})")
                }
            }

        }
    }

    )
}
