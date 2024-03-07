package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class DeleteMyHistory @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(orderId: String) = repository.deleteMyHistory(orderId)
}