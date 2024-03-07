package com.chetan.orderdelivery.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckoutFoods(
    @PrimaryKey
    val foodId: String = "",
    val foodType: String = "",
    val foodFamily: String = "",
    val foodName: String = "",
    val foodDetails: String = "",
    val foodPrice: String = "",
    val foodDiscount: String = "",
    val foodNewPrice: Int = 0,
    val isSelected: Boolean = false,
    val foodRating: Float = 0f,
    val newFoodRating: Float = 0f,
    val quantity: Int = 0,
    val date: String = "",
    val faceImgName: String = "",
    val faceImgUrl: String = "",
)
