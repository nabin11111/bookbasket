package com.nabin.bookbasket.presentation.admin.dashboard.home

import com.nabin.bookbasket.data.model.SetLatLng
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminHomeState(
    val test: String = "",
    val orderList: List<SetLatLng> = emptyList(),
    val branchWiseList : List<SetLatLng> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)