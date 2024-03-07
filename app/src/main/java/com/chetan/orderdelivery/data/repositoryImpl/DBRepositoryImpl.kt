package com.chetan.orderdelivery.data.repositoryImpl

import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.data_source.AllFoodsDao
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.domain.repository.DBRepository
import kotlinx.coroutines.flow.Flow

class DBRepositoryImpl(
    private val dao: AllFoodsDao
) : DBRepository{
    override suspend fun getAllFoods(): List<AllFoods> {
        return dao.getAllFoods()
    }

    override suspend fun insertFoodList(noteList: List<AllFoods>) {
        dao.insertFoodList(noteList)
    }

    override suspend fun getAllIds(): List<SetOneSignalId>{
        return dao.getAllIds()
    }

    override suspend fun insertIds(idsList: List<SetOneSignalId>) {
        dao.insertIds(idsList)
    }

    override suspend fun getAllCheckoutFoods(): List<CheckoutFoods> {
        return dao.getAllCheckoutFoods()
    }

    override suspend fun insertAllCheckoutFoods(checkList: List<CheckoutFoods>) {
        dao.insertAllCheckedoutFoods(checkList)
    }

    override suspend fun removeAllCheckoutFoods() {
        dao.removeAllCheckoutFoods()
    }
}