package com.nabin.bookbasket.presentation.admin.orderdetails

import com.nabin.bookbasket.data.model.order.RequestFoodOrder
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminOrderDetailState(
    val showInformDialog: Boolean = false,
    val msg: String = "Book Basket. Has started preparing your order.Our delivery executive will pick it up soon.",
    val user: String = "",
    val orderDetails: List<RequestFoodOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
