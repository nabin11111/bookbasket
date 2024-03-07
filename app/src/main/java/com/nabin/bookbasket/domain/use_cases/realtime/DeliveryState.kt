package com.nabin.bookbasket.domain.use_cases.realtime

import com.nabin.bookbasket.domain.repository.RealtimeRepository
import javax.inject.Inject

class DeliveryState @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {

    operator fun invoke() = realtimeRepository.deliveryState()
}