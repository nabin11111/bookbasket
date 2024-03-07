package com.nabin.bookbasket.presentation.user.search

sealed interface UserSearchEvent{
    data class OnQueryChange(val value: String): UserSearchEvent
    data object OnQueryCrossClicked: UserSearchEvent
}