package com.nabin.bookbasket.di

import android.app.Application
import androidx.room.Room
import com.nabin.bookbasket.data.data_source.OrderDeliveryDatabase
import com.nabin.bookbasket.data.repositoryImpl.DBRepositoryImpl
import com.nabin.bookbasket.domain.repository.DBRepository
import com.nabin.bookbasket.domain.use_cases.db.DBUseCases
import com.nabin.bookbasket.domain.use_cases.db.GetAllCheckoutBooks
import com.nabin.bookbasket.domain.use_cases.db.GetAllBooks
import com.nabin.bookbasket.domain.use_cases.db.GetAllIds
import com.nabin.bookbasket.domain.use_cases.db.InsertAllCheckoutBookList
import com.nabin.bookbasket.domain.use_cases.db.InsertBookList
import com.nabin.bookbasket.domain.use_cases.db.InsertIds
import com.nabin.bookbasket.domain.use_cases.db.RemoveAllCheckoutBooks
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
        return DBRepositoryImpl(db.allBooksDao)
    }


    @Provides
    @Singleton
    fun provdeOrderDeliveryUseCases(repository: DBRepository) = DBUseCases(
        getAllBooks = GetAllBooks(dbRepository = repository),
        insertBookList = InsertBookList(dbRepository = repository),

        insertIds = InsertIds(dbRepository = repository),
        getAllIds = GetAllIds(dbRepository = repository),

        insertAllCheckoutBookList = InsertAllCheckoutBookList(dbRepository = repository),
        getAllCheckoutBooks = GetAllCheckoutBooks(dbRepository = repository),
        removeAllCheckoutBooks = RemoveAllCheckoutBooks(dbRepository = repository)
    )

}