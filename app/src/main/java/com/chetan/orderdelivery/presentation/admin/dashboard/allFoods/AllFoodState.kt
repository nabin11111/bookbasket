package com.chetan.orderdelivery.presentation.admin.dashboard.allFoods

import com.chetan.orderdelivery.data.model.GetFoodResponse
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class AllFoodState(
    val allFoods : List<GetFoodResponse> = emptyList(),
    val searchedList: List<GetFoodResponse> = emptyList(),
    val searchQuery: String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)