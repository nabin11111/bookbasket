package com.nabin.bookbasket.domain.use_cases.realtime

import com.nabin.bookbasket.domain.repository.RealtimeRepository
import javax.inject.Inject

class DeleteOrders @Inject constructor(
    private val realtimeRepository: RealtimeRepository
) {
    suspend operator fun invoke() = realtimeRepository.deleteOrders()
}