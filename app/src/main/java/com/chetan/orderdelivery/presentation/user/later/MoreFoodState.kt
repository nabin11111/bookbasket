package com.chetan.orderdelivery.presentation.user.later

import com.chetan.orderdelivery.data.model.GetFoodResponse

data class MoreFoodState(
    val allFoodList: List<GetFoodResponse> = emptyList()
)
