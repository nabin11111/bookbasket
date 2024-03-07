package com.chetan.orderdelivery.presentation.user.morepopularfood

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.Destination
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserMoreScreen(
    navController: NavHostController,
    state: UserMoreState,
    event: (event: UserMoreEvent) -> Unit
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
        topBar = {
            TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
                Text(
                    text = "Popular Foods",
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

        },

    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize())
        {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                TextField(
                    value = state.searchQuery,
                    onValueChange = {
                        event(UserMoreEvent.OnQueryChange(it))
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
                                event(UserMoreEvent.OnQueryCrossClicked)
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
                            text = "Search Food",
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
                Box(

                ) {
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "" )
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
                                event(UserMoreEvent.OnFilterChange(selectionOption))
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
sealed class FilterTypes(val name: String){
    data object Name :FilterTypes("Name")
    data object Rating :FilterTypes("Rating")
    data object PriceHigh :FilterTypes("Price High")
    data object PriceLow :FilterTypes("Price Low")
}


