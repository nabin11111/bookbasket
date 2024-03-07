package com.nabin.bookbasket.presentation.user.foodwithcategories

import com.nabin.bookbasket.domain.model.AllFoods
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserFoodCategoryState(
    val allFoods : List<AllFoods> = emptyList(),
    val foodTypesList: List<String> = emptyList(),
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)