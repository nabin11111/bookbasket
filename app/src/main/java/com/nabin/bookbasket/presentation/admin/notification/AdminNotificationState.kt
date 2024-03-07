package com.nabin.bookbasket.presentation.admin.notification

import com.nabin.bookbasket.data.model.StoreNotificationRequestResponse
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminNotificationState(
    val notificationList: List<StoreNotificationRequestResponse> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)