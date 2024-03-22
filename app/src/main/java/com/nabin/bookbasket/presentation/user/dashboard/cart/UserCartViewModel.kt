package com.nabin.bookbasket.presentation.user.dashboard.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin.bookbasket.R
import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.local.Preference
import com.nabin.bookbasket.domain.model.CheckoutBooks
import com.nabin.bookbasket.domain.use_cases.db.DBUseCases
import com.nabin.bookbasket.domain.use_cases.firestore.FirestoreUseCases
import com.nabin.bookbasket.domain.use_cases.realtime.RealtimeUseCases
import com.nabin.bookbasket.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCartViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases,
    private val preference: Preference,
    private val realtimeUseCases: RealtimeUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(UserCartState())
    val state: StateFlow<UserCartState> = _state

    init {
        _state.update {
            it.copy(
                phoneNo = preference.phone?:""
            )
        }
        viewModelScope.launch {
            realtimeUseCases.deliveryState().collect{data ->
                when(data){
                    is Resource.Failure -> {
                    }
                    Resource.Loading -> {
                    }
                    is Resource.Success -> {

                        _state.update { it.copy(deliveryState = data.data,deliveryStateShowDialog = data.data)
                        }
                    }
                }
            }
        }
        getCartItems()
    }

    private fun getCartItems() {
        viewModelScope.launch {
            dbUseCases.removeAllCheckoutFoods()
            val allFoods = dbUseCases.getAllFoods()

            when (val cartItemList = firestoreUseCases.getCartItems()) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    println(cartItemList)
                    _state.update {
                        it.copy(cartItemList = allFoods.filter { food ->
                            cartItemList.data.any { it.bookId == food.bookId }
                        }.map { food ->
                            val updatedQuantity =
                                cartItemList.data.find { it.bookId == food.bookId }?.bookQuantity
                                    ?: food.quantity
                            food.copy(quantity = updatedQuantity)
                        }.toMutableList()
                        )
                    }
                }
            }
        }
    }

    val onEvent: (event: UserCartEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                UserCartEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                UserCartEvent.DeleteItems -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                title = "Deleting Item",
                                description = "Please wait while deleting...",
                                lottieImage = R.raw.delete,
                                yesNoRequired = false,
                                isCancellable = false
                            )
                        )
                    }
                    for (food in state.value.cartItemList) {
                        if (food.isSelected) {
                            firestoreUseCases.deleteCartItem(food.bookId)
                        }
                    }
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                    getCartItems()

                }

                is UserCartEvent.DecreaseQuantity -> {
                    _state.update {
                        it.copy(cartItemList = state.value.cartItemList.map { food ->
                            if (food.bookId == event.foodId) {
                                food.copy(quantity = event.lastValue - 1)
                            } else {
                                food
                            }
                        })
                    }
                }

                is UserCartEvent.IncreaseQuantity -> {
                    _state.update {
                        it.copy(cartItemList = state.value.cartItemList.map { food ->
                            if (food.bookId == event.foodId) {
                                food.copy(quantity = event.lastValue + 1)
                            } else {
                                food
                            }
                        })
                    }
                }

                is UserCartEvent.SelectAllCheckBox -> {
                    _state.update {
                        it.copy(
                            cartItemList = state.value.cartItemList.map { it.copy(isSelected = event.isChecked) },
                        )
                    }
                }

                is UserCartEvent.ItemSelected -> {
                    _state.update {
                        it.copy(cartItemList = state.value.cartItemList.map { food ->
                            if (food.bookId == event.item) {
                                food.copy(isSelected = event.isItemSelected)
                            } else {
                                food
                            }
                        })
                    }
                }

                UserCartEvent.Checkout -> {
                    dbUseCases.removeAllCheckoutFoods()
                    dbUseCases.insertAllCheckoutFoodList(checkList = state.value.cartItemList.filter { it.isSelected }
                        .map {
                            CheckoutBooks(
                                bookId = it.bookId,
                                bookType = it.bookType,
                                bookFamily = it.bookFamily,
                                bookName = it.bookName,
                                bookDetails = it.bookDetails,
                                bookPrice = it.bookPrice,
                                bookDiscount = it.bookDiscount,
                                bookNewPrice = it.bookNewPrice,
                                isSelected = it.isSelected,
                                bookRating = it.bookRating,
                                newBookRating = it.newBookRating,
                                quantity = it.quantity,
                                date = it.date,
                                faceImgName = it.faceImgName,
                                faceImgUrl = it.faceImgUrl
                            )
                        })
                }
                UserCartEvent.OnRefresh -> {
                    _state.update {
                        it.copy(
                            phoneNo = preference.phone?:""
                        )
                    }
                    getCartItems()
                }
            }
        }
    }
}