package com.nabin.bookbasket.presentation.user.notification

import com.nabin.bookbasket.data.model.StoreNotificationRequestResponse
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class NotificationState(
    val notificationList: List<StoreNotificationRequestResponse> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)