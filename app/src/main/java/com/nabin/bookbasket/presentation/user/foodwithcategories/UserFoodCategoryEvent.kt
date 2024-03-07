package com.nabin.bookbasket.presentation.user.foodwithcategories


sealed interface UserFoodCategoryEvent{
    data object DismissInfoMsg: UserFoodCategoryEvent
}