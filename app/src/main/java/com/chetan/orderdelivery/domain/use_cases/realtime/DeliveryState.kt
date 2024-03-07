package com.chetan.orderdelivery.domain.use_cases.realtime

import com.chetan.orderdelivery.domain.repository.RealtimeRepository
import javax.inject.Inject

class DeliveryState @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {

    operator fun invoke() = realtimeRepository.deliveryState()
}