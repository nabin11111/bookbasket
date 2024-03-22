package com.nabin.bookbasket.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.domain.model.CheckoutFoods
import com.nabin.bookbasket.domain.model.SetOneSignalId

@Database(
    entities = [AllBooks::class,CheckoutFoods::class,SetOneSignalId::class],
    version = 1,
    exportSchema = false
)
abstract class OrderDeliveryDatabase :RoomDatabase() {

    abstract val allBooksDao: AllFoodsDao

    companion object{
        const val DATABASE_NAME = "order_delivery_db"
    }
}