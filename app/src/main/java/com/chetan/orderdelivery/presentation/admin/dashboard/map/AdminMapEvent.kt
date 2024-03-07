package com.chetan.orderdelivery.presentation.admin.dashboard.map

sealed interface AdminMapEvent{
    data class OnClickWindoInfo(val userMail: String): AdminMapEvent
}