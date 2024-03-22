package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.data.model.AddBookRequest
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddFood @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data: AddBookRequest) = repository.addFood(data)
}