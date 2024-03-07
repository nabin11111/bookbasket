package com.chetan.orderdelivery.service

import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.di.HiltEntryPoint
import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationServiceExtension
    : INotificationServiceExtension {

    @Inject
    lateinit var preference: Preference
    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        preference = Preference(event.context)
        preference.isNewNotification = true
        if (event.notification.body != null) {
            try {
                val hiltEntryPoint =
                    EntryPointAccessors.fromApplication(event.context, HiltEntryPoint::class.java)
                val firestoreUseCases = hiltEntryPoint.firestoreUseCases()
                CoroutineScope(SupervisorJob()).launch {
                    firestoreUseCases.setNotification(
                        StoreNotificationRequestResponse(
                            body = event.notification.body?:"",
                            title = event.notification.title?:"",
                            time = event.notification.sentTime.toString(),
                            readNotice = false,
                        )
                    )
                }
            } catch (e: Exception) {

            }
        }


    }
}