package com.chetan.orderdelivery.presentation.admin.dashboard.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.MyDate
import com.chetan.orderdelivery.presentation.user.dashboard.cart.UserCartEvent
import com.chetan.orderdelivery.presentation.user.morepopularfood.UserMoreEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController, event: (event: AdminHomeEvent) -> Unit, state: AdminHomeState
) {
    val branchList = listOf(
        BranchType.ALL,
        BranchType.NPJ,
        BranchType.KLP
    )
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedOption by remember {
        mutableStateOf(branchList[0])
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember {
        mutableStateOf(false)
    }
    fun refresh() = refreshScope.launch {
        refreshing = true
        event(AdminHomeEvent.OnRefresh)
        refreshing = false
    }
    val refreshState = rememberPullRefreshState(refreshing, ::refresh)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        if (!refreshing){
            Scaffold(topBar = {}, content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 10.dp)
                        .fillMaxSize(),
                ) {
                    state.infoMsg?.let {
                        MessageDialog(message = it, onDismissRequest = {
                            if (event != null && state.infoMsg.isCancellable == true) {
                                event(AdminHomeEvent.DismissInfoMsg)
                            }
                        }, onPositive = { /*TODO*/ }) {

                        }
                    }

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
                            branchList.forEach { selectionOption ->
                                DropdownMenuItem(text = {
                                    Text(text = selectionOption.branch)
                                }, onClick = {
                                    event(AdminHomeEvent.OnFilterChange(selectionOption))
                                    selectedOption = selectionOption
                                    expanded = false
                                })
                            }
                        }

                    }
                    Divider()
                    LazyColumn(modifier = Modifier.fillMaxSize(), content = {
                        items(state.branchWiseList) { orders ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 15.dp, start = 50.dp)
                                        .clickable {
                                            navController.navigate(
                                                Destination.Screen.AdminOrderDetailScreen.route.replace(
                                                    "{user}", orders.userMail
                                                )
                                            )
                                        },
                                    elevation = CardDefaults.cardElevation(10.dp),
                                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(start = 60.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = orders.userName.ifBlank { orders.googleUserName },
                                                style = MaterialTheme.typography.headlineMedium.copy(

                                                )
                                            )
                                            Text(
                                                text = orders.locationAddress,
                                                maxLines = 2,
                                                minLines = 2,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MaterialTheme.colorScheme.outline,
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = MyDate.differenceOfDatesNoMultiple(orders.time,System.currentTimeMillis().toString()),
                                                maxLines = 1,
                                                minLines = 1,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = orders.userContactNo,
                                                maxLines = 1,
                                                minLines = 1,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                            }
                                        }
                                    }
                                }
                                Card(
                                    modifier = Modifier.align(Alignment.TopStart), shape = CircleShape
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .border(
                                                border = BorderStroke(
                                                    width = 2.dp, color = Color.White
                                                ), shape = CircleShape
                                            ),
                                        model = orders.dbProfileUrl.ifBlank { orders.googleProfileUrl },
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop

                                    )
                                }
                                IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
                                    event(AdminHomeEvent.RemoveUser(orders.userMail))
                                }) {
                                    Icon(
                                        tint = MaterialTheme.colorScheme.error,
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = ""
                                    )
                                }


                            }
                        }
                    })
                }
            })
        }
        PullRefreshIndicator(
            refreshing = refreshing, state = refreshState, modifier = Modifier.align(
                Alignment.TopCenter
            )
        )

    }


}

sealed class BranchType(val branch: String){
    data object ALL : BranchType("All")
    data object NPJ : BranchType("Npj")
    data object KLP : BranchType("Klp")
}
