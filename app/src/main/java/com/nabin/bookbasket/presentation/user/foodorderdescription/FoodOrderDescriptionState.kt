package com.nabin.bookbasket.presentation.user.foodorderdescription

import com.nabin.bookbasket.data.model.FavouriteModel
import com.nabin.bookbasket.domain.model.AllFoods
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

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
