package com.nabin.bookbasket.domain.use_cases.realtime

import com.nabin.bookbasket.data.model.RealtimeModelResponse
import com.nabin.bookbasket.domain.repository.RealtimeRepository
import javax.inject.Inject

class Insert @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {
    suspend operator fun invoke(data: RealtimeModelResponse.RealTimeNewOrderRequest) = realtimeRepository.insert(data)
}