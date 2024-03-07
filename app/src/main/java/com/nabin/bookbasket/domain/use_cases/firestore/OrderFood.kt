package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.data.model.order.RequestFoodOrder
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class OrderFood @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data: RequestFoodOrder) = repository.orderFood(data)
}