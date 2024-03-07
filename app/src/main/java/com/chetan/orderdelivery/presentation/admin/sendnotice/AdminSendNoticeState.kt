package com.chetan.orderdelivery.presentation.admin.sendnotice

import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class AdminSendNoticeState(
    val title : String = "",
    val notice : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)