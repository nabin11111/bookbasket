package com.chetan.orderdelivery.di

import android.app.Application
import androidx.room.Room
import com.chetan.orderdelivery.data.data_source.OrderDeliveryDatabase
import com.chetan.orderdelivery.data.repositoryImpl.DBRepositoryImpl
import com.chetan.orderdelivery.domain.repository.DBRepository
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.db.GetAllCheckoutFoods
import com.chetan.orderdelivery.domain.use_cases.db.GetAllFoods
import com.chetan.orderdelivery.domain.use_cases.db.GetAllIds
import com.chetan.orderdelivery.domain.use_cases.db.InsertAllCheckoutFoodList
import com.chetan.orderdelivery.domain.use_cases.db.InsertFoodList
import com.chetan.orderdelivery.domain.use_cases.db.InsertIds
import com.chetan.orderdelivery.domain.use_cases.db.RemoveAllCheckoutFoods
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOrderDeliveryDatabase(app: Application): OrderDeliveryDatabase {
        return Room.databaseBuilder(
            app, OrderDeliveryDatabase::class.java, OrderDeliveryDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDBRepository(db: OrderDeliveryDatabase): DBRepository {
        return DBRepositoryImpl(db.allFoodsDao)
    }


    @Provides
    @Singleton
    fun provdeOrderDeliveryUseCases(repository: DBRepository) = DBUseCases(
        getAllFoods = GetAllFoods(dbRepository = repository),
        insertFoodList = InsertFoodList(dbRepository = repository),

        insertIds = InsertIds(dbRepository = repository),
        getAllIds = GetAllIds(dbRepository = repository),

        insertAllCheckoutFoodList = InsertAllCheckoutFoodList(dbRepository = repository),
        getAllCheckoutFoods = GetAllCheckoutFoods(dbRepository = repository),
        removeAllCheckoutFoods = RemoveAllCheckoutFoods(dbRepository = repository)
    )

}