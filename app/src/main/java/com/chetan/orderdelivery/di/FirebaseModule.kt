package com.chetan.orderdelivery.di

import android.app.Application
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.data.repositoryImpl.FirestoreRepositoryImpl
import com.chetan.orderdelivery.data.repositoryImpl.RealtimeRepositoryImpl
import com.chetan.orderdelivery.data.repositoryImpl.StorageRepositoryImpl
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import com.chetan.orderdelivery.domain.repository.RealtimeRepository
import com.chetan.orderdelivery.domain.repository.StorageRepository
import com.chetan.orderdelivery.domain.use_cases.firestore.AddFood
import com.chetan.orderdelivery.domain.use_cases.firestore.AddOffer
import com.chetan.orderdelivery.domain.use_cases.firestore.AddToCart
import com.chetan.orderdelivery.domain.use_cases.firestore.DeleteAdminNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.DeleteCartItem
import com.chetan.orderdelivery.domain.use_cases.firestore.DeleteMyHistory
import com.chetan.orderdelivery.domain.use_cases.firestore.DeleteNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.GetAdminHistories
import com.chetan.orderdelivery.domain.use_cases.firestore.GetAdminNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.GetCartItems
import com.chetan.orderdelivery.domain.use_cases.firestore.GetFavouriteList
import com.chetan.orderdelivery.domain.use_cases.firestore.GetFoodItem
import com.chetan.orderdelivery.domain.use_cases.firestore.GetFoodOrderDetails
import com.chetan.orderdelivery.domain.use_cases.firestore.GetFoodOrders
import com.chetan.orderdelivery.domain.use_cases.firestore.GetFoods
import com.chetan.orderdelivery.domain.use_cases.firestore.GetFoodsForUpdate
import com.chetan.orderdelivery.domain.use_cases.firestore.GetMyHistory
import com.chetan.orderdelivery.domain.use_cases.firestore.GetNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.GetOffer
import com.chetan.orderdelivery.domain.use_cases.firestore.GetOneSignalIds
import com.chetan.orderdelivery.domain.use_cases.firestore.GetUserProfile
import com.chetan.orderdelivery.domain.use_cases.firestore.OrderDelivered
import com.chetan.orderdelivery.domain.use_cases.firestore.OrderFood
import com.chetan.orderdelivery.domain.use_cases.firestore.RateIt
import com.chetan.orderdelivery.domain.use_cases.firestore.ReadNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.RemoveUserOrder
import com.chetan.orderdelivery.domain.use_cases.firestore.SetAddress
import com.chetan.orderdelivery.domain.use_cases.firestore.SetAdminNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.SetFavourite
import com.chetan.orderdelivery.domain.use_cases.firestore.SetNotification
import com.chetan.orderdelivery.domain.use_cases.firestore.SetOneSignalId
import com.chetan.orderdelivery.domain.use_cases.firestore.UpdateDeliveredHistroy
import com.chetan.orderdelivery.domain.use_cases.firestore.UpdateRating
import com.chetan.orderdelivery.domain.use_cases.firestore.UpdateUserHistory
import com.chetan.orderdelivery.domain.use_cases.firestore.UpdateUserProfile
import com.chetan.orderdelivery.domain.use_cases.realtime.ChangeDeliveryState
import com.chetan.orderdelivery.domain.use_cases.realtime.DeleteOrders
import com.chetan.orderdelivery.domain.use_cases.realtime.DeliveryState
import com.chetan.orderdelivery.domain.use_cases.realtime.GetItems
import com.chetan.orderdelivery.domain.use_cases.realtime.Insert
import com.chetan.orderdelivery.domain.use_cases.realtime.RealtimeUseCases
import com.chetan.orderdelivery.domain.use_cases.storage.DeleteImage
import com.chetan.orderdelivery.domain.use_cases.storage.FirestorageUseCases
import com.chetan.orderdelivery.domain.use_cases.storage.InsertImage
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
            orderFood = OrderFood(repository = repository),
            getFoodOrders = GetFoodOrders(repository = repository),
            getFoodOrderDetails = GetFoodOrderDetails(repository = repository),
            getFoodItem = GetFoodItem(repository = repository),
            getFoods = GetFoods(repository = repository),
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
            getFoodsForUpdate = GetFoodsForUpdate(repository = repository),
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