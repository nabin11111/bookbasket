package com.chetan.orderdelivery.presentation.admin.sendnotice

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.admin.food.addfood.OutlinedTextFieldAddFood
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSendNoticeScreen(
    event: (event: AdminSendNoticeEvent) -> Unit,
    state: AdminSendNoticeState,
    nav: NavHostController
) {

    var alertDialog by remember {
        mutableStateOf(false)
    }
    if (alertDialog) {
        AlertDialog(title = {
            Text(
                text = "Add Food", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(text = "Are you sure?")
        }, onDismissRequest = {

        }, confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                onClick = {
                    alertDialog = false
                    event(AdminSendNoticeEvent.SendNotice)
                }) {
                Text(text = "Confirm")
            }
        }, dismissButton = {
            Button(colors = ButtonDefaults.buttonColors(Color.Red.copy(alpha = 0.7f)), onClick = {
                alertDialog = false
            }) {
                Text(text = "Cancel")
            }
        })
    }
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "Send Notice",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }, navigationIcon = {
            IconButton(onClick = {
                nav.popBackStack()
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
                .padding(5.dp)
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (event != null && state.infoMsg.isCancellable == true) {
                        event(AdminSendNoticeEvent.DismissInfoMsg)
                    }
                }, onPositive = { }, onNegative = {

                })
            }

            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextFieldAddFood(
                modifier = Modifier.fillMaxWidth(),
                foodLabel = "Title",
                foodValue = state.title,
                onFoodValueChange = {
                    event(AdminSendNoticeEvent.OnTitleChange(it))
                },
                foodMaxLine = 2
            )
            OutlinedTextFieldAddFood(
                modifier = Modifier.fillMaxWidth(),
                foodLabel = "Notice",
                foodValue = state.notice,
                onFoodValueChange = {
                    event(AdminSendNoticeEvent.OnNoticeChange(it))
                },
                foodMaxLine = 3
            )

            Spacer(modifier = Modifier.height(50.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                onClick = {
                    alertDialog = true
                }) {
                Text(text = "Send", color = Color.White)
            }

        }
    })
}
