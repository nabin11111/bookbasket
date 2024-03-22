package com.nabin.bookbasket.data.repositoryImpl

import com.nabin.bookbasket.data.data_source.AllBooksDao
import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.domain.model.CheckoutBooks
import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.domain.repository.DBRepository

class DBRepositoryImpl(
    private val dao: AllBooksDao
) : DBRepository{
    override suspend fun getAllFoods(): List<AllBooks> {
        return dao.getAllFoods()
    }

    override suspend fun insertFoodList(noteList: List<AllBooks>) {
        dao.insertFoodList(noteList)
    }

    override suspend fun getAllIds(): List<SetOneSignalId>{
        return dao.getAllIds()
    }

    override suspend fun insertIds(idsList: List<SetOneSignalId>) {
        dao.insertIds(idsList)
    }

    override suspend fun getAllCheckoutFoods(): List<CheckoutBooks> {
        return dao.getAllCheckoutFoods()
    }

    override suspend fun insertAllCheckoutFoods(checkList: List<CheckoutBooks>) {
        dao.insertAllCheckedoutFoods(checkList)
    }

    override suspend fun removeAllCheckoutFoods() {
        dao.removeAllCheckoutFoods()
    }
}