package com.chetan.orderdelivery.presentation.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardModalDrawerPage(
    onClick: (MenuItem) -> Unit,
    state: AdminDashboardState,
) {

    val menuList = listOf(
        MenuItem.AddFoodItem,
        MenuItem.UpdateRating,
        MenuItem.SendNotice,
        MenuItem.AddOffer,
        MenuItem.OrderHistory

    )
    val bottomMenuItem = listOf(
        MenuItem.Setting,
        MenuItem.Logout
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 20.dp,
                bottomEnd = 20.dp,
                bottomStart = 20.dp
            ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            menuList.forEach { menuItem ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(menuItem)
                        },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = menuItem.icon,
                                contentDescription = menuItem.label,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = menuItem.label,
                                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "arrow right",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }


                }
                Spacer(modifier = Modifier.height(5.dp))

            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        ElevatedCard(
            modifier = Modifier,
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = MenuItem.StopDelivery.icon,
                        contentDescription = MenuItem.StopDelivery.label,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = MenuItem.StopDelivery.label,
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
                Switch(
                    checked = state.changeDeliveryState, onCheckedChange = {
                        onClick(MenuItem.StopDelivery)
                    }, colors = SwitchDefaults.colors(
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
        ElevatedCard(
            modifier = Modifier,
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = MenuItem.DarkMode.icon,
                        contentDescription = MenuItem.DarkMode.label,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = MenuItem.DarkMode.label,
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
                Switch(
                    checked = state.darkMode, onCheckedChange = {
                        onClick(MenuItem.DarkMode)
                    }, colors = SwitchDefaults.colors(
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        bottomMenuItem.forEach {item ->
            ElevatedCard(
                modifier = Modifier
                    .clickable {
                        onClick(item)
                    },
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }


    }
}


sealed class MenuItem(val icon: ImageVector, val label: String) {
    data object AddFoodItem : MenuItem(icon = Icons.Default.LocalPizza, label = "Add Food Item")
    data object UpdateRating : MenuItem(icon = Icons.Default.StarRate, label = "Update Rating")
    data object SendNotice : MenuItem(icon = Icons.Default.NotificationAdd, label = "Send Notice")
    data object OrderHistory: MenuItem(icon = Icons.Default.History, label = "Order History")
    data object AddOffer : MenuItem(icon = Icons.Default.FreeBreakfast, label = "Add Offer")
    data object DarkMode : MenuItem(icon = Icons.Default.DarkMode, label = "Dark Mode")
    data object StopDelivery : MenuItem(icon = Icons.Default.DeliveryDining, label = "Delivery")
    data object Setting : MenuItem(icon = Icons.Default.Settings, label = "Setting")
    data object Logout : MenuItem(icon = Icons.Default.Logout, label = "LogOut")
}