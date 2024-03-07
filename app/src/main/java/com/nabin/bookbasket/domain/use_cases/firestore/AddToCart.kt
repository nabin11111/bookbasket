package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.data.model.GetCartItemModel
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddToCart @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(foodItem : GetCartItemModel) = repository.addToCart(foodItem)
}