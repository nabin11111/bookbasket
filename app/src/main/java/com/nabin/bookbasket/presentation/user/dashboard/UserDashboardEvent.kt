package com.nabin.bookbasket.presentation.user.dashboard

sealed interface UserDashboardEvent{
    data object DismissInfoMsg: UserDashboardEvent
    data object ChangeDarkMode: UserDashboardEvent

}