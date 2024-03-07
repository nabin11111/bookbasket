package com.chetan.orderdelivery.domain.repository

import android.net.Uri
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.ImageStorageDetails
import com.chetan.orderdelivery.presentation.admin.food.addfood.ImageUrlDetail


interface StorageRepository {
    suspend fun insertImage(data: ImageStorageDetails) : Resource<Pair<String,String>>
    suspend fun deleteImage(data: ImageUrlDetail) : Resource<Boolean>
}