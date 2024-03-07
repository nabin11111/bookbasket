package com.nabin.bookbasket.presentation.common.utils

import androidx.navigation.NavHostController

object CleanNavigate {
    fun NavHostController.cleanNavigate(
        toRoute: String,
        popUpTo: String? = currentDestination ?.route
    ){
        navigate(route = toRoute){
            popUpTo?.let {
                popUpTo(it){
                    inclusive = true
                }
            }
        }

    }
}