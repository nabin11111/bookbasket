package com.chetan.orderdelivery.data.model

data class GetFoodResponse(
    val foodId: String = "",
    val foodType: String = "",
    val foodFamily: String = "",
    val foodName: String = "",
    val foodDetails: String = "",
    val foodPrice: String = "",
    val foodDiscount: String = "",
    val foodNewPrice: Int = 0,
    val foodRating: Float = 0f,
    val newFoodRating: Float = 0f,
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
