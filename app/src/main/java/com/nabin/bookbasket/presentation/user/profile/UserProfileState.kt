package com.nabin.bookbasket.presentation.user.profile

import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserProfileState(
    val name: String = "",
    val address: String = "",
    val phoneNo : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
