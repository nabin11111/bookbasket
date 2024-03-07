package com.chetan.orderdelivery.presentation.admin.orderdetails

sealed interface AdminOrderDetailEvent{
    data class Delivered(val value: String) :AdminOrderDetailEvent
    data object DismissInfoMsg: AdminOrderDetailEvent
    data class GetOrderDetails(val user: String) : AdminOrderDetailEvent
    data class OnOrderReady(val value: String): AdminOrderDetailEvent
    data class OnMessageChange(val value: String): AdminOrderDetailEvent
    data class OnMessageSend(val value : String) : AdminOrderDetailEvent
    data class OnShowHideMsgDialog(val value: Boolean) : AdminOrderDetailEvent
}