package com.chetan.orderdelivery.presentation.admin.dashboard.allFoods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import com.chetan.orderdelivery.presentation.user.morepopularfood.FilterTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllFoodViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases
): ViewModel() {
    private val _state = MutableStateFlow(AllFoodState())
    val state: StateFlow<AllFoodState> = _state
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
                    val data = getAllFoodsResponse.data.sortedByDescending { it.foodRating }
                    dbUseCases.insertFoodList(data.map {
                        AllFoods(
                            foodId = it.foodId,
                            foodType = it.foodType,
                            foodFamily = it.foodFamily,
                            foodName = it.foodName,
                            foodDetails = it.foodDetails,
                            foodPrice = it.foodPrice,
                            foodDiscount = it.foodDiscount,
                            foodNewPrice = it.foodNewPrice,
                            foodRating = it.foodRating,
                            newFoodRating = it.newFoodRating,
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
                            allFoods = data,
                            searchedList = data,
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }

    val onEvent :(event: AllFoodEvent) -> Unit = {event ->
        viewModelScope.launch {
            when(event){
                is AllFoodEvent.EditFood -> {

                }

                is AllFoodEvent.OnFilterChange -> {
                    when(event.value){
                        FilterTypes.Name -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allFoods.sortedBy { it.foodName }
                                )
                            }
                        }
                        FilterTypes.PriceHigh -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allFoods.sortedByDescending { it.foodPrice.toInt() }
                                )
                            }
                        }
                        FilterTypes.PriceLow -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allFoods.sortedBy { it.foodPrice.toInt() }
                                )
                            }
                        }
                        FilterTypes.Rating -> {
                            _state.update {
                                it.copy(
                                    searchedList = state.value.allFoods.sortedByDescending { it.foodRating }
                                )
                            }
                        }
                    }
                }
                is AllFoodEvent.OnQueryChange -> {
                    _state.update {
                        it.copy(
                            searchQuery = event.value,
                            searchedList = state.value.allFoods.filter { it.foodName.contains(event.value) }

                        )
                    }
                }
                AllFoodEvent.OnQueryCrossClicked -> {
                    _state.update {
                        it.copy(
                            searchQuery = "",
                            searchedList = state.value.allFoods
                        )
                    }
                }
            }
        }
    }
}