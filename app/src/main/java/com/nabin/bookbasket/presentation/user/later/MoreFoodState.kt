package com.nabin.bookbasket.presentation.user.later

import com.nabin.bookbasket.data.model.GetBookResponse

data class MoreFoodState(
    val allFoodList: List<GetBookResponse> = emptyList()
)
