package com.nabin.bookbasket.domain.repository

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.ImageStorageDetails
import com.nabin.bookbasket.presentation.admin.food.addfood.ImageUrlDetail


interface StorageRepository {
    suspend fun insertImage(data: ImageStorageDetails) : Resource<Pair<String,String>>
    suspend fun deleteImage(data: ImageUrlDetail) : Resource<Boolean>
}