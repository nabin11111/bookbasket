package com.chetan.orderdelivery.presentation.admin.dashboard.home

sealed interface AdminHomeEvent {
    data object DismissInfoMsg : AdminHomeEvent
    data class RemoveUser(val user: String) : AdminHomeEvent
    data object Test : AdminHomeEvent
    data object OnRefresh : AdminHomeEvent
    data class OnFilterChange(val value: BranchType) : AdminHomeEvent
}