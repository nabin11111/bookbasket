package com.chetan.orderdelivery.presentation.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.chetan.orderdelivery.presentation.common.components.requestpermission.RequestPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserLocation(): String {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
        context,
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
            },
                text = {
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
    return locationInfo
}