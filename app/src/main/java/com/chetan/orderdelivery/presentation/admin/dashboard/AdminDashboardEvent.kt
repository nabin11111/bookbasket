package com.chetan.orderdelivery.presentation.admin.dashboard

sealed interface AdminDashboardEvent{
    data object DismissInfoMsg: AdminDashboardEvent
    data object ChangeDarkMode: AdminDashboardEvent
    data object ChangeDeliveryState: AdminDashboardEvent
}