package com.nabin.bookbasket.domain.repository

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.PushNotificationRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OneSignalRepository {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: Basic YmY3NzA5MzYtMDQyYS00Njk4LTllZTAtZWQ1NWUyYTdjMDJh"
    )
    @POST("notifications")
    suspend fun pushNotification(
        @Body requestBody: PushNotificationRequest
    ): Resource<Boolean>
}