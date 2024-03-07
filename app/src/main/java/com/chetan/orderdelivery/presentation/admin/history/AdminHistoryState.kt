package com.chetan.orderdelivery.presentation.admin.history

import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class AdminHistoryState(
    val historyList: List<RequestFoodOrder> = emptyList(),
    val listByGroup: List<Pair<String, List<RequestFoodOrder>>> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)