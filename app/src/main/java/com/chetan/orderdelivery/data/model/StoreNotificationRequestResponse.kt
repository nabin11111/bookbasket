package com.chetan.orderdelivery.data.model

data class StoreNotificationRequestResponse(
    val body: String = "",
    val time: String = "",
    val title: String = "",
    val readNotice: Boolean = false
)
