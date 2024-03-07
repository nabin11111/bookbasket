package com.nabin.bookbasket.presentation.user.dashboard.history

import com.nabin.bookbasket.data.model.RatingRequestResponse
import com.nabin.bookbasket.data.model.order.RequestFoodOrder
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserHistoryState(
    val test: String = "",
    val ratingData: RatingRequestResponse = RatingRequestResponse(),
    val historyList : List<RequestFoodOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
