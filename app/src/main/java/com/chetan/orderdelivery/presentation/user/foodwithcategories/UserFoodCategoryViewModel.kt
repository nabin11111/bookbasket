package com.chetan.orderdelivery.presentation.user.foodwithcategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.presentation.user.dashboard.UserDashboardEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFoodCategoryViewModel @Inject constructor(
    private val dbUseCases: DBUseCases
) : ViewModel(){
    private val _state = MutableStateFlow(UserFoodCategoryState())
    val state : StateFlow<UserFoodCategoryState> = _state

    init {
        viewModelScope.launch {
            val data = dbUseCases.getAllFoods().sortedBy { it.foodFamily }
            _state.update {
                it.copy(
                    allFoods = data,
                    foodTypesList = data.map { it.foodFamily }.distinct()
                )
            }
        }
    }
    val event : (onEvent: UserFoodCategoryEvent) -> Unit = { event ->
        viewModelScope.launch {
            when(event){
                UserFoodCategoryEvent.DismissInfoMsg -> {

                }
            }
        }
    }
}