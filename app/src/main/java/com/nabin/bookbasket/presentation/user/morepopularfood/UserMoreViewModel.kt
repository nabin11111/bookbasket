package com.nabin.bookbasket.presentation.user.morepopularfood

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
class UserMoreViewModel @Inject constructor(
    private val dbUseCases: DBUseCases
): ViewModel() {
    private val _state = MutableStateFlow(UserMoreState())
    val state : StateFlow<UserMoreState> = _state

    init {
        viewModelScope.launch {
        val data = dbUseCases.getAllBooks().filter { it.bookType == "Popular" }.sortedBy { it.bookName }
            _state.update {
                it.copy(
                    allBooks = data,
                    searchedList = data
                )
            }
        }
    }

    val onEvent: (event: UserMoreEvent) -> Unit = { event ->
        viewModelScope.launch {
            when(event){
                is UserMoreEvent.OnQueryChange ->{
                    _state.update {
                        it.copy(
                            searchQuery = event.value,
                            searchedList = state.value.allBooks.filter { it.bookName.contains(event.value,ignoreCase = true) }

                        )
                    }
                }
                UserMoreEvent.OnQueryCrossClicked -> {
                    _state.update {
                        it.copy(
                            searchQuery = "",
                            searchedList = state.value.allBooks
                        )
                    }
                }

                is UserMoreEvent.OnFilterChange -> {
                    when(event.value){
                        FilterTypes.Name -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allBooks.sortedBy { it.bookName }
                                )
                            }
                        }
                        FilterTypes.PriceHigh -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allBooks.sortedByDescending { it.bookPrice.toInt() }
                                )
                            }
                        }
                        FilterTypes.PriceLow -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allBooks.sortedBy { it.bookPrice.toInt() }
                                )
                            }
                        }
                        FilterTypes.Rating -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allBooks.sortedByDescending { it.bookRating }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}