package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.GetCartItemModel
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddToCart @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(foodItem : GetCartItemModel) = repository.addToCart(foodItem)
}