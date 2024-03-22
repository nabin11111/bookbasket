package com.nabin.bookbasket.data.model


data class GetCartItemModel(
    val bookId: String = "",
    val bookQuantity : Int = 0,
    val date: String = ""
)
