package com.nabin.bookbasket.presentation.user.later

sealed interface MoreFoodEvent{
    data object Test: MoreFoodEvent
}