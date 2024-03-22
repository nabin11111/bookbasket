package com.nabin.bookbasket.presentation.user.dashboard.cart

import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserCartState(
    val deliveryState: Boolean = false,
    val deliveryStateShowDialog: Boolean = true,
    val phoneNo : String = "",
    val cartItemList: List<AllBooks> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)