package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateRating @Inject constructor(
    private val repository: FirestoreRepository
){
    suspend operator fun invoke(foodId: String, foodRating: Float) = repository.updateRating(foodId,foodRating)
}