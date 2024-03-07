package com.chetan.orderdelivery.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.model.SetOneSignalId

@Database(
    entities = [AllFoods::class,CheckoutFoods::class,SetOneSignalId::class],
    version = 1,
    exportSchema = false
)
abstract class OrderDeliveryDatabase :RoomDatabase() {

    abstract val allFoodsDao: AllFoodsDao

    companion object{
        const val DATABASE_NAME = "order_delivery_db"
    }
}