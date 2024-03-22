package com.nabin.bookbasket.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.domain.model.CheckoutFoods
import com.nabin.bookbasket.domain.model.SetOneSignalId


@Dao
interface AllFoodsDao {

    @Query("SELECT * FROM allBooks")
    suspend fun getAllFoods() : List<AllBooks>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodList(noteList: List<AllBooks>)

    @Query("SELECT * FROM SetOneSignalId")
    suspend fun getAllIds() : List<SetOneSignalId>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIds(idList: List<SetOneSignalId>)

    @Query("SELECT * FROM CheckoutFoods")
    suspend fun getAllCheckoutFoods() : List<CheckoutFoods>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCheckedoutFoods(checkedList: List<CheckoutFoods>)

    @Query("DELETE FROM CheckoutFoods")
    suspend fun removeAllCheckoutFoods()

}