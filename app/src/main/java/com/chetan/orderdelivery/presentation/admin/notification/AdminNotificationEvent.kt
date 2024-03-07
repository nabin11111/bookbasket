package com.chetan.orderdelivery.presentation.admin.notification

sealed interface AdminNotificationEvent{
    data class DeleteNotification(val id: String): AdminNotificationEvent
    data object DismissInfoMsg: AdminNotificationEvent

}