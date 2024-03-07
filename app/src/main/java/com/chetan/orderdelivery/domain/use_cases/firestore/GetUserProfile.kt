package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetUserProfile @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(user: String) = repository.getUserProfile(user)
}