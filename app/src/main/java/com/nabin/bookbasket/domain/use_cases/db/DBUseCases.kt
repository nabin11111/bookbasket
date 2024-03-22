package com.nabin.bookbasket.domain.use_cases.db

data class DBUseCases(
    val getAllBooks: GetAllBooks,
    val insertBookList: InsertBookList,

    val getAllIds: GetAllIds,
    val insertIds: InsertIds,


    val getAllCheckoutBooks :GetAllCheckoutBooks,
    val insertAllCheckoutBookList: InsertAllCheckoutBookList,
    val removeAllCheckoutBooks : RemoveAllCheckoutBooks
)
