package com.chetan.orderdelivery.presentation.user.foodwithcategories


sealed interface UserFoodCategoryEvent{
    data object DismissInfoMsg: UserFoodCategoryEvent
}