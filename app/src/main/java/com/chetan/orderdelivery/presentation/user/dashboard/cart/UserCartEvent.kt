package com.chetan.orderdelivery.presentation.user.dashboard.cart

import com.chetan.orderdelivery.presentation.user.dashboard.home.UserHomeEvent


sealed interface UserCartEvent{
    data object DismissInfoMsg: UserCartEvent
    data object DeleteItems: UserCartEvent
    data class IncreaseQuantity(val foodId: String, val lastValue: Int) : UserCartEvent
    data class DecreaseQuantity(val foodId: String, val lastValue: Int) : UserCartEvent
    data class SelectAllCheckBox(val isChecked: Boolean) : UserCartEvent
    data class ItemSelected(val isItemSelected: Boolean, val item: String) : UserCartEvent
    data object Checkout: UserCartEvent
    data object OnRefresh : UserCartEvent
}