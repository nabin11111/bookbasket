package com.chetan.orderdelivery.domain.use_cases.realtime

import com.chetan.orderdelivery.data.model.RealtimeModelResponse
import com.chetan.orderdelivery.domain.repository.RealtimeRepository
import javax.inject.Inject

class DeleteOrders @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {
    suspend operator fun invoke() = realtimeRepository.deleteOrders()
}