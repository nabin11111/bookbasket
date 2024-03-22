package com.nabin.bookbasket.data.model

data class GetBookResponse(
    val bookId: String = "",
    val bookType: String = "",
    val bookFamily: String = "",
    val bookName: String = "",
    val bookDetails: String = "",
    val bookPrice: String = "",
    val bookDiscount: String = "",
    val bookNewPrice: Int = 0,
    val bookRating: Float = 0f,
    val newBookRating: Float = 0f,
    val date: String = "",


    val faceImgName: String = "",
    val faceImgUrl: String = "",
    val faceImgPath: String = "",

    val supportImgName2: String = "",
    val supportImgUrl2: String = "",
    val supportImgPath2: String = "",

    val supportImgName3: String = "",
    val supportImgUrl3: String = "",
    val supportImgPath3: String = "",

    val supportImgName4: String = "",
    val supportImgUrl4: String = "",
    val supportImgPath4: String = "",
)
