package com.nabin.bookbasket.presentation.admin.dashboard.allFoods

import com.nabin.bookbasket.presentation.user.morepopularfood.FilterTypes

sealed interface AllBookEvent {
    data class EditFood(val id: String): AllBookEvent
    data class OnQueryChange(val value: String): AllBookEvent
    data object OnQueryCrossClicked: AllBookEvent
    data class OnFilterChange( val value : FilterTypes): AllBookEvent
}