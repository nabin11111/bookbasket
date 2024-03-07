package com.chetan.orderdelivery.presentation.admin.dashboard.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.presentation.common.components.requestpermission.RequestPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn( ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(state: AdminMapState, onEvent: (event: AdminMapEvent) -> Unit) {
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
    val cameraPositionState = com.google.maps.android.compose.rememberCameraPositionState()

    RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION) {
        canOrder = it
    }

    if (locationInfo.isNotBlank()) {
        LaunchedEffect(key1 = Unit, block = {
            val userlatlng = locationInfo.split(",")
            val position = CameraPosition.fromLatLngZoom(
                LatLng(
                    userlatlng.first().toDouble(), userlatlng.last().toDouble()
                ), 16f
            )
            cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(position))
            cameraPositionState.position = position
        })
    }

    var showUserDetails by remember {
        mutableStateOf(false)
    }
    if (showUserDetails){
        Dialog(
            onDismissRequest = {
            showUserDetails = false
        }) {
            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = CircleShape)
                        ,
                        model = state.userDetails.googleProfileUrl,
                        contentDescription = "")

                    Text(text = state.userDetails.userContactNo)
                }
            }


        }
    }


    Scaffold(modifier = Modifier, topBar = {}, bottomBar = {}, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            GoogleMap(modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = remember {
                    MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = true,
                        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.map_style
                        )
                    )
                },
                onMapClick = {

                },
                onMapLongClick = {

                }

            ) {

                state.orderedUserList.forEach { userData ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(userData.locationLat.toDouble(),userData.locationLng.toDouble()), ),
                        draggable = false,
                        title = userData.userName.ifBlank { userData.googleUserName },
                        snippet = userData.userContactNo,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                        onInfoWindowLongClick = {
                            val uri = Uri.parse("google.navigation:q=${userData.locationLat},${userData.locationLng}&origin=${locationInfo}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            // Start the navigation intent
                            context.startActivity(mapIntent)
                        },
                        onInfoWindowClick = {
                            onEvent(AdminMapEvent.OnClickWindoInfo(userData.userMail))
                                            showUserDetails = true
                        },
                        onClick = {
                            it.showInfoWindow()
                            true
                        })
                }
//                LaunchedEffect(destination) {
//                    if (destination.latitude != 0.0) {
//                        // Delay the marker update to ensure it occurs after the recomposition
//                        delay(100)
//                        markerState.position = destination
//                    }
//                }
//                if (destination.latitude != 0.0) {
//                    Marker(state = markerState,
//                        draggable = true,
//                        title = "Nepalgunj",
//                        snippet = "Nepalgunj Momo bar",
//                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
//                        onInfoWindowLongClick = {
////                                viewModel.onEvent(MapEvent.OnInfoWindowLongClick(parkingSpot))
//                        },
//                        onClick = {
//                            it.showInfoWindow()
//                            true
//                        })
//                }

            }
        }
    })

}