package com.chetan.orderdelivery.presentation.admin.addoffer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddOfferScreen(
    navController: NavHostController,
    event: (event: AdminAddOfferEvent) -> Unit,
    state: AdminAddOfferState
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                event(AdminAddOfferEvent.OnImageUriToUrl(it))
            }

        })

    var alertDialog by remember {
        mutableStateOf(false)
    }
    if (alertDialog) {
        AlertDialog(title = {
            Text(
                text = "Add Offer", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
            text = {
                Text(text = "Are you sure?")
            }, onDismissRequest = {

            }, confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                    onClick = {
                        alertDialog = false
                        event(AdminAddOfferEvent.AddFood)
                    }) {
                    Text(text = "Confirm")
                }
            }, dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Red.copy(alpha = 0.7f)),
                    onClick = {
                        alertDialog = false
                    }) {
                    Text(text = "Cancel")
                }
            })
    }
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "Add Offer",
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
    },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                state.infoMsg?.let {
                    MessageDialog(
                        message = it,
                        onDismissRequest = {
                            if (event != null && state.infoMsg.isCancellable == true) {
                                event(AdminAddOfferEvent.DismissInfoMsg)
                            }
                        },
                        onPositive = { },
                        onNegative = {

                        })
                }

                Box(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            Color.Transparent
                        ),
                    ) {
                        Box {
                            Card(
                                modifier = Modifier.padding(top = 30.dp, end = 10.dp)
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp),
                                    model = state.faceImgUrl,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                            IconButton(modifier = Modifier.align(Alignment.TopEnd),
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = null,
                                    tint = Constants.dark_primaryContainer
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Button(modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                            onClick = {
                                alertDialog = true
                            }) {
                            Text(text = "Add", color = Color.White)
                        }


                    }
                }
            }
        })
}