package com.nabin.bookbasket.di

import android.app.Application
import com.nabin.bookbasket.data.local.Preference
import com.nabin.bookbasket.data.repositoryImpl.FirestoreRepositoryImpl
import com.nabin.bookbasket.data.repositoryImpl.RealtimeRepositoryImpl
import com.nabin.bookbasket.data.repositoryImpl.StorageRepositoryImpl
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import com.nabin.bookbasket.domain.repository.RealtimeRepository
import com.nabin.bookbasket.domain.repository.StorageRepository
import com.nabin.bookbasket.domain.use_cases.firestore.AddFood
import com.nabin.bookbasket.domain.use_cases.firestore.AddOffer
import com.nabin.bookbasket.domain.use_cases.firestore.AddToCart
import com.nabin.bookbasket.domain.use_cases.firestore.DeleteAdminNotification
import com.nabin.bookbasket.domain.use_cases.firestore.DeleteCartItem
import com.nabin.bookbasket.domain.use_cases.firestore.DeleteMyHistory
import com.nabin.bookbasket.domain.use_cases.firestore.DeleteNotification
import com.nabin.bookbasket.domain.use_cases.firestore.FirestoreUseCases
import com.nabin.bookbasket.domain.use_cases.firestore.GetAdminHistories
import com.nabin.bookbasket.domain.use_cases.firestore.GetAdminNotification
import com.nabin.bookbasket.domain.use_cases.firestore.GetCartItems
import com.nabin.bookbasket.domain.use_cases.firestore.GetFavouriteList
import com.nabin.bookbasket.domain.use_cases.firestore.GetBookItem
import com.nabin.bookbasket.domain.use_cases.firestore.GetBookOrderDetails
import com.nabin.bookbasket.domain.use_cases.firestore.GetBookOrders
import com.nabin.bookbasket.domain.use_cases.firestore.GetBooks
import com.nabin.bookbasket.domain.use_cases.firestore.GetBooksForUpdate
import com.nabin.bookbasket.domain.use_cases.firestore.GetMyHistory
import com.nabin.bookbasket.domain.use_cases.firestore.GetNotification
import com.nabin.bookbasket.domain.use_cases.firestore.GetOffer
import com.nabin.bookbasket.domain.use_cases.firestore.GetOneSignalIds
import com.nabin.bookbasket.domain.use_cases.firestore.GetUserProfile
import com.nabin.bookbasket.domain.use_cases.firestore.OrderDelivered
import com.nabin.bookbasket.domain.use_cases.firestore.OrderBook
import com.nabin.bookbasket.domain.use_cases.firestore.RateIt
import com.nabin.bookbasket.domain.use_cases.firestore.ReadNotification
import com.nabin.bookbasket.domain.use_cases.firestore.RemoveUserOrder
import com.nabin.bookbasket.domain.use_cases.firestore.SetAddress
import com.nabin.bookbasket.domain.use_cases.firestore.SetAdminNotification
import com.nabin.bookbasket.domain.use_cases.firestore.SetFavourite
import com.nabin.bookbasket.domain.use_cases.firestore.SetNotification
import com.nabin.bookbasket.domain.use_cases.firestore.SetOneSignalId
import com.nabin.bookbasket.domain.use_cases.firestore.UpdateDeliveredHistroy
import com.nabin.bookbasket.domain.use_cases.firestore.UpdateRating
import com.nabin.bookbasket.domain.use_cases.firestore.UpdateUserHistory
import com.nabin.bookbasket.domain.use_cases.firestore.UpdateUserProfile
import com.nabin.bookbasket.domain.use_cases.realtime.ChangeDeliveryState
import com.nabin.bookbasket.domain.use_cases.realtime.DeleteOrders
import com.nabin.bookbasket.domain.use_cases.realtime.DeliveryState
import com.nabin.bookbasket.domain.use_cases.realtime.GetItems
import com.nabin.bookbasket.domain.use_cases.realtime.Insert
import com.nabin.bookbasket.domain.use_cases.realtime.RealtimeUseCases
import com.nabin.bookbasket.domain.use_cases.storage.DeleteImage
import com.nabin.bookbasket.domain.use_cases.storage.FirestorageUseCases
import com.nabin.bookbasket.domain.use_cases.storage.InsertImage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun providePreference(application: Application): Preference {
        return Preference(application)
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageRepository(storage: FirebaseStorage): StorageRepository {
        return StorageRepositoryImpl(storage)
    }

    @Singleton
    @Provides
    fun provideStorageUseCases(repository: StorageRepository) =
        FirestorageUseCases(
            insertImage = InsertImage(repository = repository),
            deleteImage = DeleteImage(repository = repository)
        )

    @Singleton
    @Provides
    fun provideFirebaseFirestoreRepository(
        firestore: FirebaseFirestore,
        preference: Preference
    ): FirestoreRepository {
        return FirestoreRepositoryImpl(firestore, preference)
    }

    @Singleton
    @Provides
    fun provideFirestoreUseCases(repository: FirestoreRepository) =
        FirestoreUseCases(
            setAddress = SetAddress(repository = repository),
            orderFood = OrderBook(repository = repository),
            getFoodOrders = GetBookOrders(repository = repository),
            getFoodOrderDetails = GetBookOrderDetails(repository = repository),
            getFoodItem = GetBookItem(repository = repository),
            getFoods = GetBooks(repository = repository),
            addFood = AddFood(repository = repository),
            removeUserOrder = RemoveUserOrder(repository = repository),
            updateUserHistory = UpdateUserHistory(repository = repository),
            orderDelivered = OrderDelivered(repository = repository),
            updateDeliveredHistory = UpdateDeliveredHistroy(repository = repository),
            getMyHistory = GetMyHistory(repository = repository),
            deleteMyHistory = DeleteMyHistory(repository = repository),
            setFavourite = SetFavourite(repository = repository),
            getFavouriteList = GetFavouriteList(repository = repository),
            getUserProfile = GetUserProfile(repository = repository),
            updateUserProfile = UpdateUserProfile(repository = repository),

            setOneSignalId = SetOneSignalId(repository = repository),
            getOneSignalIds = GetOneSignalIds(repository = repository),
            setNotification = SetNotification(repository = repository),
            getNotification = GetNotification(repository = repository),
            readNotification = ReadNotification(repository = repository),
            deleteNotification = DeleteNotification(repository = repository),
            getAdminNotification = GetAdminNotification(repository = repository),
            setAdminNotification = SetAdminNotification(repository = repository),
            deleteAdminNotification = DeleteAdminNotification(repository = repository),

            addOffer = AddOffer(repository = repository),
            getOffer = GetOffer(repository = repository),
            getCartItems = GetCartItems(repository = repository),
            addToCart = AddToCart(repository = repository),
            deleteCartItem = DeleteCartItem(repository = repository),

            rateIt = RateIt(repository = repository),
            updateRating = UpdateRating(repository = repository),
            getFoodsForUpdate = GetBooksForUpdate(repository = repository),
            getAdminHistories = GetAdminHistories(repository = repository)
        )

    @Singleton
    @Provides
    fun provideFirebaseRealtime(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseRealtimeRepository(realtime: FirebaseDatabase): RealtimeRepository {
        return RealtimeRepositoryImpl(realtime)
    }

    @Singleton
    @Provides
    fun provideFirebaseRealtimeUseCases(repository: RealtimeRepository) =
        RealtimeUseCases(
            insert = Insert(realtimeRepository = repository),
            getItems = GetItems(realtimeRepository = repository),
            deleteOrders = DeleteOrders(realtimeRepository = repository),

            changeDeliveryState = ChangeDeliveryState(realtimeRepository = repository),
            deliveryState = DeliveryState(realtimeRepository = repository)
        )
}