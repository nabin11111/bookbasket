package com.chetan.orderdelivery.presentation.user.dashboard

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.chetan.orderdelivery.presentation.common.components.LoadLottieAnimation
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.common.components.dialogs.PrivacyPolicyDialog
import com.chetan.orderdelivery.presentation.common.utils.BottomNavigate.bottomNavigate
import com.chetan.orderdelivery.presentation.user.dashboard.cart.UserCartScreen
import com.chetan.orderdelivery.presentation.user.dashboard.cart.UserCartViewModel
import com.chetan.orderdelivery.presentation.user.dashboard.favourite.UserFavouriteScreen
import com.chetan.orderdelivery.presentation.user.dashboard.favourite.UserFavouriteViewModel
import com.chetan.orderdelivery.presentation.user.dashboard.history.UserHistoryScreen
import com.chetan.orderdelivery.presentation.user.dashboard.history.UserHistoryViewModel
import com.chetan.orderdelivery.presentation.user.dashboard.home.UserHomeScreen
import com.chetan.orderdelivery.presentation.user.dashboard.home.UserHomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class UserInnerPage(
    val route: String,
    val label: Int,
    val icon: ImageVector,
    val count: String = "",
    val isBadge: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    onBack: () -> Unit,
    navController: NavHostController,
    state: UserDashboardState,
    onEvent: (event: UserDashboardEvent) -> Unit,
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
    var showDialog by remember {
        mutableStateOf(false)
    }
    if (showDialog) {
        PrivacyPolicyDialog(onDismiss = {
            showDialog = it
        })
    }
    if (showApplyThemeDialog) {
        Dialog(onDismissRequest = {}) {
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
                    modifier = Modifier.size(200.dp), image = R.raw.loading_food
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Please restart the app to apply the theme.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.height(34.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                        .also { Arrangement.Center }) {
                    Button(
                        modifier = Modifier.weight(1f), onClick = {
                            onAction(ApplicationAction.Restart)
                        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Restart")
                    }
                    Button(
                        modifier = Modifier.weight(1f), onClick = {
                            showApplyThemeDialog = false
                        }, colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer)
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
            onBack()
        }
    }
    val items: List<UserInnerPage> = remember {
        listOf(
            UserInnerPage("home", R.string.home, Icons.Default.Home),
            UserInnerPage("favourite", R.string.favourite, Icons.Default.Favorite),
            UserInnerPage("cart", R.string.cart, Icons.Default.ShoppingCart, isBadge = true),
            UserInnerPage("history", R.string.history, Icons.Default.History)
        )
    }
    val bottomNavController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        drawerContent = {
            UserDashboardModalDrawerPage(state = state, onClick = { menuItem ->
                when (menuItem) {
                    MenuItem.Contacts -> {

                    }

                    MenuItem.Logout -> {
                        onAction(ApplicationAction.Logout)
                    }

                    MenuItem.PrivacyPolicy -> {
                        showDialog = true
                    }

                    MenuItem.DarkMode -> {
                        showApplyThemeDialog = true
                        onEvent(UserDashboardEvent.ChangeDarkMode)
                    }

                    MenuItem.MyOrders -> {
                        navController.navigate(Destination.Screen.UserMyOrderScreen.route)
                    }
                    MenuItem.Profile -> {
                        navController.navigate(Destination.Screen.UserProfileScreen.route.replace(
                            "{isCompleteBack}","N"
                        ))
                    }
                }

            })
        }) {
        Scaffold(modifier = Modifier, topBar = {
            TopAppBar(modifier = Modifier.padding(horizontal = 10.dp), navigationIcon = {
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
            }, actions = {
                IconButton(onClick = {
                    navController.navigate(Destination.Screen.UserSearchScreen.route)
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")

                }
                Card(
                    modifier = Modifier
                        .size(34.dp)
                        .clickable {
                            navController.navigate(Destination.Screen.UserNotificationScreen.route)
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
            }, title = {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(
                        text = "MOMO BAR",
                        style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Next In",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            })
        }, bottomBar = {
            BottomAppBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                items.forEach { screen ->
                    val isSelected =
                        navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true
                    val color = if (isSelected) Color.White else MaterialTheme.colorScheme.outline

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
                                            text = if (screen.isBadge) "" else "",
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
                            label = {},
                            alwaysShowLabel = false
                        )
                    }


                }
            }
        }, content = {

            state.infoMsg?.let {
                MessageDialog(message = it, onDismissRequest = {
                    if (onEvent != null && state.infoMsg.isCancellable == true) {
                        onEvent(UserDashboardEvent.DismissInfoMsg)
                    }
                }, onPositive = { /*TODO*/ }) {

                }
            }
            NavHost(
                modifier = Modifier.padding(it),
                navController = bottomNavController,
                startDestination = "home"
            ) {
                composable("home") {
                    val viewModel = hiltViewModel<UserHomeViewModel>()
                    UserHomeScreen(
                        navController = navController,
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        event = viewModel.onEvent
                    )
                }
                composable("favourite") {
                    val viewModel = hiltViewModel<UserFavouriteViewModel>()
                    UserFavouriteScreen(
                        navController = navController,
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        event = viewModel.onEvent
                    )
                }
                composable("history") {
                    val viewModel = hiltViewModel<UserHistoryViewModel>()
                    UserHistoryScreen(
                        navController = navController,
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        event = viewModel.onEvent
                    )
                }
                composable("cart") {
                    val viewModel = hiltViewModel<UserCartViewModel>()
                    UserCartScreen(
                        navController = navController,
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        event = viewModel.onEvent
                    )
                }

            }
        })
    }

}
