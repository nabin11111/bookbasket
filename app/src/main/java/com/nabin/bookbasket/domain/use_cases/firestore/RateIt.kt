package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.data.model.RatingRequestResponse
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class RateIt @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data: RatingRequestResponse) = repository.rating(data)
}