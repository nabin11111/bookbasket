package com.nabin.bookbasket.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckoutBooks(
    @PrimaryKey
    val bookId: String = "",
    val bookType: String = "",
    val bookFamily: String = "",
    val bookName: String = "",
    val bookDetails: String = "",
    val bookPrice: String = "",
    val bookDiscount: String = "",
    val bookNewPrice: Int = 0,
    val isSelected: Boolean = false,
    val bookRating: Float = 0f,
    val newBookRating: Float = 0f,
    val quantity: Int = 0,
    val date: String = "",
    val faceImgName: String = "",
    val faceImgUrl: String = "",
)
