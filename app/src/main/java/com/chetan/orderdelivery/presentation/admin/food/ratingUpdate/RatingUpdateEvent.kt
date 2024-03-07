package com.chetan.orderdelivery.presentation.admin.food.ratingUpdate

sealed interface RatingUpdateEvent{
    data object DismissInfoMsg: RatingUpdateEvent
    data class UpdateThis(val foodId : String, val foodRating: Float) : RatingUpdateEvent
}