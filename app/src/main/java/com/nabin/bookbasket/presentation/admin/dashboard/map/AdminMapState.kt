package com.nabin.bookbasket.presentation.admin.dashboard.map

import com.nabin.bookbasket.data.model.SetLatLng

data class AdminMapState(
    val orderedUserList: List<SetLatLng> = emptyList(),
    val userDetails: SetLatLng = SetLatLng()
)
