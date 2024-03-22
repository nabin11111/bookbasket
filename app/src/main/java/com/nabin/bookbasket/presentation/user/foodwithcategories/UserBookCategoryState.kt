package com.nabin.bookbasket.presentation.user.foodwithcategories

import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserBookCategoryState(
    val allBooks : List<AllBooks> = emptyList(),
    val bookTypesList: List<String> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)