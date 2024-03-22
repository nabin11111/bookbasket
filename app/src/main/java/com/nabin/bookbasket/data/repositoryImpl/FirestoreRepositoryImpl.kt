package com.nabin.bookbasket.data.repositoryImpl

import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.local.Preference
import com.nabin.bookbasket.data.model.AddFoodRequest
import com.nabin.bookbasket.data.model.FavouriteModel
import com.nabin.bookbasket.data.model.GetCartItemModel
import com.nabin.bookbasket.data.model.RatingRequestResponse
import com.nabin.bookbasket.data.model.GetFoodResponse
import com.nabin.bookbasket.data.model.ProfileRequestResponse
import com.nabin.bookbasket.data.model.SetLatLng
import com.nabin.bookbasket.data.model.StoreNotificationRequestResponse
import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.data.model.order.RequestBookOrder
import com.nabin.bookbasket.domain.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, private val preference: Preference
) : FirestoreRepository {
    override suspend fun orderFood(data: RequestBookOrder): Resource<Boolean> {
        return try {
//            withContext(Dispatchers.IO) {
//                data.map { order ->
//                    async {
//                        firestore.collection("admin")
//                            .document("foods")
//                            .collection("orders")
//                            .document(preference.tableName.toString())
//                            .collection("orders")
//                            .document(order.orderId)
//                            .set(order)
//                            .await()
//                    }
//                }.awaitAll()
//            }

            var success = false
            firestore.collection("admin")
                .document("foods")
                .collection("orders")
                .document(preference.tableName.toString())
                .collection("orderDetails")
                .document(data.orderId)
                .set(data)
                .addOnSuccessListener {
                    success = true
                }.await()
            Resource.Success(success)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setAddress(address: SetLatLng): Resource<Boolean> {
        return try {
            var success = false
            firestore.collection("admin").document("foods").collection("orders")
                .document(preference.tableName.toString()).set(address).addOnSuccessListener {
                    success = true
                }.await()
            Resource.Success(success)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun rating(data: RatingRequestResponse): Resource<Boolean> {
        return try {
            firestore.collection("admin").document("foods").collection("foods")
                .document(data.foodId).collection("rating").document(data.userMail).set(data)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFoods(): Resource<List<GetFoodResponse>> {
        return try {
            val foodResponse = mutableListOf<GetFoodResponse>()
            val documentRef =
                firestore.collection("admin").document("foods").collection("foods").get().await()
            for (document in documentRef.documents) {
                val data = document.toObject<GetFoodResponse>()
                data?.let {
                    foodResponse.add(data)
                }
            }
            Resource.Success(foodResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFoodItem(foodId: String): Resource<GetFoodResponse> {
        return try {
            val foodItem: GetFoodResponse
            val document =
                firestore.collection("admin").document("foods").collection("foods").document(foodId)
                    .get().await().toObject<GetFoodResponse>()
            foodItem = document ?: GetFoodResponse()
            Resource.Success(foodItem)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFoodsForUpdate(): Resource<List<GetFoodResponse>> {
        return try {
            val foodResponse = mutableListOf<GetFoodResponse>()
            val ratingResponseList = mutableListOf<RatingRequestResponse>()
            val documentRef =
                firestore.collection("admin").document("foods").collection("foods").get().await()
            for (document in documentRef.documents) {
                val data = document.toObject<GetFoodResponse>()
                data?.let {
                    val ratingRef =
                        firestore.collection("admin").document("foods").collection("foods")
                            .document(it.foodId).collection("rating").get().await()

                    for (documentRating in ratingRef.documents) {
                        val newData = documentRating.toObject<RatingRequestResponse>()
                        newData?.let { ratingResponse ->
                            ratingResponseList.add(newData)
                        }
                    }
                    val newValue = ratingResponseList.sumOf { it.rateValue.toInt() }
                        .toFloat() / ratingResponseList.size.toFloat()
                    foodResponse.add(data.copy(newFoodRating = newValue))
                }
                ratingResponseList.clear()
            }
            Resource.Success(foodResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getRating(): Resource<List<RatingRequestResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToCart(foodItem: GetCartItemModel): Resource<Boolean> {
        return try {
            firestore.collection("users").document(preference.tableName!!).collection("myCart")
                .document(foodItem.foodId).set(foodItem).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getCartItems(): Resource<List<GetCartItemModel>> {
        return try {
            val cartItemlist = mutableListOf<GetCartItemModel>()
            val docRef =
                firestore.collection("users").document(preference.tableName!!).collection("myCart")
                    .get().await()

            for (document in docRef) {
                val data = document.toObject<GetCartItemModel>()
                data?.let {
                    cartItemlist.add(data)
                }
            }
            Resource.Success(cartItemlist)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteCartItem(foodId: String): Resource<Boolean> {
        return try {
            var isSuccess = false
            firestore.collection("users").document(preference.tableName!!).collection("myCart")
                .document(foodId).delete().addOnSuccessListener {
                    isSuccess = true
                }.await()
            Resource.Success(isSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getMyHistory(): Resource<List<RequestBookOrder>> {
        return try {
            val orderList = mutableListOf<RequestBookOrder>()
            val querySnapshot = firestore.collection("users").document(preference.tableName ?: "")
                .collection("history").get().await()
            for (document in querySnapshot.documents) {
                val order = document.toObject<RequestBookOrder>()
                order?.let {
                    orderList.add(it)
                }
            }
            Resource.Success(orderList.reversed())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteMyHistory(orderId: String): Resource<Boolean> {
        return try {
            var isSuccess = false
            firestore.collection("users").document(preference.tableName!!).collection("history")
                .document(orderId).delete().addOnSuccessListener {
                    isSuccess = true
                }.await()
            Resource.Success(isSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setFavourite(
        isFavourite: Boolean, foodId: String
    ): Resource<Boolean> {
        return try {
            var isDone: Boolean
            if (isFavourite) {
                firestore.collection("users").document(preference.tableName!!)
                    .collection("favourite").document(foodId).set(FavouriteModel(foodId)).await()
                isDone = true
            } else {
                firestore.collection("users").document(preference.tableName!!)
                    .collection("favourite").document(foodId).delete().await()
                isDone = true
            }
            Resource.Success(isDone)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFavouriteList(): Resource<List<FavouriteModel>> {
        return try {
            val favList = mutableListOf<FavouriteModel>()
            val coll = firestore.collection("users").document(preference.tableName ?: "")
                .collection("favourite").get().await()
            for (document in coll.documents) {
                val data = document.toObject<FavouriteModel>()
                data?.let {
                    favList.add(it)
                }
            }
            Resource.Success(favList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUserProfile(data: ProfileRequestResponse): Resource<Boolean> {
        return try {
            var isSuccess = false
            firestore
                .collection("users")
                .document(preference.tableName?:"test")
                .collection("profile")
                .document("profile")
                .set(data)
                .addOnSuccessListener {
                    isSuccess = true
                }.addOnFailureListener {
                    isSuccess = false
                }.await()
            Resource.Success(isSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserProfile(user: String): Resource<ProfileRequestResponse> {
        return try {
            val coll = firestore.collection("users")
                .document(user)
                .collection("profile")
                .document("profile")
                .get()
                .await()
                .toObject<ProfileRequestResponse>()
            if (coll != null){
                Resource.Success(coll)
            }else{
                Resource.Failure(java.lang.Exception("NO data yet"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setOneSignalId(data: SetOneSignalId): Resource<Boolean> {
        return try {
            var isSuccess = false
            firestore.collection("admin").document("oneSignal").collection(data.branch)
                .document(data.id).set(data).addOnSuccessListener {
                    isSuccess = true
                }.addOnFailureListener {
                    isSuccess = false
                }.await()


            Resource.Success(isSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getOneSignalIds(branch: String): Resource<List<SetOneSignalId>> {
        return try {
            val ids = mutableListOf<SetOneSignalId>()
            val coll = firestore.collection("admin")
                .document("oneSignal")
                .collection(branch)
                .get()
                .await()
            for (document in coll.documents) {
                val data = document.toObject<SetOneSignalId>()
                data?.let {
                    ids.add(data)
                }
            }
            Resource.Success(ids)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setNotification(data: StoreNotificationRequestResponse): Resource<Boolean> {
        return try {
            var isSuccess = false
            firestore.collection("users")
                .document(preference.tableName!!)
                .collection("notifications")
                .document(data.time)
                .set(data)
                .addOnSuccessListener {
                    isSuccess = true
                }.addOnFailureListener {
                    isSuccess = false
                }.await()
            Resource.Success(isSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }

    override suspend fun getNotification(): Resource<List<StoreNotificationRequestResponse>> {
        return try {
            val noticeList = mutableListOf<StoreNotificationRequestResponse>()
            val querySnapshot =
                firestore
                    .collection("users")
                    .document(preference.tableName!!)
                    .collection("notifications")
                    .get()
                    .await()
            for (document in querySnapshot.documents) {
                val order = document.toObject<StoreNotificationRequestResponse>()
                order?.let {
                    noticeList.add(it)
                }
            }
            Resource.Success(noticeList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun readNotification(id: String): Resource<Boolean> {
        return try {
            var read = false
            firestore
                .collection("users")
                .document(preference.tableName ?: "test")
                .collection("notifications")
                .document(id)
                .update("readNotice", true)
                .addOnSuccessListener {
                    read = true
                }
                .addOnFailureListener {
                    read = false
                }
            Resource.Success(read)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteNotification(id: String): Resource<Boolean> {
        return try {
            var read = false
            firestore
                .collection("users")
                .document(preference.tableName ?: "test")
                .collection("notifications")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    read = true
                }
                .addOnFailureListener {
                    read = false
                }
            Resource.Success(read)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    //admin
    override suspend fun getFoodOrders(): Resource<List<SetLatLng>> {
        return try {
            val orderList = mutableListOf<SetLatLng>()
            val querySnapshot =
                firestore.collection("admin").document("foods").collection("orders").get().await()
            for (document in querySnapshot.documents) {
                val order = document.toObject<SetLatLng>()
                order?.let {
                    orderList.add(it)
                }
            }
            Resource.Success(orderList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getFoodOrderDetails(user: String): Resource<List<RequestBookOrder>> {
        return try {
            val orderDetailsList = mutableListOf<RequestBookOrder>()
            val documentRef =
                firestore.collection("admin").document("foods").collection("orders").document(user)
                    .collection("orderDetails").get().await()
            for (document in documentRef.documents) {
                val data = document.toObject<RequestBookOrder>()
                data?.let {
                    orderDetailsList.add(data)
                }
            }
            Resource.Success(orderDetailsList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addFood(data: AddFoodRequest): Resource<Boolean> {
        return try {
            firestore.collection("admin").document("foods").collection("foods")
                .document(data.foodId).set(data).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun orderDelivered(data: RequestBookOrder): Resource<Boolean> {
        return try {
            var isTrue = false
            val ref = firestore.collection("admin").document("foods").collection("orders")
                .document(data.userMail).collection("orderDetails").document(data.orderId).delete()
                .addOnSuccessListener {
                    isTrue = true
                }.addOnFailureListener {
                    isTrue = false
                }.await()
            Resource.Success(isTrue)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun removeUser(user: String): Resource<Boolean> {
        return try {
            var isTrue = false
            val ref =
                firestore.collection("admin").document("foods").collection("orders").document(user)
                    .delete().addOnSuccessListener {
                        isTrue = true
                    }.addOnFailureListener {
                        isTrue = false
                    }.await()
            Resource.Success(isTrue)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateUserHistory(data: RequestBookOrder): Resource<Boolean> {
        return try {
            var isTrue = false
            val ref = firestore.collection("users").document(data.userMail).collection("history")
                .document(data.orderId).set(data).addOnSuccessListener {
                    isTrue = true
                }.addOnFailureListener {
                    isTrue = false
                }.await()
            Resource.Success(isTrue)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateDeliveredHistroy(data: RequestBookOrder): Resource<Boolean> {
        return try {
            var isTrue = false
            val ref = firestore.collection("admin").document("foods").collection("orderHistory")
                .document(data.orderId).set(data).addOnSuccessListener {
                    isTrue = true
                }.addOnFailureListener {
                    isTrue = false
                }.await()
            Resource.Success(isTrue)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addOffer(url: String): Resource<Boolean> {
        return try {
            var isTrue = false
            val ref = firestore
                .collection("admin")
                .document("foods")
                .collection("offer")
                .document("offer")
                .set(mapOf("url" to url)).addOnSuccessListener {
                    isTrue = true
                }.addOnFailureListener {
                    isTrue = false
                }.await()
            Resource.Success(isTrue)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getOffer(): Resource<String> {
        return try {
            val ref = firestore
                .collection("admin")
                .document("foods")
                .collection("offer")
                .document("offer")
                .get()
                .await()

            Resource.Success(ref.data?.get("url") as String? ?:"")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAdminHistories(): Resource<List<RequestBookOrder>> {
        return try {
            var list = mutableListOf<RequestBookOrder>()
            val ref = firestore.collection("admin")
                .document("foods")
                .collection("orderHistory")
                .get()
                .await()
            for (document in ref.documents){
                val data = document.toObject<RequestBookOrder>()
                data?.let {
                    list.add(it)
                }
            }
            Resource.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getAdminNotification(): Resource<List<StoreNotificationRequestResponse>> {
        return try {
            val noticeList = mutableListOf<StoreNotificationRequestResponse>()
            val querySnapshot =
                firestore
                    .collection("admin")
                    .document("notification")
                    .collection("notifications")
                    .get()
                    .await()
            for (document in querySnapshot.documents) {
                val order = document.toObject<StoreNotificationRequestResponse>()
                order?.let {
                    noticeList.add(it)
                }
            }
            Resource.Success(noticeList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setAdminNotification(data: StoreNotificationRequestResponse): Resource<Boolean> {
        return try {
            var isSuccess = false
            firestore.collection("admin")
                .document("notification")
                .collection("notifications")
                .document(data.time)
                .set(data)
                .addOnSuccessListener {
                    isSuccess = true
                }.addOnFailureListener {
                    isSuccess = false
                }.await()
            Resource.Success(isSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteAdminNotification(id: String): Resource<Boolean> {
        return try {
            var read = false
            firestore
                .collection("admin")
                .document("notification")
                .collection("notifications")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    read = true
                }
                .addOnFailureListener {
                    read = false
                }
            Resource.Success(read)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }



    override suspend fun updateRating(foodId: String, foodRating: Float): Resource<Boolean> {
        return try {
            firestore.collection("admin").document("foods").collection("foods").document(foodId)
                .update("foodRating", foodRating).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
