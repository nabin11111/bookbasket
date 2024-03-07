package com.chetan.orderdelivery.domain.use_cases.firestore

data class FirestoreUseCases(
    val setAddress: SetAddress,

    val orderFood: OrderFood,
    val getFoods: GetFoods,
    val getFoodItem: GetFoodItem,
    val getFoodOrders : GetFoodOrders,
    val getFoodOrderDetails: GetFoodOrderDetails,
    val removeUserOrder : RemoveUserOrder,
    val updateUserHistory: UpdateUserHistory,
    val orderDelivered : OrderDelivered,
    val updateDeliveredHistory: UpdateDeliveredHistroy,
    val getMyHistory: GetMyHistory,
    val deleteMyHistory: DeleteMyHistory,
    val setFavourite: SetFavourite,
    val getFavouriteList: GetFavouriteList,
    val getUserProfile: GetUserProfile,
    val updateUserProfile: UpdateUserProfile,

    val setOneSignalId: SetOneSignalId,
    val getOneSignalIds: GetOneSignalIds,
    val setNotification : SetNotification,
    val getNotification : GetNotification,
    val readNotification : ReadNotification,
    val deleteNotification: DeleteNotification,
    val getAdminNotification: GetAdminNotification,
    val setAdminNotification: SetAdminNotification,
    val deleteAdminNotification: DeleteAdminNotification,

    val addFood : AddFood,
    val addOffer : AddOffer,
    val getOffer : GetOffer,

    val getCartItems : GetCartItems,
    val deleteCartItem: DeleteCartItem,
    val addToCart : AddToCart,

    val rateIt : RateIt,
    val updateRating: UpdateRating,
    val getFoodsForUpdate : GetFoodsForUpdate,
    val getAdminHistories: GetAdminHistories
)
