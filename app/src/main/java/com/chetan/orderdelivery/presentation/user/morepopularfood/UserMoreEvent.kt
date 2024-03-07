package com.chetan.orderdelivery.presentation.user.morepopularfood

sealed interface UserMoreEvent{
    data class OnQueryChange(val value: String): UserMoreEvent
    data object OnQueryCrossClicked: UserMoreEvent
    data class OnFilterChange( val value : FilterTypes): UserMoreEvent
}