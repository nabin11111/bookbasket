package com.nabin.bookbasket.presentation.user.later

sealed interface MoreBookEvent{
    data object Test: MoreBookEvent
}