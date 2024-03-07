package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.ProfileRequestResponse
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateUserProfile @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data: ProfileRequestResponse) = repository.updateUserProfile(data)
}