package com.chetan.orderdelivery.presentation.user.myorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.data.model.PushNotificationRequest
import com.chetan.orderdelivery.data.model.StoreNotificationRequestResponse
import com.chetan.orderdelivery.domain.repository.OneSignalRepository
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
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
class MyOrderViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val preference: Preference,
    private val oneSignalRepository: OneSignalRepository,
    private val dbUseCases: DBUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(MyOrderState())
    val state: StateFlow<MyOrderState> = _state

    init {
        getMyOrders()
    }

    private fun getMyOrders() {
        viewModelScope.launch {
            val orderDetails = firestoreUseCases.getFoodOrderDetails(preference.tableName ?: "test")
            when (orderDetails) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            orderDetails = orderDetails.data.reversed()
                        )
                    }
                }
            }
        }
    }

    val onEvent: (event: MyOrderEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is MyOrderEvent.CancelOrder -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Order Cancel",
                                description = "Please wait...",
                                lottieImage = R.raw.delete,
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
                                try {
                                    val sendNotification =
                                        oneSignalRepository.pushNotification(
                                            PushNotificationRequest(
                                                contents = mapOf("en" to "Order is Canceled by ${orderIdDetails.userMail}"),
                                                headings = mapOf("en" to "Order Canceled"),
                                                include_player_ids = dbUseCases.getAllIds()
                                                    .map { it.id }
                                            )
                                        )
                                    when (sendNotification) {
                                        is Resource.Failure -> {
                                        }

                                        Resource.Loading -> {

                                        }

                                        is Resource.Success -> {

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
                                        infoMsg = null,
                                        orderDetails = state.value.orderDetails.filterNot { it.orderId == orderIdDetails.orderId }
                                    )
                                }
                            }
                            _state.update {
                                it.copy(
                                    infoMsg = null
                                )
                            }

                        }
                    }

                }

                MyOrderEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                MyOrderEvent.GetFoodStatus -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Order Status",
                                description = "Please wait. We will inform you soon.",
                                isCancellable = false,
                                yesNoRequired = false
                            )
                        )
                    }
                    val askStatus = firestoreUseCases.setAdminNotification(
                        StoreNotificationRequestResponse(
                            body = "${preference.userName} wants to know status.",
                            title = "Food Status",
                            time = System.currentTimeMillis().toString(),
                            readNotice = false,
                        )
                    )
                    when(askStatus){
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
                            try {
                                val sendNotification =
                                    oneSignalRepository.pushNotification(
                                        PushNotificationRequest(
                                            contents = mapOf("en" to "${preference.userName?:"someone"}  wants to know status"),
                                            headings = mapOf("en" to "Order Status"),
                                            include_player_ids = dbUseCases.getAllIds()
                                                .map { it.id }
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
                    }
                }
            }
        }
    }
}