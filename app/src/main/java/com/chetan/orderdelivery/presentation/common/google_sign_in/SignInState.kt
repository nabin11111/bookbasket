package com.chetan.orderdelivery.presentation.common.google_sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
