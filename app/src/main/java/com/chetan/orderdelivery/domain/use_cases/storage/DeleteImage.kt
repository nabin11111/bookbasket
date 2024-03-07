package com.chetan.orderdelivery.domain.use_cases.storage

import com.chetan.orderdelivery.domain.repository.StorageRepository
import com.chetan.orderdelivery.presentation.admin.food.addfood.ImageUrlDetail
import javax.inject.Inject

class DeleteImage @Inject constructor(
    private val repository: StorageRepository
) {
    suspend operator fun invoke(data: ImageUrlDetail) = repository.deleteImage(data)
}