package com.nabin.bookbasket.domain.repository

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.AddBookRequest
import com.nabin.bookbasket.data.model.FavouriteModel
import com.nabin.bookbasket.data.model.GetCartItemModel
import com.nabin.bookbasket.data.model.RatingRequestResponse
import com.nabin.bookbasket.data.model.GetBookResponse
import com.nabin.bookbasket.data.model.ProfileRequestResponse
import com.nabin.bookbasket.data.model.SetLatLng
import com.nabin.bookbasket.data.model.StoreNotificationRequestResponse
import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.data.model.order.RequestBookOrder

interface FirestoreRepository {
    suspend fun orderFood(
        data : RequestBookOrder
    ) : Resource<Boolean>

    suspend fun setAddress(
        address: SetLatLng
    ) : Resource<Boolean>

    suspend fun rating(
        data: RatingRequestResponse
    ) : Resource<Boolean>

    suspend fun getFoods() : Resource<List<GetBookResponse>>
    suspend fun getFoodItem(foodId: String) : Resource<GetBookResponse>
    suspend fun getFoodsForUpdate() : Resource<List<GetBookResponse>>
    suspend fun getRating() : Resource<List<RatingRequestResponse>>

    suspend fun addToCart(foodItem: GetCartItemModel) : Resource<Boolean>
    suspend fun getCartItems() : Resource<List<GetCartItemModel>>
    suspend fun deleteCartItem(foodId: String): Resource<Boolean>
    suspend fun getMyHistory(): Resource<List<RequestBookOrder>>
    suspend fun deleteMyHistory(orderId: String): Resource<Boolean>

    suspend fun setFavourite(isFavourite: Boolean = false, foodId: String): Resource<Boolean>
    suspend fun getFavouriteList(): Resource<List<FavouriteModel>>

    suspend fun updateUserProfile(
        data: ProfileRequestResponse
    ): Resource<Boolean>
    suspend fun getUserProfile(
        user: String
    ): Resource<ProfileRequestResponse>

    //common

    suspend fun setOneSignalId(data : SetOneSignalId) : Resource<Boolean>
    suspend fun getOneSignalIds( branch: String ) : Resource<List<SetOneSignalId>>
    suspend fun setNotification(data: StoreNotificationRequestResponse): Resource<Boolean>
    suspend fun getNotification(): Resource<List<StoreNotificationRequestResponse>>
    suspend fun readNotification(id: String): Resource<Boolean>
    suspend fun deleteNotification(id: String): Resource<Boolean>


    //admin
    suspend fun getFoodOrders(): Resource<List<SetLatLng>>
    suspend fun getFoodOrderDetails(user: String) : Resource<List<RequestBookOrder>>
    suspend fun addFood(
        data: AddBookRequest
    ): Resource<Boolean>

    suspend fun orderDelivered(data : RequestBookOrder): Resource<Boolean>
    suspend fun removeUser(user : String): Resource<Boolean>
    suspend fun updateUserHistory(data : RequestBookOrder): Resource<Boolean>
    suspend fun updateDeliveredHistroy(data: RequestBookOrder) : Resource<Boolean>
    suspend fun addOffer(url: String) : Resource<Boolean>
    suspend fun getOffer() : Resource<String>
    suspend fun getAdminHistories() : Resource<List<RequestBookOrder>>
    suspend fun getAdminNotification(): Resource<List<StoreNotificationRequestResponse>>
    suspend fun setAdminNotification(data: StoreNotificationRequestResponse): Resource<Boolean>
    suspend fun deleteAdminNotification(id: String): Resource<Boolean>

    suspend fun updateRating(
        foodId : String,
        foodRating: Float
    ) : Resource<Boolean>
}