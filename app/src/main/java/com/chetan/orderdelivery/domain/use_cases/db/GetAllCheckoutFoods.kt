package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.repository.DBRepository
import javax.inject.Inject

class GetAllCheckoutFoods @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(): List<CheckoutFoods> = dbRepository.getAllCheckoutFoods()
}