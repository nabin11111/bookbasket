package com.nabin.bookbasket.presentation.admin.history

import com.nabin.bookbasket.data.model.order.RequestFoodOrder
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminHistoryState(
    val historyList: List<RequestFoodOrder> = emptyList(),
    val listByGroup: List<Pair<String, List<RequestFoodOrder>>> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)