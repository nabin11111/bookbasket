package com.nabin.bookbasket.presentation.admin.food.addfood

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabin.bookbasket.data.Resource
import com.nabin.bookbasket.data.model.AddFoodRequest
import com.nabin.bookbasket.data.model.ImageStorageDetails
import com.nabin.bookbasket.domain.use_cases.firestore.FirestoreUseCases
import com.nabin.bookbasket.domain.use_cases.storage.FirestorageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val storageUseCases: FirestorageUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddFoodState())
    val state: StateFlow<AddFoodState> = _state


    @RequiresApi(Build.VERSION_CODES.O)
    val onEvent: (event: AddFoodEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {

                AddFoodEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(infoMsg = null)
                    }
                }

                is AddFoodEvent.OnSelectedFoodTypeChange -> {
                    _state.update {
                        it.copy(
                            selectedFoodType = event.value
                        )
                    }
                }

                is AddFoodEvent.OnSelectedFoodFamilyChange -> {
                    _state.update {
                        it.copy(
                            selectedFoodFamily = event.value
                        )
                    }
                }

                is AddFoodEvent.OnFoodNameChange -> {
                    _state.update {
                        it.copy(
                            foodName = event.value,
                            foodId = event.value.replace(" ", "")
                        )
                    }
                }

                is AddFoodEvent.OnFoodDetailsChange -> {
                    _state.update {
                        it.copy(
                            foodDetails = event.value
                        )
                    }
                }

                is AddFoodEvent.OnFoodDiscountChange -> {
                    _state.update {
                        it.copy(
                            foodDiscountPrice = event.value
                        )
                    }
                }

                is AddFoodEvent.OnFoodPriceChange -> {
                    _state.update {
                        it.copy(
                            foodPrice = event.value
                        )
                    }
                }

                is AddFoodEvent.OnImageUriToUrl -> {
                    val requestUrl = storageUseCases.insertImage(
                        data = ImageStorageDetails(
                            imageUri = event.value,
                            imagePath = "/foods/${state.value.foodId}/",
                            imageName = event.name.toString()
                        )
                    )
                    when (requestUrl) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {}
                        is Resource.Success -> {
                            when (event.name) {
                                1 -> {
                                    _state.update {
                                        it.copy(
                                            faceImgUrl = ImageUrlDetail(
                                                imageName = requestUrl.data.first,
                                                imageUrl = requestUrl.data.second,
                                                storagePath = "/foods/${state.value.foodId}/",
                                            )
                                        )
                                    }
                                }

                                2 -> {
                                    _state.update {
                                        it.copy(
                                            supportImgUrl2 = ImageUrlDetail(
                                                imageName = requestUrl.data.first,
                                                imageUrl = requestUrl.data.second,
                                                storagePath = "/foods/${state.value.foodId}/",
                                            )
                                        )
                                    }
                                }

                                3 -> {
                                    _state.update {
                                        it.copy(
                                            supportImgUrl3 = ImageUrlDetail(
                                                imageName = requestUrl.data.first,
                                                imageUrl = requestUrl.data.second,
                                                storagePath = "/foods/${state.value.foodId}/",
                                            )
                                        )
                                    }
                                }

                                4 -> {
                                    _state.update {
                                        it.copy(
                                            supportImgUrl4 = ImageUrlDetail(
                                                imageName = requestUrl.data.first,
                                                imageUrl = requestUrl.data.second,
                                                storagePath = "/foods/${state.value.foodId}/",
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                AddFoodEvent.AddFood -> {


                    val addFoodRequest = firestoreUseCases.addFood(
                        data = AddFoodRequest(
                            foodId = state.value.foodId,
                            foodType = state.value.selectedFoodType,
                            foodFamily = state.value.selectedFoodFamily,
                            foodName = state.value.foodName,
                            foodDetails = state.value.foodDetails,
                            foodPrice = state.value.foodPrice,
                            foodDiscount = state.value.foodDiscountPrice,
                            foodNewPrice = state.value.foodPrice.toInt() - state.value.foodDiscountPrice.toInt(),
                            foodRating = 0f,
                            date = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            ),
                            faceImgName = state.value.faceImgUrl.imageName,
                            faceImgUrl = state.value.faceImgUrl.imageUrl,
                            supportImgName2 = state.value.supportImgUrl2.imageName,
                            supportImgUrl2 = state.value.supportImgUrl2.imageUrl,
                            supportImgName3 = state.value.supportImgUrl3.imageName,
                            supportImgUrl3 = state.value.supportImgUrl3.imageUrl,
                            supportImgName4 = state.value.supportImgUrl4.imageName,
                            supportImgUrl4 = state.value.supportImgUrl4.imageUrl,

                            )
                    )

                    when (addFoodRequest) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    selectedFoodType = "",
                                    selectedFoodFamily = "",
                                    foodName = "",
                                    foodId = "",
                                    foodDetails = "",
                                    foodDiscountPrice = "",
                                    foodPrice = "",
                                    faceImgUrl = ImageUrlDetail(
                                        imageName = "",
                                        imageUrl = "",
                                        storagePath = "",
                                    ),
                                    supportImgUrl2 = ImageUrlDetail(
                                        imageName = "",
                                        imageUrl = "",
                                        storagePath = "",
                                    ),
                                    supportImgUrl3 = ImageUrlDetail(
                                        imageName = "",
                                        imageUrl = "",
                                        storagePath = "",
                                    ),
                                    supportImgUrl4 = ImageUrlDetail(
                                        imageName = "",
                                        imageUrl = "",
                                        storagePath = "",
                                    )
                                )
                            }
                        }
                    }

                }


            }
        }

    }
}
