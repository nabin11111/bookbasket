package com.chetan.orderdelivery.presentation.user.morepopularfood

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserMoreState(
    val allFoods : List<AllFoods> = emptyList(),
    val searchedList: List<AllFoods> = emptyList(),
    val searchQuery: String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)