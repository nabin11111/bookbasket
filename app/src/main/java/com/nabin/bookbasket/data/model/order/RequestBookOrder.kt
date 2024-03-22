package com.nabin.bookbasket.data.model.order

data class RequestBookOrder(
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
        val bookId: String = "",
        val bookType: String = "",
        val bookFamily: String = "",
        val bookName: String = "",
        val bookDetails: String = "",
        val bookPrice: String = "",
        val bookDiscount: String = "",
        val bookNewPrice: Int = 0,
        val isSelected: Boolean = false,
        val foodRating: Float = 0f,
        val newBookRating: Float = 0f,
        val quantity: Int = 0,
        val date: String = "",
        val faceImgName: String = "",
        val faceImgUrl: String = "",
    )
}
