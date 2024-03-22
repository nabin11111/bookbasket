package com.nabin.bookbasket.presentation.user.bookwithcategories


sealed interface UserBookCategoryEvent{
    data object DismissInfoMsg: UserBookCategoryEvent
}