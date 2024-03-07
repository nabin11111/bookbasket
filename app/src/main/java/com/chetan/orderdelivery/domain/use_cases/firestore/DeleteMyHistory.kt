package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class DeleteMyHistory @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(orderId: String) = repository.deleteMyHistory(orderId)
}