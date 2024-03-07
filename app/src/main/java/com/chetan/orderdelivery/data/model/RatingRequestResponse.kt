package com.chetan.orderdelivery.data.model

data class RatingRequestResponse(
    val userMail: String = "",
    val userName: String = "",
    val foodId: String = "",
    val rateValue: Float = 0f,
    val url: String = ""
)
