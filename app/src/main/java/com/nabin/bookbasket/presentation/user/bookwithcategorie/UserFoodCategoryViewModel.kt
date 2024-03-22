package com.nabin.bookbasket.presentation.user.bookwithcategorie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin.bookbasket.domain.use_cases.db.DBUseCases
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
    private val _state = MutableStateFlow(UserBookCategoryState())
    val state : StateFlow<UserBookCategoryState> = _state

    init {
        viewModelScope.launch {
            val data = dbUseCases.getAllFoods().sortedBy { it.foodFamily }
            _state.update {
                it.copy(
                    allBooks = data,
                    bookTypesList = data.map { it.foodFamily }.distinct()
                )
            }
        }
    }
    val event : (onEvent: UserBookCategoryEvent) -> Unit = { event ->
        viewModelScope.launch {
            when(event){
                UserBookCategoryEvent.DismissInfoMsg -> {

                }
            }
        }
    }
}