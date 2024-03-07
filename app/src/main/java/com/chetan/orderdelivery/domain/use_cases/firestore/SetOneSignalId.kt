package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetOneSignalId @Inject constructor(
    private val repository: FirestoreRepository
){
    suspend operator fun invoke(data: SetOneSignalId) = repository.setOneSignalId(data)
}