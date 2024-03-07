package com.nabin.bookbasket.presentation.admin.sendnotice

import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminSendNoticeState(
    val title : String = "",
    val notice : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)