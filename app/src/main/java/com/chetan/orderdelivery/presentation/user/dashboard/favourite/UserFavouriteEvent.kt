package com.chetan.orderdelivery.presentation.user.dashboard.favourite

sealed interface UserFavouriteEvent{
    data object OrderAgain: UserFavouriteEvent
    data class RemoveFavourite(val id: String): UserFavouriteEvent

}