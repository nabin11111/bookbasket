package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.repository.DBRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFoods @Inject constructor(
    private val dbRepository: DBRepository
) {

    suspend operator fun invoke() = dbRepository.getAllFoods()
}