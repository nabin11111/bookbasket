package com.nabin.bookbasket.domain.use_cases.realtime

data class RealtimeUseCases(
    val insert : Insert,
    val getItems: GetItems,
    val deleteOrders: DeleteOrders,

    val changeDeliveryState: ChangeDeliveryState,
    val deliveryState: DeliveryState
)
