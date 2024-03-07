package com.chetan.orderdelivery.presentation.user.dashboard.history

import com.chetan.orderdelivery.data.model.RatingRequestResponse
import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserHistoryState(
    val test: String = "",
    val ratingData: RatingRequestResponse = RatingRequestResponse(),
    val historyList : List<RequestFoodOrder> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
