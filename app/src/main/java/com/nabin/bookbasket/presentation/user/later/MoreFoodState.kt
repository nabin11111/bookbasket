package com.nabin.bookbasket.presentation.user.later

import com.nabin.bookbasket.data.model.GetFoodResponse

data class MoreFoodState(
    val allFoodList: List<GetFoodResponse> = emptyList()
)
