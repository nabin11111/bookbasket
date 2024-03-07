package com.chetan.orderdelivery.domain.repository

import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.model.SetOneSignalId
import kotlinx.coroutines.flow.Flow

interface DBRepository {
    suspend fun getAllFoods(): List<AllFoods>
    suspend fun insertFoodList(noteList: List<AllFoods>)

    suspend fun getAllIds(): List<SetOneSignalId>
    suspend fun insertIds(idsList: List<SetOneSignalId>)

    suspend fun getAllCheckoutFoods() : List<CheckoutFoods>
    suspend fun insertAllCheckoutFoods(checkList : List<CheckoutFoods>)
    suspend fun removeAllCheckoutFoods()
}