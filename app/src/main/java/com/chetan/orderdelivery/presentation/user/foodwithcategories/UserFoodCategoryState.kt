package com.chetan.orderdelivery.presentation.user.foodwithcategories

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserFoodCategoryState(
    val allFoods : List<AllFoods> = emptyList(),
    val foodTypesList: List<String> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)