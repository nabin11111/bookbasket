package com.chetan.orderdelivery.presentation.user.notification

sealed interface NotificationEvent{
    data class ChangeToRead(val id: String): NotificationEvent
    data class DeleteNotification(val id: String): NotificationEvent
    data object DismissInfoMsg: NotificationEvent
    data object DeleteAll: NotificationEvent
}