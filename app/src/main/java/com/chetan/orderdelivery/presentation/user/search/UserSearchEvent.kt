package com.chetan.orderdelivery.presentation.user.search

import com.chetan.orderdelivery.presentation.user.dashboard.UserDashboardEvent

sealed interface UserSearchEvent{
    data class OnQueryChange(val value: String): UserSearchEvent
    data object OnQueryCrossClicked: UserSearchEvent
}