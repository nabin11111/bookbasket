package com.chetan.orderdelivery.presentation.admin.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import com.chetan.orderdelivery.presentation.user.morepopularfood.FilterTypes
import com.chetan.orderdelivery.presentation.user.morepopularfood.UserMoreEvent
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AdminHomeState())
    val state: StateFlow<AdminHomeState> = _state

    init {
        getOrders()
        val data = OneSignal.User.pushSubscription.id
        if(data.isNotBlank()){
            viewModelScope.launch {
                firestoreUseCases.setOneSignalId(
                    data = SetOneSignalId(
                        id = data,
                        branch = "1",
                    )
                )
            }
        }

    }

    private fun getOrders() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        title = "Orders",
                        description = "Getting orders...",
                        lottieImage = R.raw.loading_food,
                        yesNoRequired = false,
                        isCancellable = false
                    )
                )
            }
            val orderList = firestoreUseCases.getFoodOrders()
            when (orderList) {
                is Resource.Failure -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Error",
                                description = "Something went wrong...",
                                lottieImage = R.raw.loading_food,
                                yesNoRequired = false,
                                isCancellable = true
                            )
                        )
                    }
                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            orderList = orderList.data.sortedByDescending { it.time },
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }

    val onEvent: (event: AdminHomeEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is AdminHomeEvent.OnFilterChange -> {
                    when(event.value){
                        BranchType.ALL -> {
                            _state.update {
                                it.copy(
                                    branchWiseList = state.value.orderList
                                )
                            }
                        }
                        BranchType.NPJ -> {
                            _state.update {
                                it.copy(
                                    branchWiseList = state.value.orderList.filter { it.branch == "npj" }
                                )
                            }
                        }
                        BranchType.KLP -> {
                            _state.update {
                                it.copy(
                                    branchWiseList = state.value.orderList.filter { it.branch == "klp" }
                                )
                            }
                        }
                    }
                }

                AdminHomeEvent.Test -> {

                }

                AdminHomeEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is AdminHomeEvent.RemoveUser -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Orders",
                                description = "Deleting Order...",
                                lottieImage = R.raw.delete,
                                yesNoRequired = false,
                                isCancellable = false
                            )
                        )
                    }
                    val deleteUser = firestoreUseCases.removeUserOrder(event.user)
                    when (deleteUser) {
                        is Resource.Failure -> {
                            _state.update {
                                it.copy(
                                    infoMsg = Message.Loading(
                                        title = "Error",
                                        description = "Something went wrong...",
                                        lottieImage = R.raw.delete,
                                        yesNoRequired = false,
                                        isCancellable = true
                                    )
                                )
                            }
                        }

                        Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            if (deleteUser.data) {
                                _state.update {
                                    it.copy(
                                        branchWiseList = state.value.branchWiseList.filter { it.userMail != event.user },
                                        infoMsg = null,
                                    )
                                }

                            }
                        }
                    }
                }

                AdminHomeEvent.OnRefresh -> {
                    getOrders()
                }
            }
        }
    }

}