package com.nabin.bookbasket.domain.use_cases.storage

import com.nabin.bookbasket.data.model.ImageStorageDetails
import com.nabin.bookbasket.domain.repository.StorageRepository
import javax.inject.Inject

class InsertImage @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(data: ImageStorageDetails) = repository.insertImage(data)
}