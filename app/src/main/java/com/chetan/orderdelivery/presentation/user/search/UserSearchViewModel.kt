package com.chetan.orderdelivery.presentation.user.search

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
class UserSearchViewModel @Inject constructor(
    private val dbUseCases: DBUseCases
) : ViewModel(){
    private val _state = MutableStateFlow(UserSearchState())
    val state : StateFlow<UserSearchState> = _state

    init {
        viewModelScope.launch {
            val data = dbUseCases.getAllFoods().sortedBy { it.foodName }
            _state.update {
                it.copy(
                    allFoods = data,
                    searchedList = data
                )
            }
        }
    }

    val event : (onEvent: UserSearchEvent) -> Unit = { event ->
        viewModelScope.launch {
            when(event){
                is UserSearchEvent.OnQueryChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = event.value,
                            searchedList = state.value.allFoods.filter { it.foodName.contains(event.value,ignoreCase = false) }

                        )
                    }
                }

                UserSearchEvent.OnQueryCrossClicked -> {
                    _state.update {
                        it.copy(
                            searchQuery = "",
                            searchedList = state.value.allFoods.sortedBy { it.foodName }
                        )
                    }
                }
            }
        }
    }
}