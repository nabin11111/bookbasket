package com.nabin.bookbasket.presentation.admin.addoffer

import android.net.Uri

sealed interface AdminAddOfferEvent{
    data object DismissInfoMsg: AdminAddOfferEvent
    data class OnImageUriToUrl(val value: Uri) : AdminAddOfferEvent
    //    data class OnRemoveImageUri(val value: ImageUrlDetail) : AddFoodEvent
    data object AddFood : AdminAddOfferEvent
}