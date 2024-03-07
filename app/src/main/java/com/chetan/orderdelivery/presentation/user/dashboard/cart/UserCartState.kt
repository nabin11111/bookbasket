package com.chetan.orderdelivery.presentation.user.dashboard.cart

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserCartState(
    val deliveryState: Boolean = false,
    val deliveryStateShowDialog: Boolean = true,
    val phoneNo : String = "",
    val cartItemList: List<AllFoods> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)