package com.nabin.bookbasket.data.model

data class RatingRequestResponse(
    val userMail: String = "",
    val userName: String = "",
    val bookId: String = "",
    val rateValue: Float = 0f,
    val url: String = ""
)
