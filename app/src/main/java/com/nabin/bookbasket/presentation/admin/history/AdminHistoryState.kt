package com.nabin.bookbasket.presentation.admin.history

import com.nabin.bookbasket.data.model.order.RequestBookOrder
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminHistoryState(
    val historyList: List<RequestBookOrder> = emptyList(),
    val listByGroup: List<Pair<String, List<RequestBookOrder>>> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)