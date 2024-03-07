package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.AddFoodRequest
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddFood @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data: AddFoodRequest) = repository.addFood(data)
}