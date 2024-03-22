package com.nabin.bookbasket.domain.repository

import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.domain.model.CheckoutBooks
import com.nabin.bookbasket.domain.model.SetOneSignalId

interface DBRepository {
    suspend fun getAllFoods(): List<AllBooks>
    suspend fun insertFoodList(noteList: List<AllBooks>)

    suspend fun getAllIds(): List<SetOneSignalId>
    suspend fun insertIds(idsList: List<SetOneSignalId>)

    suspend fun getAllCheckoutFoods() : List<CheckoutBooks>
    suspend fun insertAllCheckoutFoods(checkList : List<CheckoutBooks>)
    suspend fun removeAllCheckoutFoods()
}