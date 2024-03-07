package com.chetan.orderdelivery.presentation.user.myorder

import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class MyOrderState(
    val user: String = "",
    val orderDetails: List<RequestFoodOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)