package com.chetan.orderdelivery.presentation.user.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.Destination
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun UserSearchScreen(
    navController: NavHostController,
    state: UserSearchState,
    event: (onEvent: UserSearchEvent) -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.searchQuery, onValueChange = {
                    event(UserSearchEvent.OnQueryChange(it))
                }, leadingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier,
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    }

                }, trailingIcon = {
                    if (state.searchQuery.isBlank()) {

                    } else {
                        IconButton(onClick = {
                            event(UserSearchEvent.OnQueryCrossClicked)
                        }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier,
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }
                    }

                }, placeholder = {
                    Text(
                        text = "Search Food", color = MaterialTheme.colorScheme.outline
                    )
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ), keyboardActions = KeyboardActions(onSearch = {

                }), colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )

                )

            Divider()
            LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), content = {
                items(state.searchedList) { foodItem ->
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .width(150.dp)
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
                                        .size(110.dp),
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
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
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
                                style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                            )
                        }
                    }
                }
            })

        }

    }
}