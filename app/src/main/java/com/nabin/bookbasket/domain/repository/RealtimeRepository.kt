package com.nabin.bookbasket.domain.repository

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.RealtimeModelResponse
import kotlinx.coroutines.flow.Flow

interface RealtimeRepository {

    suspend fun insert(
        item: RealtimeModelResponse.RealTimeNewOrderRequest
    ) : Resource<String>

    fun getItems() : Flow<Resource<List<RealtimeModelResponse>>>
    fun deliveryState() : Flow<Resource<Boolean>>
    suspend fun changeDeliveryState(onOff: Boolean) : Resource<Boolean>

    suspend fun deleteOrders() : Resource<Boolean>
}