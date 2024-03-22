package com.nabin.bookbasket.presentation.user.dashboard.history

import com.nabin.bookbasket.data.model.RatingRequestResponse
import com.nabin.bookbasket.data.model.order.RequestBookOrder
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserHistoryState(
    val test: String = "",
    val ratingData: RatingRequestResponse = RatingRequestResponse(),
    val historyList : List<RequestBookOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
