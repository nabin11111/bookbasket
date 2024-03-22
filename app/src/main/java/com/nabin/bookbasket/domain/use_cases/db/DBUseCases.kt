package com.nabin.bookbasket.domain.use_cases.db

data class DBUseCases(
    val getAllFoods: GetAllBooks,
    val insertFoodList: InsertBookList,

    val getAllIds: GetAllIds,
    val insertIds: InsertIds,


    val getAllCheckoutFoods :GetAllCheckoutBooks,
    val insertAllCheckoutFoodList: InsertAllCheckoutBookList,
    val removeAllCheckoutFoods : RemoveAllCheckoutBooks
)
