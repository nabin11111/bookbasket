package com.chetan.orderdelivery

import android.app.Application
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.google.android.gms.ads.MobileAds
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


const val ONESIGNAL_APP_ID = "473febeb-3878-459e-a056-6e0f67ad6fb8"
@HiltAndroidApp
class OrderDeliveryApp : Application() {


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        MobileAds.initialize(this)
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
    }
}