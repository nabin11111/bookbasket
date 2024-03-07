package com.chetan.orderdelivery.presentation.user.profile

import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserProfileState(
    val name: String = "",
    val address: String = "",
    val phoneNo : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
