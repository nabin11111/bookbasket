package com.nabin.bookbasket.presentation.user.foodorderdescription

sealed interface BookOrderDescriptionEvent{
    data object OrderFood: BookOrderDescriptionEvent
    data object DismissInfoMsg : BookOrderDescriptionEvent
    data class AddToCart(val value: String): BookOrderDescriptionEvent
    data class GetFoodItemDetails(val value: String) : BookOrderDescriptionEvent
    data object IncreaseQuantity : BookOrderDescriptionEvent
    data object DecreaseQuantity : BookOrderDescriptionEvent
    data class SetFavourite(val foodId: String, val isFav: Boolean) : BookOrderDescriptionEvent
}