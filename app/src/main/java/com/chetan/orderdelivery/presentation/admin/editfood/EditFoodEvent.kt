package com.chetan.orderdelivery.presentation.admin.editfood

import android.net.Uri

sealed interface EditFoodEvent{
    data object DismissInfoMsg: EditFoodEvent
    data class OnSelectedFoodTypeChange(val value: String) : EditFoodEvent
    data class OnSelectedFoodFamilyChange(val value: String) : EditFoodEvent
    data class OnFoodNameChange(val value: String) : EditFoodEvent
    data class OnFoodDetailsChange(val value: String) : EditFoodEvent
    data class OnFoodPriceChange(val value: String) : EditFoodEvent
    data class OnFoodDiscountChange(val value: String) : EditFoodEvent
    data class OnImageUriToUrl(val value: Uri, val name: Int) : EditFoodEvent
    data class GetFoodItemDetails(val value: String) : EditFoodEvent

    //    data class OnRemoveImageUri(val value: ImageUrlDetail) : EditFoodEvent
    data object AddFood : EditFoodEvent
}