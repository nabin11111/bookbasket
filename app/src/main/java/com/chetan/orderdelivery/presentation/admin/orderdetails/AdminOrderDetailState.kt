package com.chetan.orderdelivery.presentation.admin.orderdetails

import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class AdminOrderDetailState(
    val showInformDialog: Boolean = false,
    val msg: String = "Momobar Next In. Has started preparing your order.Our delivery executive will pick it up soon.",
    val user: String = "",
    val orderDetails: List<RequestFoodOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
