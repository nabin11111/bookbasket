package com.chetan.orderdelivery.presentation.user.ordercheckout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.data.model.PushNotificationRequest
import com.chetan.orderdelivery.data.model.RealtimeModelResponse
import com.chetan.orderdelivery.data.model.SetLatLng
import com.chetan.orderdelivery.data.model.order.RequestFoodOrder
import com.chetan.orderdelivery.domain.repository.OneSignalRepository
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.domain.use_cases.realtime.RealtimeUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import com.chetan.orderdelivery.presentation.common.utils.MyDate.CurrentDateTimeSDF
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class OrderCheckoutViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases,
    private val realtimeUseCases: RealtimeUseCases,
    private val preference: Preference,
    private val oneSiganlRepository: OneSignalRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(OrderCheckoutState())
    val state: StateFlow<OrderCheckoutState> = _state
    val context = GeoApiContext.Builder().apiKey("AIzaSyCmYhLU8zTX8SJV0IxIvtwV0D27tlFyVSc").build()

    init {
        getOrderList()
        getIds()
        try {
            val directionsResult: DirectionsResult? = DirectionsApi.newRequest(context)
                .mode(TravelMode.DRIVING)
                .origin(
                    com.google.maps.model.LatLng(
                        state.value.cameraLocation.latitude,
                        state.value.cameraLocation.longitude
                    )
                )
                .destination(
                    com.google.maps.model.LatLng(
                        state.value.momobarNpj.latitude,
                        state.value.momobarNpj.longitude
                    )
                )
                .await()

            _state.update {
                it.copy(
                    distance = directionsResult?.routes?.get(0)?.legs?.get(0)?.distance?.humanReadable
                        ?: "null"
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getOrderList() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    orderList = dbUseCases.getAllCheckoutFoods()
                )
            }
        }
    }

    private fun getIds() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    allIds = dbUseCases.getAllIds()
                )
            }
        }
    }

    val onEvent: (event: OrderCheckoutEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                OrderCheckoutEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is OrderCheckoutEvent.Location -> {

                    val location = event.value.split(",")
                    try {
                        val npjDirectionResult: DirectionsResult? =
                            DirectionsApi.newRequest(context)
                                .mode(TravelMode.DRIVING)
                                .origin(
                                    com.google.maps.model.LatLng(
                                        location.first().toDouble(),
                                        location.last().toDouble()
                                    )
                                )
                                .destination(
                                    com.google.maps.model.LatLng(
                                        state.value.momobarNpj.latitude,
                                        state.value.momobarNpj.longitude
                                    )
                                )
                                .await()
                        val klpDirectionResult: DirectionsResult? =
                            DirectionsApi.newRequest(context)
                                .mode(TravelMode.DRIVING)
                                .origin(
                                    com.google.maps.model.LatLng(
                                        location.first().toDouble(),
                                        location.last().toDouble()
                                    )
                                )
                                .destination(
                                    com.google.maps.model.LatLng(
                                        state.value.momobarKlp.latitude,
                                        state.value.momobarKlp.longitude
                                    )
                                )
                                .await()

                        val npjDistance = npjDirectionResult?.routes?.get(0)?.legs?.get(0)?.distance
                        val klpDistance = klpDirectionResult?.routes?.get(0)?.legs?.get(0)?.distance
                        if ((npjDistance?.inMeters ?: 0L) <= 5000L || (klpDistance?.inMeters
                                ?: 0L) <= 5000L
                        ) {
                            if ((npjDistance?.inMeters
                                    ?: 0L) < (klpDistance?.inMeters ?: 0L)
                            ) {
                                _state.update {
                                    it.copy(
                                        branch = "npj",
                                        distance = npjDistance?.humanReadable ?: "null",
                                        canOrder = true,
                                        location = event.value
                                    )
                                }

                            } else {
                                _state.update {
                                    it.copy(
                                        branch = "klp",
                                        distance = klpDistance?.humanReadable ?: "null",
                                        canOrder = true,
                                        location = event.value
                                    )
                                }
                            }


                        } else {
                            _state.update {
                                it.copy(
                                    infoMsg = Message.Error(
                                        lottieImage = R.raw.delete,
                                        title = "Error",
                                        description = "Delivery is Not available here",
                                        yesNoRequired = false,
                                        isCancellable = true
                                    ),
                                    canOrder = false
                                )
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                is OrderCheckoutEvent.LocationAddress -> {
                    _state.update {
                        it.copy(
                            locationAddress = event.value
                        )
                    }
                }

                OrderCheckoutEvent.OrderNow -> {
                    val location = state.value.location.split(",")
                    val setAddress = firestoreUseCases.setAddress(
                        SetLatLng(
                            time = System.currentTimeMillis().toString(),
                            locationLat = location.first(),
                            locationLng = location.last(),
                            locationAddress = state.value.locationAddress,
                            distance = state.value.distance,
                            userName = "",
                            userContactNo = preference.phone?:"",
                            userMail = preference.tableName!!,
                            googleProfileUrl = preference.gmailProfile ?: "",
                            dbProfileUrl = "",
                            googleUserName = preference.userName ?: "",
                            branch = state.value.branch

                        )
                    )
                    when (setAddress) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            if (setAddress.data) {
                                val orderRequest =
                                    firestoreUseCases.orderFood(
                                        data = RequestFoodOrder(
                                            orderId = System.currentTimeMillis().toString(),
                                            locationLat = location.first(),
                                            locationLng = location.last(),
                                            distance = state.value.distance,
                                            userName = "",
                                            oneSignalId = OneSignal.User.pushSubscription.id,
                                            userContactNo =  preference.userName?:"",
                                            userMail = preference.tableName!!,
                                            googleProfileUrl = preference.gmailProfile ?: "",
                                            dbProfileUrl = "",
                                            googleUserName = preference.userName ?: "",
                                            locationAddress = state.value.locationAddress,
                                            dateTime = CurrentDateTimeSDF(),
                                            branch = state.value.branch,
                                            orderList = state.value.orderList.map { food ->
                                                RequestFoodOrder.OrderedList(
                                                    foodId = food.foodId,
                                                    foodType = food.foodType,
                                                    foodFamily = food.foodFamily,
                                                    foodName = food.foodName,
                                                    foodDetails = food.foodDetails,
                                                    foodPrice = food.foodPrice,
                                                    foodDiscount = food.foodDiscount,
                                                    foodNewPrice = food.foodNewPrice,
                                                    isSelected = food.isSelected,
                                                    foodRating = food.foodRating,
                                                    newFoodRating = food.newFoodRating,
                                                    quantity = food.quantity,
                                                    date = CurrentDateTimeSDF(),
                                                    faceImgName = food.faceImgName,
                                                    faceImgUrl = food.faceImgUrl,
                                                )
                                            })
                                    )
                                when (orderRequest) {
                                    is Resource.Failure -> {

                                    }

                                    Resource.Loading -> {

                                    }

                                    is Resource.Success -> {
                                        realtimeUseCases.insert(
                                            RealtimeModelResponse.RealTimeNewOrderRequest(
                                                true, ""
                                            )
                                        )
                                        for (food in state.value.orderList) {
                                            if (food.isSelected) {
                                                firestoreUseCases.deleteCartItem(food.foodId)
                                            }
                                        }
                                        try {
                                            val sendNotification =
                                                oneSiganlRepository.pushNotification(
                                                    PushNotificationRequest(
                                                        contents = mapOf("en" to state.value.locationAddress),
                                                        headings = mapOf("en" to "Order"),
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
        }
    }
}