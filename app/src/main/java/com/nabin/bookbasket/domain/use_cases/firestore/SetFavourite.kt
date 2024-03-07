package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetFavourite @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(isFavourite: Boolean = false, foodId: String) =
        repository.setFavourite(isFavourite, foodId)
}