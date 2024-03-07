package com.nabin.bookbasket.presentation.admin.dashboard.allFoods

import com.nabin.bookbasket.data.model.GetFoodResponse
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AllFoodState(
    val allFoods : List<GetFoodResponse> = emptyList(),
    val searchedList: List<GetFoodResponse> = emptyList(),
    val searchQuery: String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)