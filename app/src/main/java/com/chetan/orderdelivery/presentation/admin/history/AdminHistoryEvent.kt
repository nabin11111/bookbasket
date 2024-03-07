package com.chetan.orderdelivery.presentation.admin.history

sealed interface AdminHistoryEvent{
    data object DeleteHistory : AdminHistoryEvent
    data object DismissInfoMsg : AdminHistoryEvent
}