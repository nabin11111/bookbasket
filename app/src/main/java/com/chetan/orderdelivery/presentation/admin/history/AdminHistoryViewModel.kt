package com.chetan.orderdelivery.presentation.admin.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHistoryViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AdminHistoryState())
    val state: StateFlow<AdminHistoryState> = _state

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        title = "Loading",
                        description = "Getting all histories",
                        lottieImage = R.raw.loading_food,
                        yesNoRequired = false,
                        isCancellable = true
                    )
                )
            }
            val historyList = firestoreUseCases.getAdminHistories()
            when (historyList) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            historyList = historyList.data,
                            listByGroup = historyList.data.groupBy { it.dateTime }.toList(),
                            infoMsg = null
                        )
                    }
                }

            }
        }
    }

    val onEvent: (event: AdminHistoryEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                AdminHistoryEvent.DeleteHistory -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Histories",
                                description = "Deleting histories",
                                lottieImage = R.raw.delete
                            )
                        )
                    }
                }

                AdminHistoryEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }


}