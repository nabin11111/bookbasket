package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.repository.DBRepository
import javax.inject.Inject

class InsertAllCheckoutFoodList @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(checkList: List<CheckoutFoods>) = dbRepository.insertAllCheckoutFoods(checkList)
}