package com.chetan.orderdelivery.presentation.admin.dashboard.map

import com.chetan.orderdelivery.data.model.SetLatLng

data class AdminMapState(
    val orderedUserList: List<SetLatLng> = emptyList(),
    val userDetails: SetLatLng = SetLatLng()
)
