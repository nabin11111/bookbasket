package com.chetan.orderdelivery.presentation.user.dashboard.history


sealed interface UserHistoryEvent{
    data class RateIt(val id: String, val url: String, val value: Float): UserHistoryEvent
    data class DeleteMyHistory(val id: String): UserHistoryEvent
    data object DismissInfoMsg : UserHistoryEvent
}