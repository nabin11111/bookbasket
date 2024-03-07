package com.chetan.orderdelivery.presentation.admin.dashboard

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chetan.orderdelivery.Destination
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.common.ApplicationAction
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.admin.dashboard.allFoods.AllFoodScreen
import com.chetan.orderdelivery.presentation.admin.dashboard.allFoods.AllFoodViewModel
import com.chetan.orderdelivery.presentation.admin.dashboard.home.AdminHomeViewModel
import com.chetan.orderdelivery.presentation.admin.dashboard.home.HomeScreen
import com.chetan.orderdelivery.presentation.admin.dashboard.map.AdminMapViewModel
import com.chetan.orderdelivery.presentation.admin.dashboard.map.MapScreen
import com.chetan.orderdelivery.presentation.common.components.LoadLottieAnimation
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.utils.BottomNavigate.bottomNavigate
import com.chetan.orderdelivery.presentation.common.utils.PlayNotificationSound
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class AdminInnerPage(
    val route: String, val label: Int, val icon: ImageVector, val isBadge: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
    navController: NavHostController,
    state: AdminDashboardState,
    onEvent: (event: AdminDashboardEvent) -> Unit,
    onAction: (ApplicationAction) -> Unit
) {
    var backPressCount by remember {
        mutableIntStateOf(0)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = backPressCount, block = {
        if (backPressCount == 1) {
            delay(2000)
            backPressCount = 0
        }
    })

    var showApplyThemeDialog by remember {
        mutableStateOf(false)
    }

    if (showApplyThemeDialog){
        Dialog(onDismissRequest = {
        }) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Action Needed",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
//            AsyncImage(
//                model = message.image,
//                modifier = Modifier.size(145.dp),
//                contentDescription = null
//            )

                LoadLottieAnimation(
                    modifier = Modifier.size(200.dp) ,
                    image = R.raw.loading_food)

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Please restart the app to apply the theme.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.height(34.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp).also { Arrangement.Center }){
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onAction(ApplicationAction.Restart)
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Restart")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showApplyThemeDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer)
                    ) {
                        Text(text = "Cancel")
                    }
                }

            }
        }
    }

    BackHandler {
        if (backPressCount == 0) {
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
            backPressCount++
        } else if (backPressCount == 1) {
            onBack() // Call your onBack function
        }
    }
    val items: List<AdminInnerPage> = remember {
        listOf(
            AdminInnerPage("home", R.string.home, Icons.Default.Home),
            AdminInnerPage("map", R.string.map, Icons.Default.LocationOn,true),
            AdminInnerPage("foods", R.string.foods, Icons.Default.Fastfood)
        )
    }

    val bottomNavController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // ui

    var totalNewRequest by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = state.newRequestList, block = {
        totalNewRequest = state.newRequestList.filter { it.item.newOrder }.size
        if (totalNewRequest >0){
            PlayNotificationSound(context)
        }

    })



    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        drawerContent = {
            AdminDashboardModalDrawerPage(
                state = state,
                onClick = {
                    when(it){
                        MenuItem.SendNotice -> {
                            navController.navigate(Destination.Screen.AdminSendNoticeScreen.route)
                        }
                        MenuItem.AddFoodItem -> {
                            navController.navigate(Destination.Screen.AdminAddFoodScreen.route)
                        }
                        MenuItem.UpdateRating -> {
                            navController.navigate(Destination.Screen.AdminRatingUpdateScreen.route)
                        }

                        MenuItem.AddOffer -> {
                            navController.navigate(Destination.Screen.AdminAddOfferScreen.route)
                        }

                        MenuItem.Logout -> {
                            onAction(ApplicationAction.Logout)
                        }
                        MenuItem.Setting -> {

                        }

                        MenuItem.DarkMode ->{
                            showApplyThemeDialog = true
                            onEvent(AdminDashboardEvent.ChangeDarkMode)
                        }

                        MenuItem.StopDelivery -> {
                            onEvent(AdminDashboardEvent.ChangeDeliveryState)
                        }

                        MenuItem.OrderHistory -> {
                            navController.navigate(Destination.Screen.AdminOrderHistory.route)
                        }
                    }
                })
        }) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    navigationIcon = {
                        Card(
                            modifier = Modifier.size(34.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            IconButton(
//                        colors = IconButtonDefaults.iconButtonColors(Color.White),
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                Icon(
                                    tint = Color.White,
                                    imageVector = Icons.Default.MenuOpen,
                                    contentDescription = "Menu"
                                )
                            }

                        }
                    },
                    actions = {
                        Card(
                            modifier = Modifier
                                .size(34.dp)
                                .clickable {
                                    navController.navigate(Destination.Screen.AdminNotificationScreen.route)
                                },
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                            elevation = CardDefaults.cardElevation(10.dp),
                        ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(20.dp),
                                imageVector = if (state.isNewNotification) Icons.Default.NotificationAdd else Icons.Default.Notifications,
                                tint = Color.White,
                                contentDescription = ""
                            )
                        }
                    }
                    },

                    title = {
                        Text(
                            text = "MOMO BAR",
                            style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    })
            },
            bottomBar = {
                BottomAppBar {
                    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                    items.forEach { screen ->
                        val isSelected =
                            navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true
                        val color =
                            if (isSelected) Color.White else MaterialTheme.colorScheme.outline
                        CompositionLocalProvider(LocalContentColor provides color) {
                            NavigationBarItem(
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                        LocalAbsoluteTonalElevation.current
                                    )
                                ),
                                icon = {
                                    Card(
                                        modifier = Modifier.size(34.dp),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                        elevation = CardDefaults.cardElevation(10.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(2.dp)
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .size(20.dp),
                                                imageVector = screen.icon,
                                                tint = color,
                                                contentDescription = ""
                                            )

                                            Text(
                                                text = if (screen.isBadge) totalNewRequest.toString() else "",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(end = 2.dp),
                                                fontSize = 8.sp,
                                                textAlign = TextAlign.Right,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                    }
                                },
                                selected = isSelected,
                                onClick = { bottomNavController.bottomNavigate(screen.route) },
                                label = {
                                },
                                alwaysShowLabel = false
                            )
                        }
                    }

                }
            },
            content = {
                state.infoMsg?.let {
                    MessageDialog(
                        message = it,
                        onDismissRequest = {
                            if (onEvent != null && state.infoMsg.isCancellable == true) {
                                onEvent(AdminDashboardEvent.DismissInfoMsg)
                            }
                        },
                        onPositive = { /*TODO*/ }) {

                    }
                }
                NavHost(
                    navController = bottomNavController,
                    startDestination = "home",
                    modifier = Modifier.padding(it)
                ) {
                    composable("home") {
                        val viewModel = hiltViewModel<AdminHomeViewModel>()
                        HomeScreen(
                            navController = navController,
                            event = viewModel.onEvent,
                            state = viewModel.state.collectAsStateWithLifecycle().value
                        )
                    }
                    composable("map") {
                        val viewModel = hiltViewModel<AdminMapViewModel>()
                        MapScreen(
                            state = viewModel.state.collectAsStateWithLifecycle().value,
                            onEvent = viewModel.onEvent
                        )
                    }
                    composable("foods"){
                        val viewModel = hiltViewModel<AllFoodViewModel>()
                        AllFoodScreen(
                            nav = navController,
                            event = viewModel.onEvent,
                            state = viewModel.state.collectAsStateWithLifecycle().value
                        )
                    }
                }
            })
    }

}