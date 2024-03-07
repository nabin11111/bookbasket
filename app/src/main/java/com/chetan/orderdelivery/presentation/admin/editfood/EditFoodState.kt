package com.chetan.orderdelivery.presentation.admin.editfood

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.admin.food.addfood.ImageUrlDetail
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class EditFoodState(
    val foodItemDetails : AllFoods = AllFoods(),
    val foodTypes: List<String> =  listOf("Regular", "Popular","New","Drinks"),
    val selectedFoodType: String = foodTypes[0],

    val foodFamilies: List<String> = listOf("Pizza", "Momo", "Burger","Soft Drinks","Hard Drinks"),
    val selectedFoodFamily: String = foodFamilies[0],

    val faceImgUrl : ImageUrlDetail = ImageUrlDetail(),
    val supportImgUrl2 : ImageUrlDetail = ImageUrlDetail(),
    val supportImgUrl3 : ImageUrlDetail = ImageUrlDetail(),
    val supportImgUrl4 : ImageUrlDetail = ImageUrlDetail(),

    val supportImageUrls : List<ImageUrlDetail> = emptyList(),

    val foodName : String = "",
    val foodId : String = "",
    val foodDetails: String = "",
    val foodPrice: String = "",
    val foodDiscountPrice : String = "",
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)