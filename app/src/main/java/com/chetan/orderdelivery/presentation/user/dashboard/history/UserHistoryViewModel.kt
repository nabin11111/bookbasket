package com.chetan.orderdelivery.presentation.user.dashboard.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.data.model.RatingRequestResponse
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHistoryViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val preference: Preference
) : ViewModel() {
    private val _state = MutableStateFlow(UserHistoryState())
    val state: StateFlow<UserHistoryState> = _state

    init {
        getMyHistories()
    }

    private fun getMyHistories() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        title = "History",
                        description = "Getting Histories...",
                        lottieImage = R.raw.loading_food,
                        yesNoRequired = false,
                        isCancellable = false
                    )
                )
            }
            val histories = firestoreUseCases.getMyHistory()
            when (histories) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            historyList = histories.data,
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }

    val onEvent: (event: UserHistoryEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is UserHistoryEvent.RateIt -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Rating",
                                description = "Please wait...",
                                isCancellable = false,
                                yesNoRequired = false,
                                lottieImage = R.raw.rating_update
                            )
                        )
                    }
                    val rateit = firestoreUseCases.rateIt(
                        data = RatingRequestResponse(
                            userName = preference.userName ?: "",
                            userMail = preference.tableName ?: "",
                            foodId = event.id,
                            rateValue = event.value,
                            url = event.url
                        )
                    )
                    when (rateit) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {}
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    infoMsg = null
                                )
                            }
                        }
                    }
                }

                UserHistoryEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is UserHistoryEvent.DeleteMyHistory -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "History",
                                description = "Deleting Item...",
                                lottieImage = R.raw.delete,
                                yesNoRequired = false,
                                isCancellable = false
                            )
                        )
                    }


                    val deleteHistory = firestoreUseCases.deleteMyHistory(event.id)
                    when (deleteHistory) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {}
                        is Resource.Success -> {
                            if (deleteHistory.data) {
                                _state.update {
                                    it.copy(infoMsg = null,
                                        historyList = state.value.historyList.filter { it.orderId != event.id })
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}