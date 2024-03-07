package com.chetan.orderdelivery.presentation.user.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chetan.orderdelivery.R

@Composable
fun UserDashboardModalDrawerPage(
    onClick: (MenuItem) -> Unit,
    state: UserDashboardState,
) {
    val topMenuList = listOf(
        MenuItem.Profile,
        MenuItem.MyOrders
    )
    val bottomMenuList = listOf(
        MenuItem.Contacts, MenuItem.PrivacyPolicy, MenuItem.Logout
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(shape = RoundedCornerShape(
                    topStart = 0.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp
                ))
                .background(color = Color.Transparent)
        ) {
            Image(
                modifier = Modifier.fillMaxSize().padding(bottom = 20.dp),
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "banner",
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
                    .align(Alignment.BottomStart),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(width = 2.dp, color = Color.White), shape = CircleShape)
                       ,
                    model = state.profileUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )}

        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            topMenuList.forEach { menuItem ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(menuItem)
                        }, shape = RoundedCornerShape(5.dp)
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
        bottomMenuList.forEach { item ->
            ElevatedCard(
                modifier = Modifier.clickable {
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
    data object Profile: MenuItem(icon = Icons.Default.Person, label = "Profile")
    data object MyOrders : MenuItem(icon = Icons.Default.Fastfood, label = "My Orders")
    data object DarkMode : MenuItem(icon = Icons.Default.DarkMode, label = "Dark Mode")
    data object Contacts : MenuItem(icon = Icons.Default.Contacts, label = "Contacts")
    data object PrivacyPolicy : MenuItem(icon = Icons.Default.PrivacyTip, label = "Privacy & Policy")
    data object Logout : MenuItem(icon = Icons.Default.Logout, label = "LogOut")
}
