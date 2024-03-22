package com.nabin.bookbasket.presentation.user.later

import com.nabin.bookbasket.data.model.GetFoodResponse

data class MoreBookState(
    val allBookList: List<GetFoodResponse> = emptyList()
)
