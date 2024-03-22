package com.nabin.bookbasket.presentation.user.search

import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserSearchState(
    val allFoods : List<AllBooks> = emptyList(),
    val searchedList: List<AllBooks> = emptyList(),
    val searchQuery: String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)