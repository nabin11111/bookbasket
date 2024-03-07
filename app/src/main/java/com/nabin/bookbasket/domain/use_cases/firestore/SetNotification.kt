package com.nabin.bookbasket.domain.use_cases.firestore

import com.nabin.bookbasket.data.model.StoreNotificationRequestResponse
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import javax.inject.Inject

class SetNotification @Inject constructor(
    private val repository: FirestoreRepository
) {
    suspend operator fun invoke(data : StoreNotificationRequestResponse) = repository.setNotification(data)
}