package com.nabin.bookbasket.presentation.admin.dashboard

import com.nabin.bookbasket.data.model.RealtimeModelResponse
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminDashboardState(
    val darkMode: Boolean = false,
    val changeDeliveryState: Boolean = false,
    val isNewOrder : Boolean = false,
    val newRequestList:  List<RealtimeModelResponse> = emptyList(),
    val isNewNotification: Boolean = false,
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)