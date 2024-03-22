package com.nabin.bookbasket.presentation.admin.food.ratingUpdate

import com.nabin.bookbasket.data.model.GetBookResponse
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class RatingUpdateState(
    val item: String = "",
    val foodList : List<GetBookResponse> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
