package com.nabin.bookbasket.domain.use_cases.storage

import com.nabin.bookbasket.domain.repository.StorageRepository
import com.nabin.bookbasket.presentation.admin.food.addfood.ImageUrlDetail
import javax.inject.Inject

class DeleteImage @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(data: ImageUrlDetail) = repository.deleteImage(data)
}