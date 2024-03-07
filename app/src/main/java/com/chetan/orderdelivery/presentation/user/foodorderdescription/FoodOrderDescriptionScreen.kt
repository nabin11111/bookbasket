package com.chetan.orderdelivery.presentation.user.foodorderdescription

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.jobnepal.utils.ads.BannerAd
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.data.model.FavouriteModel
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.CleanNavigate.cleanNavigate
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun FoodOrderDescriptionScreen(
    onEvent: (event: FoodOrderDescriptionEvent) -> Unit,
    state: FoodOrderDescriptionState,
    navController: NavHostController,
    foodId: String
) {

    if (state.foodItemDetails.foodId.isBlank()) {
        onEvent(FoodOrderDescriptionEvent.GetFoodItemDetails(foodId))
    }
    var showProfileWarning by remember {
        mutableStateOf(false)
    }
    if (showProfileWarning) {
        AlertDialog(title = {
            Text(
                text = "Profile", style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }, text = {
            Text(text = "Please complete your profile.")
        }, onDismissRequest = {
            navController.popBackStack()
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
                Text(text = "Regretfully, we cannot assist you in this time.")
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


    val pagerImages = mutableListOf(
        state.foodItemDetails.supportImgUrl2,
        state.foodItemDetails.supportImgUrl3,
        state.foodItemDetails.supportImgUrl4,
    ).filter { it.isNotBlank() }

    val pagerState = rememberPagerState { pagerImages.size }
    val pageCount = pagerImages.size
    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
                Text(
                    text = "Food Description",
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

            }, actions = {
            }

            )

        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 5.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                state.infoMsg?.let {
                    MessageDialog(
                        message = it,
                        onDismissRequest = {
                            if (onEvent != null && state.infoMsg.isCancellable == true) {
                                onEvent(FoodOrderDescriptionEvent.DismissInfoMsg)
                            }
                        },
                        onPositive = { },
                        onNegative = {

                        })
                }
                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    if (pagerImages.size != 0) {
                        HorizontalPager(
                            state = pagerState
                        ) { page ->

                            Box(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .padding(horizontal = 10.dp)
                                        .clip(shape = RectangleShape),
                                    contentScale = ContentScale.Crop,
                                    model = pagerImages[page],
                                    contentDescription = "",
                                    alignment = Alignment.Center
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color = color)
                                    .size(20.dp)
                            )

                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(shape = CircleShape),
                            contentScale = ContentScale.Crop,
                            model = state.foodItemDetails.faceImgUrl,
                            contentDescription = "",
                        )
                        Text(
                            text = state.foodItemDetails.foodName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RatingBar(size = 15.dp,
                                value = state.foodItemDetails.foodRating,
                                spaceBetween = 2.dp,
                                style = RatingBarStyle.Default,
                                onValueChange = {

                                },
                                onRatingChanged = {

                                })
                            IconButton(modifier = Modifier
                                .padding(end = 5.dp),
                                onClick = {
                                    onEvent(
                                        FoodOrderDescriptionEvent.SetFavourite(
                                            foodId = state.foodItemDetails.foodId,
                                            isFav = !state.favouriteList.contains(
                                                FavouriteModel(state.foodItemDetails.foodId)
                                            )
                                        )
                                    )
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    tint = if (state.favouriteList.contains(FavouriteModel(state.foodItemDetails.foodId))) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                                    contentDescription = "favourite"
                                )
                            }
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "Rs. ${state.foodPrice * state.foodQuantity}",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Text(
                                    text = "Rs. ${state.foodDiscount * state.foodQuantity}",

                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.outline,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (state.foodQuantity > 1) {
                                    Card(
                                        modifier = Modifier.size(34.dp),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimaryContainer),
                                        elevation = CardDefaults.cardElevation(10.dp),
                                    ) {
                                        IconButton(
                                            onClick = {
                                                onEvent(FoodOrderDescriptionEvent.DecreaseQuantity)
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
                                    text = "${state.foodQuantity}",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Card(
                                    modifier = Modifier.size(34.dp),
                                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                    elevation = CardDefaults.cardElevation(10.dp),
                                ) {
                                    IconButton(onClick = {
                                        onEvent(FoodOrderDescriptionEvent.IncreaseQuantity)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Add, contentDescription = "Add"
                                        )
                                    }
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "About the food", style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = state.foodItemDetails.foodDetails,
                            style = MaterialTheme.typography.labelMedium.copy(MaterialTheme.colorScheme.outlineVariant)
                        )


                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                            onClick = {
                                if (state.phoneNo.isNotBlank()) {
                                    onEvent(FoodOrderDescriptionEvent.OrderFood)
                                    navController.navigate(
                                        Destination.Screen.UserOrderCheckoutScreen.route.replace(
                                            "{totalCost}",
                                            (state.foodItemDetails.foodNewPrice * state.foodQuantity).toString()
                                        )
                                    )
                                } else {
                                    showProfileWarning = true
                                }

                            },
                            enabled = state.deliveryState
                        ) {
                            Text(text = "Order Now")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            elevation = ButtonDefaults.buttonElevation(10.dp),
                            onClick = {
                                onEvent(FoodOrderDescriptionEvent.AddToCart(state.foodItemDetails.foodId))
                            },
                            enabled = true
                        ) {
                            Text(text = "Add to Basket")
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    BannerAd(
                        bId = "ca-app-pub-1412843616436423/1907426733",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        })
}

