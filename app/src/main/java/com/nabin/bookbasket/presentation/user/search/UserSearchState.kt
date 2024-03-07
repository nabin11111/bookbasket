package com.nabin.bookbasket.presentation.user.search

import com.nabin.bookbasket.domain.model.AllFoods
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserSearchState(
    val allFoods : List<AllFoods> = emptyList(),
    val searchedList: List<AllFoods> = emptyList(),
    val searchQuery: String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)