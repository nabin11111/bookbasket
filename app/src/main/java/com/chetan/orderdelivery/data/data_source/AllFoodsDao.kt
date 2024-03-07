package com.chetan.orderdelivery.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.domain.use_cases.firestore.GetOneSignalIds
import kotlinx.coroutines.flow.Flow


@Dao
interface AllFoodsDao {

    @Query("SELECT * FROM allFoods")
    suspend fun getAllFoods() : List<AllFoods>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodList(noteList: List<AllFoods>)

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