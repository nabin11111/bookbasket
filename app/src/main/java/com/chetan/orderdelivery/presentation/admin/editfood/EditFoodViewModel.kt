package com.chetan.orderdelivery.presentation.admin.editfood

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.AddFoodRequest
import com.chetan.orderdelivery.data.model.ImageStorageDetails
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.domain.use_cases.storage.FirestorageUseCases
import com.chetan.orderdelivery.presentation.admin.food.addfood.ImageUrlDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditFoodViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases,
    private val dbUseCases: DBUseCases,
    private val storageUseCases: FirestorageUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(EditFoodState())
    val state: StateFlow<EditFoodState> = _state


    @RequiresApi(Build.VERSION_CODES.O)
    val onEvent: (event: EditFoodEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is EditFoodEvent.GetFoodItemDetails -> {
                    val data = dbUseCases.getAllFoods().find { it.foodId == event.value }!!
                    _state.update {
                        it.copy(
                            selectedFoodType = data.foodType,
                            selectedFoodFamily = data.foodFamily,
                            foodName = data.foodName,
                            foodId = data.foodId,
                            foodDetails = data.foodDetails,
                            foodDiscountPrice = data.foodDiscount,
                            foodPrice = data.foodPrice,
                            faceImgUrl = ImageUrlDetail(
                                imageName = data.faceImgName,
                                imageUrl = data.faceImgUrl,
                                storagePath = "/foods/${data.foodId}/",
                            ),
                            supportImgUrl2 = ImageUrlDetail(
                                imageName = data.supportImgName2,
                                imageUrl = data.supportImgUrl2,
                                storagePath = "/foods/${data.foodId}/",
                            ),
                            supportImgUrl3 = ImageUrlDetail(
                                imageName = data.supportImgName3,
                                imageUrl = data.supportImgUrl3,
                                storagePath = "/foods/${data.foodId}/",
                            ),
                            supportImgUrl4 = ImageUrlDetail(
                                imageName = data.supportImgName4,
                                imageUrl = data.supportImgUrl4,
                                storagePath = "/foods/${data.foodId}/",
                            ),
                            foodItemDetails = data
                        )
                    }
                }

                EditFoodEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(infoMsg = null)
                    }
                }

                is EditFoodEvent.OnSelectedFoodTypeChange -> {
                    _state.update {
                        it.copy(
                            selectedFoodType = event.value
                        )
                    }
                }

                is EditFoodEvent.OnSelectedFoodFamilyChange -> {
                    _state.update {
                        it.copy(
                            selectedFoodFamily = event.value
                        )
                    }
                }

                is EditFoodEvent.OnFoodNameChange -> {
                    _state.update {
                        it.copy(
                            foodName = event.value, foodId = event.value.replace(" ", "")
                        )
                    }
                }

                is EditFoodEvent.OnFoodDetailsChange -> {
                    _state.update {
                        it.copy(
                            foodDetails = event.value
                        )
                    }
                }

                is EditFoodEvent.OnFoodDiscountChange -> {
                    _state.update {
                        it.copy(
                            foodDiscountPrice = event.value
                        )
                    }
                }

                is EditFoodEvent.OnFoodPriceChange -> {
                    _state.update {
                        it.copy(
                            foodPrice = event.value
                        )
                    }
                }

                is EditFoodEvent.OnImageUriToUrl -> {
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


                EditFoodEvent.AddFood -> {
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

                        }
                    }

                }


            }
        }

    }
}
