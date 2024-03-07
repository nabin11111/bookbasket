package com.chetan.orderdelivery.presentation.user.dashboard.home

import com.chetan.orderdelivery.data.model.FavouriteModel
import com.chetan.orderdelivery.data.model.GetFoodResponse
import com.chetan.orderdelivery.data.model.RealtimeModelResponse
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserHomeState(
    val name: List<RealtimeModelResponse> = emptyList(),
    val allFoods : List<AllFoods> = emptyList(),
    val offer : String ="",
    val favouriteList: List<FavouriteModel> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)