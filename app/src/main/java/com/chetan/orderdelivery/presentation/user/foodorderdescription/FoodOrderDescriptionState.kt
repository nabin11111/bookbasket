package com.chetan.orderdelivery.presentation.user.foodorderdescription

import com.chetan.orderdelivery.data.model.FavouriteModel
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class FoodOrderDescriptionState(
    val deliveryState: Boolean = false,
    val deliveryStateShowDialog: Boolean = true,
    val phoneNo : String = "",
    val location : String = "",
    val foodItemDetails : AllFoods = AllFoods(),
    val foodPrice: Int = 0,
    val foodDiscount: Int = 0,
    val foodQuantity: Int = 1,
    val totalCartItem : Int = 0,
    val favouriteList: List<FavouriteModel> = emptyList(),
    override val infoMsg: Message? = null
): OrderDeliveryScreenState(infoMsg)
