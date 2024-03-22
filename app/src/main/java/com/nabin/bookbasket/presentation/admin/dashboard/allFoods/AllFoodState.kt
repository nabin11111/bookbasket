package com.nabin.bookbasket.presentation.admin.dashboard.allFoods

import com.nabin.bookbasket.data.model.GetBookResponse
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AllFoodState(
    val allBooks : List<GetBookResponse> = emptyList(),
    val searchedList: List<GetBookResponse> = emptyList(),
    val searchQuery: String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)