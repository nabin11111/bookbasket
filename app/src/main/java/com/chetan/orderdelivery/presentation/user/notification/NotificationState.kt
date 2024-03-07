package com.chetan.orderdelivery.presentation.user.notification

import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class NotificationState(
    val notificationList: List<StoreNotificationRequestResponse> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)