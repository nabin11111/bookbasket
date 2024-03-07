package com.chetan.orderdelivery.presentation.admin.food.addfood

import android.net.Uri

sealed interface AddFoodEvent {
    data object DismissInfoMsg: AddFoodEvent
    data class OnSelectedFoodTypeChange(val value: String) : AddFoodEvent
    data class OnSelectedFoodFamilyChange(val value: String) : AddFoodEvent
    data class OnFoodNameChange(val value: String) : AddFoodEvent
    data class OnFoodDetailsChange(val value: String) : AddFoodEvent
    data class OnFoodPriceChange(val value: String) : AddFoodEvent
    data class OnFoodDiscountChange(val value: String) : AddFoodEvent
    data class OnImageUriToUrl(val value: Uri,val name: Int) : AddFoodEvent
//    data class OnRemoveImageUri(val value: ImageUrlDetail) : AddFoodEvent
    data object AddFood : AddFoodEvent

}