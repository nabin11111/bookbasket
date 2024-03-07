package com.nabin.bookbasket.presentation.admin.dashboard.allFoods

import com.nabin.bookbasket.presentation.user.morepopularfood.FilterTypes

sealed interface AllFoodEvent {
    data class EditFood(val id: String): AllFoodEvent
    data class OnQueryChange(val value: String): AllFoodEvent
    data object OnQueryCrossClicked: AllFoodEvent
    data class OnFilterChange( val value : FilterTypes): AllFoodEvent
}