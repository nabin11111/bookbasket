package com.chetan.orderdelivery.presentation.user.later

sealed interface MoreFoodEvent{
    data object Test: MoreFoodEvent
}