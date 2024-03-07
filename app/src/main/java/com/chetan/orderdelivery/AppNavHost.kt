package com.chetan.orderdelivery

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chetan.orderdelivery.common.ApplicationAction
import com.chetan.orderdelivery.presentation.admin.addoffer.AdminAddOfferScreen
import com.chetan.orderdelivery.presentation.admin.addoffer.AdminAddOfferViewModel
import com.chetan.orderdelivery.presentation.admin.dashboard.AdminDashboardScreen
import com.chetan.orderdelivery.presentation.admin.dashboard.AdminDashboardViewModel
import com.chetan.orderdelivery.presentation.admin.editfood.EditFoodScreen
import com.chetan.orderdelivery.presentation.admin.editfood.EditFoodViewModel
import com.chetan.orderdelivery.presentation.admin.food.addfood.AddFoodScreen
import com.chetan.orderdelivery.presentation.admin.food.addfood.AddFoodViewModel
import com.chetan.orderdelivery.presentation.admin.food.ratingUpdate.RatingUpdateScreen
import com.chetan.orderdelivery.presentation.admin.food.ratingUpdate.RatingUpdateViewModel
import com.chetan.orderdelivery.presentation.admin.history.AdminHistoryScreen
import com.chetan.orderdelivery.presentation.admin.history.AdminHistoryViewModel
import com.chetan.orderdelivery.presentation.admin.notification.AdminNotificationScreen
import com.chetan.orderdelivery.presentation.admin.notification.AdminNotificationViewModel
import com.chetan.orderdelivery.presentation.admin.orderdetails.AdminOrderDetailScreen
import com.chetan.orderdelivery.presentation.admin.orderdetails.AdminOrderDetailViewModel
import com.chetan.orderdelivery.presentation.admin.sendnotice.AdminSendNoticeScreen
import com.chetan.orderdelivery.presentation.admin.sendnotice.AdminSendNoticeViewModel
import com.chetan.orderdelivery.presentation.common.google_sign_in.GoogleAuthUiClient
import com.chetan.orderdelivery.presentation.common.google_sign_in.SignInScreen
import com.chetan.orderdelivery.presentation.common.google_sign_in.SignInViewModel
import com.chetan.orderdelivery.presentation.common.utils.CleanNavigate.cleanNavigate
import com.chetan.orderdelivery.presentation.user.dashboard.UserDashboardScreen
import com.chetan.orderdelivery.presentation.user.dashboard.UserDashboardViewModel
import com.chetan.orderdelivery.presentation.user.foodorderdescription.FoodOrderDescriptionScreen
import com.chetan.orderdelivery.presentation.user.foodorderdescription.FoodOrderDescriptionViewModel
import com.chetan.orderdelivery.presentation.user.foodwithcategories.UserFoodCategoryScreen
import com.chetan.orderdelivery.presentation.user.foodwithcategories.UserFoodCategoryViewModel
import com.chetan.orderdelivery.presentation.user.later.MoreFoodScreen
import com.chetan.orderdelivery.presentation.user.morepopularfood.UserMoreScreen
import com.chetan.orderdelivery.presentation.user.morepopularfood.UserMoreViewModel
import com.chetan.orderdelivery.presentation.user.myorder.MyOrderScreen
import com.chetan.orderdelivery.presentation.user.myorder.MyOrderViewModel
import com.chetan.orderdelivery.presentation.user.notification.NotificationScreen
import com.chetan.orderdelivery.presentation.user.notification.NotificationViewModel
import com.chetan.orderdelivery.presentation.user.ordercheckout.OrderCheckoutScreen
import com.chetan.orderdelivery.presentation.user.ordercheckout.OrderCheckoutViewModel
import com.chetan.orderdelivery.presentation.user.profile.UserProfileScreen
import com.chetan.orderdelivery.presentation.user.profile.UserProfileViewModel
import com.chetan.orderdelivery.presentation.user.search.UserSearchScreen
import com.chetan.orderdelivery.presentation.user.search.UserSearchViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    onBack: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Screen.CommonSignInScreen.route
    ) {
        // common
        composable(Destination.Screen.CommonSignInScreen.route) {
            val viewModel = hiltViewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit, block = {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    println(googleAuthUiClient.getSignedInUser()!!.userEmail)
                    if (googleAuthUiClient.getSignedInUser()!!.userEmail == "momobarnextin@gmail.com") {
                        navController.cleanNavigate(Destination.Screen.AdminDashboardScreen.route)
                    } else {
                        navController.cleanNavigate(Destination.Screen.UserDashboardScreen.route)
                    }
                }
            })
            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == ComponentActivity.RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }

                    })

            LaunchedEffect(key1 = state.isSignInSuccessful, block = {
                if (state.isSignInSuccessful) {
                    viewModel.resetState()
                    if (googleAuthUiClient.getSignedInUser()!!.userEmail == "momobarnextin@gmail.com") {
                        navController.cleanNavigate(Destination.Screen.AdminDashboardScreen.route)
                    } else {
                        navController.cleanNavigate(Destination.Screen.UserDashboardScreen.route)
                    }

                }
            })
            SignInScreen(state = state) {
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        }


        // User
        composable(Destination.Screen.UserDashboardScreen.route) {
            val viewModel = hiltViewModel<UserDashboardViewModel>()
            UserDashboardScreen(onBack = onBack,
                navController = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onEvent = viewModel.onEvent,
                onAction = { applicationAction ->
                    when (applicationAction) {
                        ApplicationAction.Restart -> {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            applicationContext.startActivity(intent)
                        }

                        ApplicationAction.Logout -> {
                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                navController.cleanNavigate(Destination.Screen.CommonSignInScreen.route)
                            }
                        }
                    }
                })
        }

        composable(Destination.Screen.UserFoodOrderDescriptionScreen.route) { it ->
            val foodId = it.arguments?.getString("foodId")!!
            val viewModel = hiltViewModel<FoodOrderDescriptionViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            FoodOrderDescriptionScreen(
                foodId = foodId,
                navController = navController,
                onEvent = viewModel.onEvent,
                state = state
            )
        }

        composable(Destination.Screen.UserOrderCheckoutScreen.route) {
            val totalCost = it.arguments?.getString("totalCost")!!
            val viewModel = hiltViewModel<OrderCheckoutViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            OrderCheckoutScreen(
                totalCost = totalCost,
                navController = navController,
                onEvent = viewModel.onEvent,
                state = state
            )
        }

        composable(Destination.Screen.UserNotificationScreen.route) {
            val viewModel = hiltViewModel<NotificationViewModel>()
            NotificationScreen(
                nav = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                event = viewModel.onEvent
            )
        }
        composable(Destination.Screen.UserMoreFoodScreen.route) {
            MoreFoodScreen()
        }
        composable(Destination.Screen.UserMoreScreen.route) {
            val viewModel = hiltViewModel<UserMoreViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value
            val event = viewModel.onEvent
            UserMoreScreen(
                navController = navController, state = state, event = event
            )
        }
        composable(Destination.Screen.UserSearchScreen.route) {
            val viewModel = hiltViewModel<UserSearchViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value
            val event = viewModel.event
            UserSearchScreen(
                navController = navController, state = state, event = event
            )
        }
        composable(Destination.Screen.UserMyOrderScreen.route) {
            val viewModel = hiltViewModel<MyOrderViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value
            MyOrderScreen(
                navController = navController, state = state, event = viewModel.onEvent
            )
        }

        composable(Destination.Screen.UserProfileScreen.route) {
            val isCompleteBack = it.arguments?.getString("isCompleteBack")!!
            val viewModel = hiltViewModel<UserProfileViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value
            UserProfileScreen(
                nav = navController,
                state = state,
                event = viewModel.onEvent,
                isCompleteBack = isCompleteBack
            )
        }

        //Admin
        composable(Destination.Screen.AdminDashboardScreen.route) {
            val viewModel = hiltViewModel<AdminDashboardViewModel>()
            AdminDashboardScreen(navController = navController,
                onBack = onBack,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onEvent = viewModel.onEvent,
                onAction = { applicationAction ->
                    when (applicationAction) {
                        ApplicationAction.Restart -> {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            applicationContext.startActivity(intent)
                        }

                        ApplicationAction.Logout -> {
                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                navController.cleanNavigate(Destination.Screen.CommonSignInScreen.route)
                            }
                        }
                    }
                })
        }
        composable(Destination.Screen.AdminAddFoodScreen.route) {
            val viewModel = hiltViewModel<AddFoodViewModel>()
            AddFoodScreen(
                navController = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onEvent = viewModel.onEvent
            )
        }
        composable(Destination.Screen.AdminRatingUpdateScreen.route) {
            val viewModel = hiltViewModel<RatingUpdateViewModel>()
            RatingUpdateScreen(
                navController = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onEvent = viewModel.onEvent
            )
        }
        composable(Destination.Screen.AdminOrderDetailScreen.route) {
            val user = it.arguments?.getString("user")!!
            val viewModel = hiltViewModel<AdminOrderDetailViewModel>()
            AdminOrderDetailScreen(
                navController = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                event = viewModel.onEvent,
                user = user
            )
        }
        composable(Destination.Screen.AdminSendNoticeScreen.route) {
            val viewModel = hiltViewModel<AdminSendNoticeViewModel>()
            AdminSendNoticeScreen(
                nav = navController,
                event = viewModel.onEvent,
                state = viewModel.state.collectAsStateWithLifecycle().value
            )
        }
        composable(Destination.Screen.AdminEditFoodScreen.route) {
            val foodId = it.arguments?.getString("foodId")!!
            val viewModel = hiltViewModel<EditFoodViewModel>()
            EditFoodScreen(
                nav = navController,
                event = viewModel.onEvent,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                foodId = foodId
            )
        }

        composable(Destination.Screen.UserFoodCategoryScreen.route) {
            val viewModel = hiltViewModel<UserFoodCategoryViewModel>()
            UserFoodCategoryScreen(
                navController = navController,
                event = viewModel.event,
                state = viewModel.state.collectAsStateWithLifecycle().value,
            )
        }

        composable(Destination.Screen.AdminAddOfferScreen.route) {
            val viewModel = hiltViewModel<AdminAddOfferViewModel>()
            AdminAddOfferScreen(
                navController = navController,
                event = viewModel.onEvent,
                state = viewModel.state.collectAsStateWithLifecycle().value,
            )
        }

        composable(Destination.Screen.AdminNotificationScreen.route) {
            val viewModel = hiltViewModel<AdminNotificationViewModel>()
            AdminNotificationScreen(
                nav = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                event = viewModel.onEvent
            )
        }
        composable(Destination.Screen.AdminOrderHistory.route) {
            val viewModel = hiltViewModel<AdminHistoryViewModel>()
            AdminHistoryScreen(
                nav = navController,
                state = viewModel.state.collectAsStateWithLifecycle().value,
                event = viewModel.onEvent
            )
        }
    }
}