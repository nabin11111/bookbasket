package com.chetan.orderdelivery.presentation.admin.food.ratingUpdate

import com.chetan.orderdelivery.data.model.GetFoodResponse
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class RatingUpdateState(
    val item: String = "",
    val foodList : List<GetFoodResponse> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
