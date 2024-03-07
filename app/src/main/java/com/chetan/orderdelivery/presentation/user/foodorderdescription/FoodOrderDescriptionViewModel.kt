package com.chetan.orderdelivery.presentation.user.foodorderdescription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.data.model.GetCartItemModel
import com.chetan.orderdelivery.domain.model.CheckoutFoods
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.domain.use_cases.realtime.RealtimeUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import com.chetan.orderdelivery.presentation.common.utils.MyDate.CurrentDateTimeSDF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FoodOrderDescriptionViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases,
    private val preference: Preference,
    private val realtimeUseCases: RealtimeUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(FoodOrderDescriptionState())
    val state: StateFlow<FoodOrderDescriptionState> = _state

    init {
        viewModelScope.launch {
            realtimeUseCases.deliveryState().collect{data ->
                when(data){
                    is Resource.Failure -> {
                    }
                    Resource.Loading -> {
                    }
                    is Resource.Success -> {

                        _state.update { it.copy(
                            deliveryState = data.data,
                            deliveryStateShowDialog = data.data) }
                    }
                }
            }
        }
        _state.update {
            it.copy(
                phoneNo = preference.phone?:""
            )
        }
        viewModelScope.launch {
            dbUseCases.removeAllCheckoutFoods()
        }

    }
    private fun getFavList() {
        viewModelScope.launch {
            val allFavList = firestoreUseCases.getFavouriteList()
            when (allFavList) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            favouriteList = allFavList.data
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val onEvent: (event: FoodOrderDescriptionEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {

                is FoodOrderDescriptionEvent.SetFavourite -> {
                    val setFav = firestoreUseCases.setFavourite(
                        foodId = event.foodId, isFavourite = event.isFav
                    )
                    when (setFav) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            if (setFav.data) {
                                getFavList()
                            }
                        }
                    }
                }

                FoodOrderDescriptionEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is FoodOrderDescriptionEvent.GetFoodItemDetails -> {
                    val data = dbUseCases.getAllFoods().find { it.foodId == event.value }!!
                    _state.update {
                        it.copy(
                            foodItemDetails = data,
                            foodPrice = data.foodPrice.toInt() - data.foodDiscount.toInt(),
                            foodDiscount = data.foodPrice.toInt()
                        )
                    }
                }

                FoodOrderDescriptionEvent.DecreaseQuantity -> {
                    _state.update {
                        it.copy(
                            foodQuantity = state.value.foodQuantity - 1
                        )
                    }
                }

                FoodOrderDescriptionEvent.IncreaseQuantity -> {
                    _state.update {
                        it.copy(
                            foodQuantity = state.value.foodQuantity + 1
                        )
                    }
                }

                is FoodOrderDescriptionEvent.OrderFood -> {
                    dbUseCases.insertAllCheckoutFoodList(
                        checkList = listOf(
                            CheckoutFoods(
                                foodId = state.value.foodItemDetails.foodId,
                                foodType = state.value.foodItemDetails.foodType,
                                foodFamily = state.value.foodItemDetails.foodFamily,
                                foodName = state.value.foodItemDetails.foodName,
                                foodDetails = state.value.foodItemDetails.foodDetails,
                                foodPrice = state.value.foodItemDetails.foodPrice,
                                foodDiscount = state.value.foodItemDetails.foodDiscount,
                                foodNewPrice = state.value.foodItemDetails.foodNewPrice,
                                isSelected = true,
                                foodRating = state.value.foodItemDetails.foodRating,
                                newFoodRating = state.value.foodItemDetails.newFoodRating,
                                quantity = state.value.foodQuantity,
                                date = CurrentDateTimeSDF(),
                                faceImgName = state.value.foodItemDetails.faceImgName,
                                faceImgUrl = state.value.foodItemDetails.faceImgUrl
                            )
                        )
                    )


                }

                is FoodOrderDescriptionEvent.AddToCart -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                lottieImage = R.raw.adding_to_cart,
                                title = "My Cart",
                                description = "Adding To Cart",
                                yesNoRequired = false,
                                isCancellable = false
                            )
                        )
                    }
                    when (firestoreUseCases.addToCart(
                        foodItem = GetCartItemModel(
                            foodId = state.value.foodItemDetails.foodId,
                            foodQuantity = state.value.foodQuantity,
                            date = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            )
                        )
                    )) {
                        is Resource.Failure -> {
                        }

                        Resource.Loading -> {


                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    infoMsg = null,
                                    totalCartItem = state.value.totalCartItem + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}