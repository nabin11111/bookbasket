package com.chetan.orderdelivery.presentation.admin.editfood

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.admin.food.addfood.ExposedDropdownMenuBoxOrderDelivery
import com.chetan.orderdelivery.presentation.admin.food.addfood.OutlinedTextFieldAddFood
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodScreen(
    nav: NavHostController,
    event: (event: EditFoodEvent) -> Unit,
    state: EditFoodState,
    foodId: String
) {
    if (state.foodItemDetails.foodId.isBlank()){
        event(EditFoodEvent.GetFoodItemDetails(foodId))
    }
    var imageName by remember {
        mutableIntStateOf(1)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                event(EditFoodEvent.OnImageUriToUrl(it, name = imageName))
            }

        })

    var alertDialog by remember {
        mutableStateOf(false)
    }
    if (alertDialog) {
        AlertDialog(title = {
            Text(
                text = "Update Food", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(text = "Are you sure?")
        }, onDismissRequest = {

        }, confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                onClick = {
                    alertDialog = false
                    event(EditFoodEvent.AddFood)
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
                text = "Edit Food",
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
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (event != null && state.infoMsg.isCancellable == true) {
                        event(EditFoodEvent.DismissInfoMsg)
                    }
                }, onPositive = { }, onNegative = {

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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (state.foodName.isNotBlank()) {
                            Box {
                                Card(
                                    modifier = Modifier.padding(top = 30.dp, end = 10.dp),
                                    shape = CircleShape
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.size(100.dp),
                                        model = state.faceImgUrl.imageUrl,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = {
                                    imageName = 1
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
                        }

                        Column(
                            modifier = Modifier.padding(end = 10.dp),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            ExposedDropdownMenuBoxOrderDelivery(
                                label = "Food Family",
                                items = state.foodFamilies,
                                selectedItem = { foodFamily ->
                                    event(EditFoodEvent.OnSelectedFoodFamilyChange(foodFamily))
                                },
                                itemValue = state.selectedFoodFamily
                            )
                            ExposedDropdownMenuBoxOrderDelivery(
                                label = "Food Type",
                                items = state.foodTypes,
                                selectedItem = { foodType ->
                                    event(EditFoodEvent.OnSelectedFoodTypeChange(foodType))
                                },
                                itemValue = state.selectedFoodType
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextFieldAddFood(
                        modifier = Modifier.fillMaxWidth(),
                        foodLabel = "Food Name",
                        foodValue = state.foodName,
                        onFoodValueChange = {
                            event(EditFoodEvent.OnFoodNameChange(it))
                        },
                        foodMaxLine = 2
                    )
                    OutlinedTextFieldAddFood(
                        modifier = Modifier.fillMaxWidth(),
                        foodLabel = "Food Details",
                        foodValue = state.foodDetails,
                        onFoodValueChange = {
                            event(EditFoodEvent.OnFoodDetailsChange(it))
                        },
                        foodMaxLine = 3
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextFieldAddFood(
                            modifier = Modifier.weight(1f),
                            foodLabel = "Price",
                            foodValue = state.foodPrice,
                            onFoodValueChange = {
                                event(EditFoodEvent.OnFoodPriceChange(it))
                            },
                            foodMaxLine = 1,
                            foodKeyboard = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        OutlinedTextFieldAddFood(
                            modifier = Modifier.weight(1f),
                            foodLabel = "Discount",
                            foodValue = state.foodDiscountPrice,
                            onFoodValueChange = {
                                event(EditFoodEvent.OnFoodDiscountChange(it))
                            },
                            foodMaxLine = 1,
                            foodKeyboard = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                    if (state.foodName.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box {
                                Card(
                                    modifier = Modifier.padding(top = 30.dp, end = 10.dp),
                                    shape = CircleShape
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.size(100.dp),
                                        model = state.supportImgUrl2.imageUrl,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = {
                                    imageName = 2
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
                            Box {
                                Card(
                                    modifier = Modifier.padding(top = 30.dp, end = 10.dp),
                                    shape = CircleShape
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.size(100.dp),
                                        model = state.supportImgUrl3.imageUrl,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = {
                                    imageName = 3
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
                            Box {
                                Card(
                                    modifier = Modifier.padding(top = 30.dp, end = 10.dp),
                                    shape = CircleShape
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.size(100.dp),
                                        model = state.supportImgUrl4.imageUrl,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                IconButton(modifier = Modifier.align(Alignment.TopEnd), onClick = {
                                    imageName = 4
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
                        }
                    }


                    Spacer(modifier = Modifier.height(50.dp))
                    Button(modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer),
                        onClick = {
                            alertDialog = true
                        }) {
                        Text(text = "Update", color = Color.White)
                    }


                }
            }
        }
    })
}