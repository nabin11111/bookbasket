package com.chetan.orderdelivery.presentation.user.foodwithcategories

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UserFoodCategoryScreen(
    navController: NavHostController,
    state: UserFoodCategoryState,
    event: (onEvent: UserFoodCategoryEvent) -> Unit,
) {
    val cardSize = remember {
        mutableFloatStateOf(0f)
    }
    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
                Text(
                    text = "All Foods",
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

            }, actions = {}

            )

        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
                .animateContentSize()
        ) {
            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (event != null && state.infoMsg.isCancellable == true) {
                        event(UserFoodCategoryEvent.DismissInfoMsg)
                    }
                }, onPositive = { /*TODO*/ }) {

                }
            }
            val colorDash = MaterialTheme.colorScheme.outline


            LazyColumn {
                items(state.foodTypesList) { foodType ->
                    var show by remember {
                        mutableStateOf(true)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = foodType,
                                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        IconButton(onClick = {
                            show = !show
                        }) {
                            Icon(
                                imageVector = if (show) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                    Column(modifier = Modifier
                        .padding(start = 10.dp)
                        .drawBehind {
                            drawLine(
                                color = colorDash,
                                start = Offset(50f, 0f),
                                end = Offset(size.width - 50, 0f),
                                strokeWidth = 5f
                            )

                            drawLine(
                                color = colorDash,
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height - 10),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 10f), 0f
                                ),
                                strokeWidth = 5f
                            )
                        }
                        .padding(horizontal = 15.dp)) {
                        if (show) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                state.allFoods.filter { it.foodFamily == foodType }
                                    .forEach { foodItem ->
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(140.dp)
                                                .height(205.dp),
                                        ) {
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(vertical = 10.dp)
                                                    .clickable {
                                                        navController.navigate(
                                                            Destination.Screen.UserFoodOrderDescriptionScreen.route.replace(
                                                                "{foodId}", foodItem.foodId
                                                            )
                                                        )
                                                    },
                                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
                                                elevation = CardDefaults.cardElevation(10.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    AsyncImage(
                                                        modifier = Modifier
                                                            .clip(CircleShape)
                                                            .size(100.dp),
                                                        model = foodItem.faceImgUrl,
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop
                                                    )
                                                }

                                                Text(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    text = foodItem.foodName,
                                                    style = MaterialTheme.typography.headlineSmall.copy(
                                                        textAlign = TextAlign.Center
                                                    ),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis

                                                )
                                                RatingBar(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    size = 15.dp,
                                                    value = foodItem.foodRating,
                                                    style = RatingBarStyle.Default,
                                                    onValueChange = {},
                                                    onRatingChanged = {},
                                                    numOfStars = 5,
                                                    spaceBetween = 1.dp
                                                )
                                            }
                                            Card(
                                                modifier = Modifier.align(Alignment.BottomCenter),
                                                elevation = CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                                            ) {
                                                Text(
                                                    text = "Rs ${foodItem.foodPrice}",
                                                    modifier = Modifier.padding(horizontal = 15.dp),
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        color = Color.White
                                                    )
                                                )
                                            }
                                        }
                                    }
                            }


//                            LazyVerticalGrid(
//                                modifier = Modifier
//                                    .height(LocalConfiguration.current.screenHeightDp.dp)
//                                    .fillMaxSize()
//                                ,
//                                columns = GridCells.Adaptive(160.dp), content = {
//                                    items(state.allFoods.filter { it.foodType == foodType }) { foodItem ->
//                                        Spacer(modifier = Modifier.height(5.dp))
//                                        Box(
//                                            modifier = Modifier
//                                                .padding(5.dp)
//                                                .width(150.dp)
//                                                .height(205.dp),
//                                        ) {
//                                            Card(
//                                                modifier = Modifier
//                                                    .fillMaxSize()
//                                                    .padding(vertical = 10.dp)
//                                                    .clickable {
//                                                        navController.navigate(
//                                                            Destination.Screen.UserFoodOrderDescriptionScreen.route.replace(
//                                                                "{foodId}", foodItem.foodId
//                                                            )
//                                                        )
//                                                    },
//                                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
//                                                elevation = CardDefaults.cardElevation(10.dp)
//                                            ) {
//                                                Row(
//                                                    modifier = Modifier
//                                                        .fillMaxWidth()
//                                                        .padding(10.dp),
//                                                    horizontalArrangement = Arrangement.Center
//                                                ) {
//                                                    AsyncImage(
//                                                        modifier = Modifier
//                                                            .clip(CircleShape)
//                                                            .size(110.dp),
//                                                        model = foodItem.faceImgUrl,
//                                                        contentDescription = null,
//                                                        contentScale = ContentScale.Crop
//                                                    )
//                                                }
//
//                                                Text(
//                                                    modifier = Modifier.fillMaxWidth(),
//                                                    text = foodItem.foodName,
//                                                    style = MaterialTheme.typography.headlineSmall.copy(
//                                                        textAlign = TextAlign.Center
//                                                    ),
//                                                    maxLines = 1
//                                                )
//                                                RatingBar(
//                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                                                    size = 15.dp,
//                                                    value = foodItem.foodRating,
//                                                    style = RatingBarStyle.Default,
//                                                    onValueChange = {},
//                                                    onRatingChanged = {},
//                                                    numOfStars = 5,
//                                                    spaceBetween = 1.dp
//                                                )
//                                            }
//                                            Card(
//                                                modifier = Modifier.align(Alignment.BottomCenter),
//                                                elevation = CardDefaults.cardElevation(10.dp),
//                                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
//                                            ) {
//                                                Text(
//                                                    text = "Rs ${foodItem.foodPrice}",
//                                                    modifier = Modifier.padding(horizontal = 15.dp),
//                                                    style = MaterialTheme.typography.titleSmall.copy(
//                                                        color = Color.White
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                })
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

        }
    }
}

