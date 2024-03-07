package com.chetan.orderdelivery.presentation.user.foodorderdescription

sealed interface FoodOrderDescriptionEvent{
    data object OrderFood: FoodOrderDescriptionEvent
    data object DismissInfoMsg : FoodOrderDescriptionEvent
    data class AddToCart(val value: String): FoodOrderDescriptionEvent
    data class GetFoodItemDetails(val value: String) : FoodOrderDescriptionEvent
    data object IncreaseQuantity : FoodOrderDescriptionEvent
    data object DecreaseQuantity : FoodOrderDescriptionEvent
    data class SetFavourite(val foodId: String, val isFav: Boolean) : FoodOrderDescriptionEvent
}