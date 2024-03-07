package com.chetan.orderdelivery.presentation.admin.dashboard.home

import com.chetan.orderdelivery.data.model.SetLatLng
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class AdminHomeState(
    val test: String = "",
    val orderList: List<SetLatLng> = emptyList(),
    val branchWiseList : List<SetLatLng> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)