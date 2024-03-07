package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.repository.DBRepository
import javax.inject.Inject

class RemoveAllCheckoutFoods @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke() = dbRepository.removeAllCheckoutFoods()
}