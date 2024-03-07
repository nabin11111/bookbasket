package com.nabin.bookbasket.domain.use_cases.db

import com.nabin.bookbasket.domain.model.CheckoutFoods
import com.nabin.bookbasket.domain.repository.DBRepository
import javax.inject.Inject

class InsertAllCheckoutFoodList @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(checkList: List<CheckoutFoods>) = dbRepository.insertAllCheckoutFoods(checkList)
}