package com.nabin.bookbasket.presentation.user.bookwithcategorie


sealed interface UserBookCategoryEvent{
    data object DismissInfoMsg: UserBookCategoryEvent
}