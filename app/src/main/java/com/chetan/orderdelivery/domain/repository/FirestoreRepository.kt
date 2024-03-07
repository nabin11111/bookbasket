package com.chetan.orderdelivery.domain.repository

import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.AddFoodRequest
import com.chetan.orderdelivery.data.model.FavouriteModel
import com.chetan.orderdelivery.data.model.GetCartItemModel
import com.chetan.orderdelivery.data.model.RatingRequestResponse
import com.chetan.orderdelivery.data.model.GetFoodResponse
import com.chetan.orderdelivery.data.model.ProfileRequestResponse
import com.chetan.orderdelivery.data.model.SetLatLng
import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.data.model.order.RequestFoodOrder

interface FirestoreRepository {
    suspend fun orderFood(
        data : RequestFoodOrder
    ) : Resource<Boolean>

    suspend fun setAddress(
        address: SetLatLng
    ) : Resource<Boolean>

    suspend fun rating(
        data: RatingRequestResponse
    ) : Resource<Boolean>

    suspend fun getFoods() : Resource<List<GetFoodResponse>>
    suspend fun getFoodItem(foodId: String) : Resource<GetFoodResponse>
    suspend fun getFoodsForUpdate() : Resource<List<GetFoodResponse>>
    suspend fun getRating() : Resource<List<RatingRequestResponse>>

    suspend fun addToCart(foodItem: GetCartItemModel) : Resource<Boolean>
    suspend fun getCartItems() : Resource<List<GetCartItemModel>>
    suspend fun deleteCartItem(foodId: String): Resource<Boolean>
    suspend fun getMyHistory(): Resource<List<RequestFoodOrder>>
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
    suspend fun getFoodOrderDetails(user: String) : Resource<List<RequestFoodOrder>>
    suspend fun addFood(
        data: AddFoodRequest
    ): Resource<Boolean>

    suspend fun orderDelivered(data : RequestFoodOrder): Resource<Boolean>
    suspend fun removeUser(user : String): Resource<Boolean>
    suspend fun updateUserHistory(data : RequestFoodOrder): Resource<Boolean>
    suspend fun updateDeliveredHistroy(data: RequestFoodOrder) : Resource<Boolean>
    suspend fun addOffer(url: String) : Resource<Boolean>
    suspend fun getOffer() : Resource<String>
    suspend fun getAdminHistories() : Resource<List<RequestFoodOrder>>
    suspend fun getAdminNotification(): Resource<List<StoreNotificationRequestResponse>>
    suspend fun setAdminNotification(data: StoreNotificationRequestResponse): Resource<Boolean>
    suspend fun deleteAdminNotification(id: String): Resource<Boolean>

    suspend fun updateRating(
        foodId : String,
        foodRating: Float
    ) : Resource<Boolean>
}