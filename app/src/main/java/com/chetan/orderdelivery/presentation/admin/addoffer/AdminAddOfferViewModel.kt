package com.chetan.orderdelivery.presentation.admin.addoffer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.ImageStorageDetails
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.domain.use_cases.storage.FirestorageUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddOfferViewModel @Inject constructor(
    private val storageUseCases: FirestorageUseCases,
    private val firestoreUseCases: FirestoreUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(AdminAddOfferState())
    val state: StateFlow<AdminAddOfferState> = _state

    val onEvent: (event: AdminAddOfferEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                AdminAddOfferEvent.AddFood -> {
                    _state.update {
                        it.copy(
                            infoMsg = Message.Loading(
                                description = "Adding Offer",
                                title = "Offer",
                                yesNoRequired = false,
                                isCancellable = false
                            )
                        )
                    }


                    val offer = firestoreUseCases.addOffer(state.value.faceImgUrl)
                    when (offer) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    infoMsg = null, faceImgUrl = ""
                                )
                            }
                        }
                    }
                }

                AdminAddOfferEvent.DismissInfoMsg -> {

                }

                is AdminAddOfferEvent.OnImageUriToUrl -> {
                    val requestUrl = storageUseCases.insertImage(
                        data = ImageStorageDetails(
                            imageUri = event.value, imagePath = "/offer/offer/", imageName = "offer"
                        )
                    )
                    when (requestUrl) {
                        is Resource.Failure -> {}
                        Resource.Loading -> {}
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    faceImgUrl = requestUrl.data.second
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}