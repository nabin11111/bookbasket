package com.nabin.bookbasket.presentation.admin.addoffer

import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminAddOfferState(

    val foodName : String = "",
    val foodId : String = "",
    val foodDetails: String = "",
    val foodPrice: String = "",
    val foodDiscountPrice : String = "",
    val faceImgUrl : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)