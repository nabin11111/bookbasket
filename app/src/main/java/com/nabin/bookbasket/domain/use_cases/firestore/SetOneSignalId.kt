package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetOneSignalId @Inject constructor(
    private val repository: FirestoreRepository
){
    suspend operator fun invoke(data: SetOneSignalId) = repository.setOneSignalId(data)
}