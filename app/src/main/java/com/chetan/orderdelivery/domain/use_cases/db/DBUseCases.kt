package com.chetan.orderdelivery.domain.use_cases.db

data class DBUseCases(
    val getAllFoods: GetAllFoods,
    val insertFoodList: InsertFoodList,

    val getAllIds: GetAllIds,
    val insertIds: InsertIds,


    val getAllCheckoutFoods :GetAllCheckoutFoods,
    val insertAllCheckoutFoodList: InsertAllCheckoutFoodList,
    val removeAllCheckoutFoods : RemoveAllCheckoutFoods
)
