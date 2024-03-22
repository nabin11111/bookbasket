package com.nabin.bookbasket.presentation.user.dashboard.favourite

import com.nabin.bookbasket.data.model.RealtimeModelResponse
import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserFavouriteState(
    val name: List<RealtimeModelResponse> = emptyList(),
    val allFoods : List<AllBooks> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
