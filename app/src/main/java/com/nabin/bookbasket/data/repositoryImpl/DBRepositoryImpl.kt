package com.nabin.bookbasket.data.repositoryImpl

import com.nabin.bookbasket.data.data_source.AllFoodsDao
import com.nabin.bookbasket.domain.model.AllFoods
import com.nabin.bookbasket.domain.model.CheckoutFoods
import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.domain.repository.DBRepository

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