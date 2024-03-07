package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.repository.DBRepository
import javax.inject.Inject

class InsertFoodList @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(list: List<AllFoods>) = dbRepository.insertFoodList(list)
}