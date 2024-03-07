package com.chetan.orderdelivery.presentation.user.dashboard.favourite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.common.Constants
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserFavouriteScreen(
    navController: NavHostController,
    state: UserFavouriteState,
    event: (event: UserFavouriteEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val listOfHeader = listOf(
        "Food", "Drinks"
    )

    val pagerState = rememberPagerState(initialPage = 0) {
        2
    }
    Scaffold(content = {
        val cardSize = remember {
            mutableFloatStateOf(0f)
        }
        Column(modifier = Modifier.padding(it)) {
            TabRow(selectedTabIndex = pagerState.currentPage, divider = {}) {
                listOfHeader.forEachIndexed { index, page ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(
                            text = page,
                            style = if (pagerState.currentPage == index) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineSmall
                        )
                    })
                }


            }
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        LazyColumn(modifier = Modifier.fillMaxSize(), content = {
                            items(state.allFoods.filter { it.foodType != "Drinks" }) { foodDetails ->
                                Box(modifier = Modifier.padding(horizontal = 25.dp)) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp, bottom = 10.dp, end = 20.dp)
                                            .height(150.dp)
                                            .onGloballyPositioned {
                                                cardSize.value = Size(
                                                    it.size.width.toFloat(),
                                                    it.size.height.toFloat()
                                                ).width
                                            },
                                        shape = RoundedCornerShape(topStart = cardSize.value / 4),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                        elevation = CardDefaults.cardElevation(10.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 10.dp),
                                            verticalArrangement = Arrangement.Bottom
                                        ) {
                                                Text(
                                                    text = foodDetails.foodName,
                                                    modifier = Modifier,
                                                    style = MaterialTheme.typography.headlineSmall.copy(
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                )

                                            Text(
                                                modifier = Modifier.fillMaxWidth(0.7f),
                                                text = foodDetails.foodDetails,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.outline
                                                ),
                                                maxLines = 3,
                                                minLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }


                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp)
                                            .align(Alignment.TopCenter),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Card(
                                            modifier = Modifier.clip(shape = CircleShape),
                                            colors = CardDefaults.cardColors(Color.Transparent),
                                            shape = CircleShape

                                        ) {
                                            AsyncImage(
                                                modifier = Modifier.size(100.dp),
                                                contentScale = ContentScale.Crop,
                                                model = foodDetails.faceImgUrl,
                                                contentDescription = ""
                                            )
                                        }
                                        Column(modifier = Modifier.padding(end = 5.dp)) {
                                            IconButton(
                                                onClick = {
                                                    event(
                                                        UserFavouriteEvent.RemoveFavourite(id = foodDetails.foodId)
                                                    )
                                                }) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    tint = MaterialTheme.colorScheme.error,
                                                    contentDescription = "favourite"
                                                )
                                            }
                                            RatingBar(
                                                size = 15.dp,
                                                value = 4f,
                                                style = RatingBarStyle.Fill(activeColor = Color.Green),
                                                onValueChange = {},
                                                onRatingChanged = {},
                                                numOfStars = 5,
                                                spaceBetween = 1.dp
                                            )
                                        }
                                    }
                                    Button(modifier = Modifier.align(Alignment.BottomEnd),
                                        shape = RoundedCornerShape(
                                            bottomEndPercent = 100,
                                            bottomStartPercent = 10,
                                            topStartPercent = 100
                                        ),
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                                        onClick = {
                                            navController.navigate(
                                                Destination.Screen.UserFoodOrderDescriptionScreen.route.replace(
                                                    "{foodId}", foodDetails.foodId
                                                )
                                            )
                                        }) {
                                        Text(
                                            text = "Details",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                }
                            }

                        })
                    }

                    1 -> {
                        LazyColumn(modifier = Modifier.fillMaxSize(), content = {
                            items(state.allFoods.filter { it.foodType == "Drinks" }) { foodDetails ->
                                Box(modifier = Modifier.padding(horizontal = 25.dp)) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp, bottom = 10.dp, end = 20.dp)
                                            .height(150.dp)
                                            .onGloballyPositioned {
                                                cardSize.value = Size(
                                                    it.size.width.toFloat(),
                                                    it.size.height.toFloat()
                                                ).width
                                            },
                                        shape = RoundedCornerShape(topStart = cardSize.value / 4),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                        elevation = CardDefaults.cardElevation(10.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 10.dp),
                                            verticalArrangement = Arrangement.Bottom
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = foodDetails.foodName,
                                                    modifier = Modifier,
                                                    style = MaterialTheme.typography.headlineSmall.copy(
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                )
                                                RatingBar(
                                                    size = 15.dp,
                                                    value = 4f,
                                                    style = RatingBarStyle.Default,
                                                    onValueChange = {},
                                                    onRatingChanged = {},
                                                    numOfStars = 5,
                                                    spaceBetween = 1.dp
                                                )
                                            }

                                            Text(
                                                modifier = Modifier.fillMaxWidth(0.7f),
                                                text = foodDetails.foodDetails,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.outline
                                                ),
                                                maxLines = 3,
                                                minLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }


                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp)
                                            .align(Alignment.TopCenter),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Card(
                                            modifier = Modifier.clip(shape = CircleShape),
                                            colors = CardDefaults.cardColors(Color.Transparent),
                                            shape = CircleShape

                                        ) {
                                            AsyncImage(
                                                modifier = Modifier.size(100.dp),
                                                contentScale = ContentScale.Crop,
                                                model = Constants.bottle,
                                                contentDescription = ""
                                            )
                                        }

                                        IconButton(
                                            modifier = Modifier.padding(end = 5.dp),
                                            onClick = {
                                                event(
                                                    UserFavouriteEvent.RemoveFavourite(id = foodDetails.foodId)
                                                )
                                            }) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                tint = MaterialTheme.colorScheme.error,
                                                contentDescription = "favourite"
                                            )
                                        }

                                    }
                                    Button(modifier = Modifier.align(Alignment.BottomEnd),
                                        shape = RoundedCornerShape(
                                            bottomEndPercent = 100,
                                            bottomStartPercent = 10,
                                            topStartPercent = 100
                                        ),
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                                        onClick = {

                                        }) {
                                        Text(
                                            text = "Details",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                }
                            }
                        })
                    }

                }

            }

        }
    })

}