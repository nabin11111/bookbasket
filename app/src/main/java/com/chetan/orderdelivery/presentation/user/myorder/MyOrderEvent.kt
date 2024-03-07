package com.chetan.orderdelivery.presentation.user.myorder

sealed interface MyOrderEvent{
    data class CancelOrder(val value: String): MyOrderEvent
    data object DismissInfoMsg : MyOrderEvent
    data object GetFoodStatus: MyOrderEvent
}