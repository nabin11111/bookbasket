package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetOffer @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke() = repository.getOffer()
}