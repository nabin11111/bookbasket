package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.repository.DBRepository
import javax.inject.Inject

class GetAllIds @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke() = dbRepository.getAllIds()
}