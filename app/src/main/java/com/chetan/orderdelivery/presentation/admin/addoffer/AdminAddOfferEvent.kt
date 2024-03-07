package com.chetan.orderdelivery.presentation.admin.addoffer

import android.net.Uri
import com.chetan.orderdelivery.presentation.admin.food.addfood.AddFoodEvent

sealed interface AdminAddOfferEvent{
    data object DismissInfoMsg: AdminAddOfferEvent
    data class OnImageUriToUrl(val value: Uri) : AdminAddOfferEvent
    //    data class OnRemoveImageUri(val value: ImageUrlDetail) : AddFoodEvent
    data object AddFood : AdminAddOfferEvent
}