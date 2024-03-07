package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateUserHistory @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data : RequestFoodOrder) = repository.updateUserHistory(data)
}