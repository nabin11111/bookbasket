package com.chetan.orderdelivery.domain.use_cases.storage

import android.net.Uri
import com.chetan.orderdelivery.data.model.ImageStorageDetails
import com.chetan.orderdelivery.domain.repository.StorageRepository
import javax.inject.Inject

class InsertImage @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(data: ImageStorageDetails) = repository.insertImage(data)
}