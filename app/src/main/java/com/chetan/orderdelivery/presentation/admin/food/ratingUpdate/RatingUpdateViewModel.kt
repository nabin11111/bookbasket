package com.chetan.orderdelivery.presentation.admin.food.ratingUpdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingUpdateViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RatingUpdateState())
    val state: StateFlow<RatingUpdateState> = _state

    init {
        getFoods()
    }
    val onEvent: (event: RatingUpdateEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is RatingUpdateEvent.UpdateThis -> {

                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                lottieImage = R.raw.rating_update,
                                yesNoRequired = false,
                                isCancellable = false,
                                description = "Updating..."
                            )
                        )
                    }

                    val updateRating = repository.updateRating(
                        foodId = event.foodId,
                        foodRating = event.foodRating
                    )
                    when (updateRating) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    infoMsg = null
                                )
                            }
                        }
                    }
                }

                RatingUpdateEvent.DismissInfoMsg ->{
                    _state.update {
                        it.copy(infoMsg = null)
                    }
                }
            }
        }
    }
    private fun getFoods() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        lottieImage = R.raw.loading_food,
                        yesNoRequired = false,
                        isCancellable = false,
                        description = "Loading..."
                    )
                )
            }

            val foodList = repository.getFoodsForUpdate()
            when (foodList) {
                is Resource.Failure -> {

                }
                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            foodList = foodList.data,
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }

}