package com.chetan.orderdelivery.domain.use_cases.firestore

import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetNotification @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data : StoreNotificationRequestResponse) = repository.setNotification(data)
}