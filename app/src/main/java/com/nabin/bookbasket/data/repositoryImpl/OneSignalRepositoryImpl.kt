package com.nabin.bookbasket.data.repositoryImpl

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.PushNotificationRequest
import com.nabin.bookbasket.domain.repository.OneSignalRepository
import retrofit2.HttpException
import javax.inject.Inject

class OneSignalRepositoryImpl @Inject constructor(
    private val oneSignal : OneSignalRepository
) : OneSignalRepository{
    override suspend fun pushNotification(requestBody: PushNotificationRequest): Resource<Boolean> {
        return try {
            oneSignal.pushNotification(requestBody)
            Resource.Success(true)
        }catch (e: HttpException){
            e.printStackTrace()
            Resource.Failure(e)
        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}