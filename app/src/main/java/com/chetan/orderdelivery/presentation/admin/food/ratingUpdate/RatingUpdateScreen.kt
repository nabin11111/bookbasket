package com.chetan.orderdelivery.presentation.admin.food.ratingUpdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chetan.orderdelivery.data.model.GetFoodResponse
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingUpdateScreen(
    navController: NavHostController,
    state: RatingUpdateState,
    onEvent: (event: RatingUpdateEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
                Text(
                    text = "Update Rating",
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
                .padding(it)
                .padding(horizontal = 5.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.infoMsg?.let {
                MessageDialog(
                    message = it,
                    onDismissRequest = {
                                       if (onEvent != null && state.infoMsg.isCancellable == true){
                                           onEvent(RatingUpdateEvent.DismissInfoMsg)
                                       }
                    },
                    onPositive = {  },
                    onNegative = {

                    })
            }
            state.foodList.forEach { foodItemDetails ->
                UpdateRatingItem(data = foodItemDetails,
                    onClick = { id,rating ->
                    onEvent(RatingUpdateEvent.UpdateThis(id,rating))
                })
            }


        }
    })
}

@Composable
fun UpdateRatingItem(
    modifier: Modifier = Modifier,
    data: GetFoodResponse,
    onClick: (String,Float) -> Unit,
) {
    var lastRatingValue by remember{
        mutableFloatStateOf(data.foodRating)
    }
    Card(
        modifier = modifier,
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.foodName, style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = data.foodFamily, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = data.foodType, style = MaterialTheme.typography.titleMedium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = lastRatingValue.toString(), style = MaterialTheme.typography.headlineSmall)
            Text(
                text = data.newFoodRating.toString(), style = MaterialTheme.typography.headlineSmall
            )
            Button(
                enabled = !data.newFoodRating.isNaN(),
                onClick = {
                lastRatingValue = data.newFoodRating
                onClick(data.foodId,data.newFoodRating)
            }) {
                Text(text = "Update")
            }
        }

    }
}
