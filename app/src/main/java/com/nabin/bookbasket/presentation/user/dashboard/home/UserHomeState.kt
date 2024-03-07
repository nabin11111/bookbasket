package com.nabin.bookbasket.presentation.user.dashboard.home

import com.nabin.bookbasket.data.model.FavouriteModel
import com.nabin.bookbasket.data.model.RealtimeModelResponse
import com.nabin.bookbasket.domain.model.AllFoods
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserHomeState(
    val name: List<RealtimeModelResponse> = emptyList(),
    val allFoods : List<AllFoods> = emptyList(),
    val offer : String ="",
    val favouriteList: List<FavouriteModel> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)