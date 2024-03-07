package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class OrderFood @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data: RequestFoodOrder) = repository.orderFood(data)
}