package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.SetLatLng
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetAddress @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(address: SetLatLng) = repository.setAddress(address)
}