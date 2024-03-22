package com.nabin.bookbasket.presentation.user.foodwithcategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin.bookbasket.domain.use_cases.db.DBUseCases
import com.nabin.bookbasket.presentation.user.bookwithcategories.UserBookCategoryEvent
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
            val data = dbUseCases.getAllBooks().sortedBy { it.bookFamily }
            _state.update {
                it.copy(
                    allBooks = data,
                    bookTypesList = data.map { it.bookFamily }.distinct()
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