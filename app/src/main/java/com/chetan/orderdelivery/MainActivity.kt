package com.chetan.orderdelivery

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.presentation.common.google_sign_in.GoogleAuthUiClient
import com.chetan.orderdelivery.service.NotificationServiceExtension
import com.chetan.orderdelivery.ui.theme.OrderDeliveryTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preference: Preference

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by remember {
                mutableStateOf(preference.isDarkMode)

            }
            OrderDeliveryTheme(darkTheme = isDarkMode.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
//                    val viewModel = hiltViewModel<UserHomeViewModel>()
//                    UserHomeScreen(
//                        navController = navController,
//                        state = viewModel.state.collectAsStateWithLifecycle().value,
//                        event = viewModel.onEvent
//
//                    )
                    AppNavHost(
                        navController = navController,
                        onBack = {
                            println("I am called")
                            finish()
                        },
                        googleAuthUiClient,
                        lifecycleScope,
                        applicationContext
                    )
                }
            }
        }
    }

}
