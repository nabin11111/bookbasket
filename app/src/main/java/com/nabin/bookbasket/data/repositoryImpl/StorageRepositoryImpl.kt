package com.nabin.bookbasket.data.repositoryImpl

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.ImageStorageDetails
import com.nabin.bookbasket.domain.repository.StorageRepository
import com.nabin.bookbasket.presentation.admin.food.addfood.ImageUrlDetail
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage : FirebaseStorage
): StorageRepository {
    override suspend fun insertImage(data: ImageStorageDetails): Resource<Pair<String, String>> {
        return try {

            val imageRef = storage.reference.child(data.imagePath + data.imageName)
            val uploadTask = imageRef.putFile(data.imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            val uploadImageInfo = Pair(data.imageName, downloadUrl.toString())
            Resource.Success(uploadImageInfo)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteImage(data: ImageUrlDetail): Resource<Boolean> {
        return try {
            storage.reference.child(data.storagePath + data.imageName).delete().await()
            Resource.Success(false)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}