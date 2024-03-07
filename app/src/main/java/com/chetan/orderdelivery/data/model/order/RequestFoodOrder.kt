package com.chetan.orderdelivery.data.model.order

data class RequestFoodOrder(
    val orderId: String = "",

    val userName: String = "",
    val googleUserName: String = "",
    val userContactNo: String = "",
    val googleProfileUrl: String = "",
    val dbProfileUrl: String = "",
    val userMail: String = "",
    val oneSignalId: String = "",
    val isDelivered: Boolean = false,
    val deliveredTime: String = "",
    val locationLat: String = "",
    val locationLng: String = "",
    val distance : String = "",
    val locationAddress: String = "",
    val dateTime: String = "",
    val branch: String = "npj",
    val orderList: List<OrderedList> = emptyList(),
    ){
    data class OrderedList(
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
}
