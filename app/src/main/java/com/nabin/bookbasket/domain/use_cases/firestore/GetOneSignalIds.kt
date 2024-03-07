package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetOneSignalIds @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(branch: String) = repository.getOneSignalIds(branch)
}