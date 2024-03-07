package com.chetan.orderdelivery.presentation.user.profile

sealed interface UserProfileEvent{
    data object UpdateProfile: UserProfileEvent
    data object DismissInfoMsg: UserProfileEvent
    data class OnNameChange(val value: String): UserProfileEvent
    data class OnAddressChange(val value: String): UserProfileEvent
    data class OnPhoneChange(val value: String): UserProfileEvent
}