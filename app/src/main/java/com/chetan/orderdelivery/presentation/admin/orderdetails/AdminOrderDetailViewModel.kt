package com.chetan.orderdelivery.presentation.admin.orderdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.PushNotificationRequest
import com.chetan.orderdelivery.domain.repository.OneSignalRepository
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AdminOrderDetailViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val oneSiganlRepository: OneSignalRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AdminOrderDetailState())
    val state: StateFlow<AdminOrderDetailState> = _state

    init {

    }

    val onEvent: (event: AdminOrderDetailEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is AdminOrderDetailEvent.Delivered -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Updating",
                                description = "Please wait...",
                                lottieImage = R.raw.loading_food,
                                yesNoRequired = false,
                                isCancellable = false
                            )
                        )
                    }
                    val orderIdDetails =
                        state.value.orderDetails.find { it.orderId == event.value }!!
                    val updateUserHistory =
                        firestoreUseCases.updateUserHistory(data = orderIdDetails)
                    when (updateUserHistory) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            if (updateUserHistory.data) {
                                val updateDeliverHistory =
                                    firestoreUseCases.updateDeliveredHistory(data = orderIdDetails.copy(
                                        dateTime = orderIdDetails.dateTime.split(" ").first().split("-").drop(1).joinToString("-")
                                    ))
                                when (updateDeliverHistory) {
                                    is Resource.Failure -> {}
                                    Resource.Loading -> {

                                    }

                                    is Resource.Success -> {
                                        if (updateDeliverHistory.data) {
                                            val orderDelivereds =
                                                firestoreUseCases.orderDelivered(data = orderIdDetails)
                                            when (orderDelivereds) {
                                                is Resource.Failure -> {}
                                                Resource.Loading -> {

                                                }

                                                is Resource.Success -> {
                                                    if (orderDelivereds.data) {


                                                        try {
                                                            val sendNotification =
                                                                oneSiganlRepository.pushNotification(
                                                                    PushNotificationRequest(
                                                                        contents = mapOf("en" to "Your order is delivered"),
                                                                        headings = mapOf("en" to "Order"),
                                                                        include_player_ids = listOf(
                                                                            orderIdDetails.oneSignalId
                                                                        )
                                                                    )
                                                                )
                                                            when (sendNotification) {
                                                                is Resource.Failure -> {
//                        _state.update {
//                            it.copy(
//                                infoMsg = Message.Error(
//                                    lottieImage = R.raw.delete_simple,
//                                    yesNoRequired = false,
//                                    isCancellable = false,
//                                    description = "Error..."
//                                )
//                            )
//
//                        }
                                                                }

                                                                Resource.Loading -> {

                                                                }

                                                                is Resource.Success -> {
//                        _state.update {
//                            it.copy(
//                                infoMsg = Message.Success(
//                                    lottieImage = R.raw.pencil_walking,
//                                    isCancellable = true,
//                                    description = "Success"
//                                )
//                            )
//                        }
                                                                }
                                                            }
                                                        } catch (e: HttpException) {
                                                            _state.update {
                                                                it.copy(

                                                                )
                                                            }
                                                            e.printStackTrace()
                                                        } catch (e: Throwable) {
                                                            _state.update {
                                                                it.copy(

                                                                )
                                                            }
                                                            e.printStackTrace()
                                                        }


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
                                }
                            }
                        }
                    }
                }

                AdminOrderDetailEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is AdminOrderDetailEvent.GetOrderDetails -> {
                    val orderDetails = firestoreUseCases.getFoodOrderDetails(event.user)
                    when (orderDetails) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    orderDetails = orderDetails.data
                                )
                            }
                        }
                    }

                }

                is AdminOrderDetailEvent.OnOrderReady -> {
                    val orderIdDetails =
                        state.value.orderDetails.find { it.orderId == event.value }!!
                    try {
                        val sendNotification =
                            oneSiganlRepository.pushNotification(
                                PushNotificationRequest(
                                    contents = mapOf("en" to "Your order is Ready"),
                                    headings = mapOf("en" to "Order"),
                                    include_player_ids = listOf(
                                        orderIdDetails.oneSignalId
                                    )
                                )
                            )
                        when (sendNotification) {
                            is Resource.Failure -> {
//                        _state.update {
//                            it.copy(
//                                infoMsg = Message.Error(
//                                    lottieImage = R.raw.delete_simple,
//                                    yesNoRequired = false,
//                                    isCancellable = false,
//                                    description = "Error..."
//                                )
//                            )
//
//                        }
                            }

                            Resource.Loading -> {

                            }

                            is Resource.Success -> {
//                        _state.update {
//                            it.copy(
//                                infoMsg = Message.Success(
//                                    lottieImage = R.raw.pencil_walking,
//                                    isCancellable = true,
//                                    description = "Success"
//                                )
//                            )
//                        }
                            }
                        }
                    } catch (e: HttpException) {
                        _state.update {
                            it.copy(

                            )
                        }
                        e.printStackTrace()
                    } catch (e: Throwable) {
                        _state.update {
                            it.copy(

                            )
                        }
                        e.printStackTrace()
                    }
                }

                is AdminOrderDetailEvent.OnShowHideMsgDialog -> {
                    _state.update {
                        it.copy(
                            showInformDialog = event.value
                        )
                    }
                }
                is AdminOrderDetailEvent.OnMessageChange -> {
                    _state.update {
                        it.copy(
                            msg = event.value
                        )
                    }
                }
                is AdminOrderDetailEvent.OnMessageSend -> {
                    val orderIdDetails =
                        state.value.orderDetails.find { it.orderId == event.value }!!
                    try {
                        val sendNotification =
                            oneSiganlRepository.pushNotification(
                                PushNotificationRequest(
                                    contents = mapOf("en" to state.value.msg),
                                    headings = mapOf("en" to "Order Confirmed"),
                                    include_player_ids = listOf(
                                        orderIdDetails.oneSignalId
                                    )
                                )
                            )
                        when (sendNotification) {
                            is Resource.Failure -> {

                            }

                            Resource.Loading -> {

                            }

                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        showInformDialog = false
                                    )
                                }
                            }
                        }
                    } catch (e: HttpException) {
                        _state.update {
                            it.copy(
                                showInformDialog = false
                            )
                        }
                        e.printStackTrace()
                    } catch (e: Throwable) {
                        _state.update {
                            it.copy(
                                showInformDialog = false
                            )
                        }
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}