package com.nabin.bookbasket.presentation.admin.dashboard.allFoods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin.bookbasket.R
import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.domain.use_cases.db.DBUseCases
import com.nabin.bookbasket.domain.use_cases.firestore.FirestoreUseCases
import com.nabin.bookbasket.presentation.common.components.dialogs.Message
import com.nabin.bookbasket.presentation.user.morepopularfood.FilterTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllBookViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases
): ViewModel() {
    private val _state = MutableStateFlow(AllBookState())
    val state: StateFlow<AllBookState> = _state
    init {
        getAllFoods()
    }
    fun getAllFoods() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        lottieImage = R.raw.loading_food,
                        yesNoRequired = false,
                        isCancellable = false,
                        title = "Loading",
                        description = "Please Wait... Getting all foods"
                    )
                )
            }
            when (val getAllFoodsResponse = firestoreUseCases.getFoods()) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    val data = getAllFoodsResponse.data.sortedByDescending { it.bookRating }
                    dbUseCases.insertBookList(data.map {
                        AllBooks(
                            bookId = it.bookId,
                            bookType = it.bookType,
                            bookFamily = it.bookFamily,
                            bookName = it.bookName,
                            bookDetails = it.bookDetails,
                            bookPrice = it.bookPrice,
                            bookDiscount = it.bookDiscount,
                            bookNewPrice = it.bookNewPrice,
                            bookRating = it.bookRating,
                            newBookRating = it.newBookRating,
                            date = it.date,
                            faceImgName = it.faceImgName,
                            faceImgUrl = it.faceImgUrl,
                            faceImgPath = it.faceImgPath,

                            supportImgName2 = it.supportImgName2,
                            supportImgUrl2 = it.supportImgUrl2,
                            supportImgPath2 = it.supportImgPath2,

                            supportImgName3 = it.supportImgName3,
                            supportImgUrl3 = it.supportImgUrl3,
                            supportImgPath3 = it.supportImgPath3,

                            supportImgName4 = it.supportImgName4,
                            supportImgUrl4 = it.supportImgUrl4,
                            supportImgPath4 = it.supportImgPath4,
                        )
                    })
                    _state.update {
                        it.copy(
                            allBooks = data,
                            searchedList = data,
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }

    val onEvent :(event: AllBookEvent) -> Unit = { event ->
        viewModelScope.launch {
            when(event){
                is AllBookEvent.EditFood -> {

                }

                is AllBookEvent.OnFilterChange -> {
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
                is AllBookEvent.OnQueryChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = event.value,
                            searchedList = state.value.allBooks.filter { it.bookName.contains(event.value) }

                        )
                    }
                }
                AllBookEvent.OnQueryCrossClicked -> {
                    _state.update {
                        it.copy(
                            searchQuery = "",
                            searchedList = state.value.allBooks
                        )
                    }
                }
            }
        }
    }
}