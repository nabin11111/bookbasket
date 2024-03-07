package com.nabin.bookbasket.presentation.user.ordercheckout

import com.nabin.bookbasket.domain.model.CheckoutFoods
import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message
import com.google.android.gms.maps.model.LatLng

data class OrderCheckoutState(
    val test: String = "",
    val locationAddress : String = "",
    val location : String = "",
    val momobarNpj: LatLng = LatLng(
        28.0594641, 81.617649
    ),
    val momobarKlp: LatLng = LatLng(
        28.199383, 81.688371
    ),
    val branch: String = "npj",
    val cameraLocation: LatLng = LatLng(28.0594641, 81.617649,),
    val distance: String ="",
    val orderList: List<CheckoutFoods> = emptyList(),
    val allIds: List<SetOneSignalId> = emptyList(),
    val canOrder: Boolean = false,
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)
