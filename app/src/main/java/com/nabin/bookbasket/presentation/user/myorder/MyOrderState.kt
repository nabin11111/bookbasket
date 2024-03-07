package com.nabin.bookbasket.presentation.user.myorder

import com.nabin.bookbasket.data.model.order.RequestFoodOrder
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class MyOrderState(
    val user: String = "",
    val orderDetails: List<RequestFoodOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)