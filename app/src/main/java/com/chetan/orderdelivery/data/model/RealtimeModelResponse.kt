package com.chetan.orderdelivery.data.model

data class RealtimeModelResponse(
    val item: RealTimeNewOrderRequest,
    val key: String = ""
){
    data class RealTimeNewOrderRequest(
        val newOrder : Boolean = false,
        val title: String = "",
        val description: String = ""
    )
}
