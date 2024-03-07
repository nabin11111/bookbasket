package com.chetan.orderdelivery.presentation.user.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.admin.food.addfood.OutlinedTextFieldAddFood
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.CleanNavigate.cleanNavigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    nav: NavHostController,
    state: UserProfileState,
    event: (event: UserProfileEvent) -> Unit,
    isCompleteBack: String
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
                    event(UserProfileEvent.UpdateProfile)
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
    BackHandler {
        if (isCompleteBack == "Y"){
            nav.cleanNavigate(Destination.Screen.UserDashboardScreen.route)
        }
        else{
            nav.popBackStack()
        }

    }
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }, navigationIcon = {
            IconButton(onClick = {
                if (isCompleteBack == "Y"){
                    nav.cleanNavigate(Destination.Screen.UserDashboardScreen.route)
                }
                else{
                    nav.popBackStack()
                }
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
                        event(UserProfileEvent.DismissInfoMsg)
                    }
                }, onPositive = { }, onNegative = {

                })
            }

            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextFieldAddFood(
                modifier = Modifier.fillMaxWidth(),
                foodLabel = "Full Name",
                foodValue = state.name,
                onFoodValueChange = {
                    event(UserProfileEvent.OnNameChange(it))
                },
                foodMaxLine = 1
            )
            OutlinedTextFieldAddFood(
                modifier = Modifier.fillMaxWidth(),
                foodLabel = "Phone No.",
                foodValue = state.phoneNo,
                onFoodValueChange = {
                    event(UserProfileEvent.OnPhoneChange(it))
                },
                foodMaxLine = 1,
                foodKeyboard = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextFieldAddFood(
                modifier = Modifier.fillMaxWidth(),
                foodLabel = "Address",
                foodValue = state.address,
                onFoodValueChange = {
                    event(UserProfileEvent.OnAddressChange(it))
                },
                foodMaxLine = 1
            )

            Spacer(modifier = Modifier.height(50.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                onClick = {
                    alertDialog = true
                }) {
                Text(text = "Update", color = Color.White)
            }

        }
    })
}