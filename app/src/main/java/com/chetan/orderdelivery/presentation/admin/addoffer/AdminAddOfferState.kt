package com.chetan.orderdelivery.presentation.admin.addoffer

import com.chetan.orderdelivery.presentation.admin.food.addfood.ImageUrlDetail
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class AdminAddOfferState(

    val foodName : String = "",
    val foodId : String = "",
    val foodDetails: String = "",
    val foodPrice: String = "",
    val foodDiscountPrice : String = "",
    val faceImgUrl : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)