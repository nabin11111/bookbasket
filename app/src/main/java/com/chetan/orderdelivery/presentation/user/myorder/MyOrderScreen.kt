package com.chetan.orderdelivery.presentation.user.myorder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chetan.orderdelivery.presentation.admin.orderdetails.AdminOrderDetailEvent
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrderScreen(
    navController: NavHostController, state: MyOrderState, event: (event: MyOrderEvent) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "My Order",
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

        }, actions = {})

    }, content = {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (event != null && state.infoMsg.isCancellable == true) {
                        event(MyOrderEvent.DismissInfoMsg)
                    }
                }, onPositive = { }, onNegative = {

                })
            }

            state.orderDetails.forEach { orders ->
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        , colors = CardDefaults.cardColors()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = orders.dateTime,
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                Text(
                                    text = orders.distance,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = orders.orderId,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(text = orders.orderList.sumOf { it.quantity * it.foodNewPrice }
                                        .toString(),
                                        style = MaterialTheme.typography.headlineMedium)
                                    Text(text = orders.orderList.sumOf { it.quantity * it.foodPrice.toInt() }
                                        .toString(),
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            textDecoration = TextDecoration.LineThrough,
                                            color = MaterialTheme.colorScheme.outline
                                        ))
                                }
                            }

                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Order Details",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(onClick = { event(MyOrderEvent.GetFoodStatus)}) {
                            Icon(imageVector = Icons.Default.DeliveryDining, contentDescription ="" )
                        }
                        Button(
                            onClick = {
                                      event(MyOrderEvent.CancelOrder(orders.orderId))
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                            Text(text = "Cancel ")
                        }

                    }
                    orders.orderList.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = item.foodName,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = item.quantity.toString() + " * " + item.foodNewPrice.toString(),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                            Text(
                                text = (item.quantity * item.foodNewPrice).toString(),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Divider(modifier = Modifier.padding(horizontal = 20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
            Spacer(modifier = Modifier.height(15.dp))


        }
    })
}