package com.chetan.orderdelivery.presentation.admin.sendnotice

sealed interface AdminSendNoticeEvent{
    data class OnTitleChange(val value: String) : AdminSendNoticeEvent
    data class OnNoticeChange(val value: String) : AdminSendNoticeEvent
    data object DismissInfoMsg : AdminSendNoticeEvent
    data object SendNotice : AdminSendNoticeEvent
}