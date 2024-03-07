package com.chetan.orderdelivery.presentation.admin.notification

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.MyDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AdminNotificationScreen(
    nav: NavHostController, state: AdminNotificationState, event: (event: AdminNotificationEvent) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.padding(horizontal = 5.dp), title = {
            Text(
                text = "Notification",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }, navigationIcon = {
            IconButton(onClick = {
                nav.popBackStack()
            }) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier,
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = ""
                )
            }

        },)
    }, content = {
        state.infoMsg?.let {
            MessageDialog(message = it, onDismissRequest = {
                if (event != null && state.infoMsg.isCancellable == true) {
                    event(AdminNotificationEvent.DismissInfoMsg)
                }
            }, onPositive = { /*TODO*/ }) {

            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
                .fillMaxSize()
                .animateContentSize()
        ) {
            itemsIndexed(items = state.notificationList, key = { _, listItem ->
                listItem.hashCode()
            }) { index, item ->
                val dismissState = rememberDismissState(confirmValueChange = {
                    if (it == DismissValue.DismissedToEnd) {
                        event(AdminNotificationEvent.DeleteNotification(item.time))
                    }
                    true
                })
                SwipeToDismiss(modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    background = {
                        Box(
                            Modifier.fillMaxSize()
                        ) {
                            IconButton(modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 20.dp), onClick = {

                            }) {
                                Icon(

                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }

                        }
                    },
                    directions = setOf(DismissDirection.StartToEnd),
                    dismissContent = {
                        swipeItem(list = state.notificationList, itemIndex = index,event = event)
                    })
            }
        }


    })
}

@Composable
fun swipeItem(
    list: List<StoreNotificationRequestResponse>,
    itemIndex: Int,
    event: (event: AdminNotificationEvent) -> Unit
) {
    Box(modifier = Modifier.padding(bottom = 5.dp)) {
        Card(
            modifier = Modifier
                .padding(top = 12.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = list[itemIndex].title, style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = MyDate.differenceOfDatesNoMultiple(list[itemIndex].time,System.currentTimeMillis().toString()) , style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = list[itemIndex].body,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Icon(
            modifier = Modifier.padding(start = 5.dp),
            imageVector = if (list[itemIndex].readNotice) Icons.Default.NotificationsNone else Icons.Default.NotificationsActive,
            contentDescription = "notification"
        )
    }
}