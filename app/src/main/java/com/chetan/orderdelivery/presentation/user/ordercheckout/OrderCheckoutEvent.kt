package com.chetan.orderdelivery.presentation.user.ordercheckout

import com.chetan.orderdelivery.presentation.user.dashboard.home.UserHomeEvent

sealed interface OrderCheckoutEvent{
    data class LocationAddress(val value: String): OrderCheckoutEvent
    data class Location(val value: String) : OrderCheckoutEvent
    data object OrderNow: OrderCheckoutEvent
    data object DismissInfoMsg: OrderCheckoutEvent
}