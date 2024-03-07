package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.data.model.SetLatLng
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetAddress @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(address: SetLatLng) = repository.setAddress(address)
}