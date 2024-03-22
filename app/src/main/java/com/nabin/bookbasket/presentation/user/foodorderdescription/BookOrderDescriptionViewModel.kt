package com.nabin.bookbasket.presentation.user.foodorderdescription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin.bookbasket.R
import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.local.Preference
import com.nabin.bookbasket.data.model.GetCartItemModel
import com.nabin.bookbasket.domain.model.CheckoutFoods
import com.nabin.bookbasket.domain.use_cases.db.DBUseCases
import com.nabin.bookbasket.domain.use_cases.firestore.FirestoreUseCases
import com.nabin.bookbasket.domain.use_cases.realtime.RealtimeUseCases
import com.nabin.bookbasket.presentation.common.components.dialogs.Message
import com.nabin.bookbasket.presentation.common.utils.MyDate.CurrentDateTimeSDF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BookOrderDescriptionViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases,
    private val preference: Preference,
    private val realtimeUseCases: RealtimeUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(BookOrderDescriptionState())
    val state: StateFlow<BookOrderDescriptionState> = _state

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
    val onEvent: (event: BookOrderDescriptionEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {

                is BookOrderDescriptionEvent.SetFavourite -> {
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

                BookOrderDescriptionEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is BookOrderDescriptionEvent.GetFoodItemDetails -> {
                    val data = dbUseCases.getAllFoods().find { it.foodId == event.value }!!
                    _state.update {
                        it.copy(
                            bookItemDetails = data,
                            bookPrice = data.foodPrice.toInt() - data.foodDiscount.toInt(),
                            bookDiscount = data.foodPrice.toInt()
                        )
                    }
                }

                BookOrderDescriptionEvent.DecreaseQuantity -> {
                    _state.update {
                        it.copy(
                            bookQuantity = state.value.bookQuantity - 1
                        )
                    }
                }

                BookOrderDescriptionEvent.IncreaseQuantity -> {
                    _state.update {
                        it.copy(
                            bookQuantity = state.value.bookQuantity + 1
                        )
                    }
                }

                is BookOrderDescriptionEvent.OrderFood -> {
                    dbUseCases.insertAllCheckoutFoodList(
                        checkList = listOf(
                            CheckoutFoods(
                                foodId = state.value.bookItemDetails.foodId,
                                foodType = state.value.bookItemDetails.foodType,
                                foodFamily = state.value.bookItemDetails.foodFamily,
                                foodName = state.value.bookItemDetails.foodName,
                                foodDetails = state.value.bookItemDetails.foodDetails,
                                foodPrice = state.value.bookItemDetails.foodPrice,
                                foodDiscount = state.value.bookItemDetails.foodDiscount,
                                foodNewPrice = state.value.bookItemDetails.foodNewPrice,
                                isSelected = true,
                                foodRating = state.value.bookItemDetails.foodRating,
                                newFoodRating = state.value.bookItemDetails.newFoodRating,
                                quantity = state.value.bookQuantity,
                                date = CurrentDateTimeSDF(),
                                faceImgName = state.value.bookItemDetails.faceImgName,
                                faceImgUrl = state.value.bookItemDetails.faceImgUrl
                            )
                        )
                    )


                }

                is BookOrderDescriptionEvent.AddToCart -> {
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
                            foodId = state.value.bookItemDetails.foodId,
                            foodQuantity = state.value.bookQuantity,
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