package com.nabin.bookbasket.presentation.admin.dashboard.allFoods

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.nabin.bookbasket.Destination
import com.nabin.bookbasket.presentation.user.morepopularfood.FilterTypes
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun AllFoodScreen(
    nav: NavHostController,
    event: (event: AllBookEvent) -> Unit,
    state: AllBookState
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val filterList = listOf(
        FilterTypes.Name,
        FilterTypes.Rating,
        FilterTypes.PriceHigh,
        FilterTypes.PriceLow,
    )
    var selectedOption by remember {
        mutableStateOf(filterList[0])
    }
    Scaffold(
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = state.searchQuery,
                    onValueChange = {
                        event(AllBookEvent.OnQueryChange(it))
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                        }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier,
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        }

                    },
                    trailingIcon = {
                        if (state.searchQuery.isBlank()) {

                        } else {
                            IconButton(onClick = {
                                event(AllBookEvent.OnQueryCrossClicked)
                            }) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier,
                                    imageVector = Icons.Default.Close,
                                    contentDescription = ""
                                )
                            }
                        }

                    },
                    placeholder = {
                        Text(
                            text = "Search Book",
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {

                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )
                Box() {
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }) {
                        filterList.forEach { selectionOption ->
                            DropdownMenuItem(text = {
                                Text(text = selectionOption.name)
                            }, onClick = {
                                event(AllBookEvent.OnFilterChange(selectionOption))
                                selectedOption = selectionOption
                                expanded = false
                            })
                        }
                    }

                }

            }
            Divider()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                content = {
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
                                    .padding(vertical = 10.dp),
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
                                    text = foodItem.bookName,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        textAlign = TextAlign.Center
                                    ),
                                    maxLines = 1
                                )
                                RatingBar(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    size = 15.dp,
                                    value = foodItem.bookRating,
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
                                    text = "Rs ${foodItem.bookPrice}",
                                    modifier = Modifier.padding(horizontal = 15.dp),
                                    style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                                )
                            }
                            IconButton(modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 5.dp),
                                onClick = {
                                    nav.navigate(
                                        Destination.Screen.AdminEditFoodScreen.route.replace(
                                            "{foodId}", foodItem.bookId
                                        )
                                    )
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = "Edit"
                                )
                            }
                        }
                    }
                })

        }
    }
}