package com.nabin.bookbasket.presentation.admin.addoffer

import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class AdminAddOfferState(

    val bookName : String = "",
    val bookId : String = "",
    val bookDetails: String = "",
    val bookPrice: String = "",
    val bookDiscountPrice : String = "",
    val faceImgUrl : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)