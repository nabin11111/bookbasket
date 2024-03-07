package com.chetan.orderdelivery.presentation.user.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.domain.model.AllFoods
import com.chetan.orderdelivery.domain.repository.DBRepository
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDashboardViewModel @Inject constructor(
    private val preference: Preference,
    private val repository: FirestoreUseCases,
    private val dbRepository: DBUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(UserDashboardState())
    val state: StateFlow<UserDashboardState> = _state


    init {
        getAllFoods()
        getAllIds()
        getUserProfile()
        _state.update {
            it.copy(
                profileUrl = preference.gmailProfile?:"",
                darkMode = preference.isDarkMode.value,
                isNewNotification = preference.isNewNotification
            )
        }

    }
    private fun getUserProfile(){
        viewModelScope.launch {
            val profile = repository.getUserProfile(preference.tableName ?: "test")
            when (profile) {
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                }
                is Resource.Success -> {
                    if(profile.data.phoneNo.isNotBlank() || profile.data.name.isNotBlank() || profile.data.address.isNotBlank()){
                        preference.phone = profile.data.phoneNo
                    }
                }
            }
        }
    }
    val onEvent: (event: UserDashboardEvent) -> Unit = { event ->
        when (event) {
            is UserDashboardEvent.ChangeDarkMode -> {
                preference.isDarkMode = mutableStateOf(!state.value.darkMode)
                _state.update {
                    it.copy(
                        darkMode = !state.value.darkMode
                    )
                }
            }

            UserDashboardEvent.DismissInfoMsg -> {
                _state.update {
                    it.copy(
                        infoMsg = null
                    )
                }
            }
        }
    }


    fun getAllFoods() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        lottieImage = R.raw.loading_food,
                        yesNoRequired = false,
                        isCancellable = false,
                        title = "Loading",
                        description = "Please Wait... Getting all foods"
                    )
                )
            }
            when (val getAllFoodsResponse = repository.getFoods()) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    dbRepository.insertFoodList(getAllFoodsResponse.data.map {
                        AllFoods(
                            foodId = it.foodId,
                            foodType = it.foodType,
                            foodFamily = it.foodFamily,
                            foodName = it.foodName,
                            foodDetails = it.foodDetails,
                            foodPrice = it.foodPrice,
                            foodDiscount = it.foodDiscount,
                            foodNewPrice = it.foodNewPrice,
                            foodRating = it.foodRating,
                            newFoodRating = it.newFoodRating,
                            date = it.date,
                            faceImgName = it.faceImgName,
                            faceImgUrl = it.faceImgUrl,
                            faceImgPath = it.faceImgPath,

                            supportImgName2 = it.supportImgName2,
                            supportImgUrl2 = it.supportImgUrl2,
                            supportImgPath2 = it.supportImgPath2,

                            supportImgName3 = it.supportImgName3,
                            supportImgUrl3 = it.supportImgUrl3,
                            supportImgPath3 = it.supportImgPath3,

                            supportImgName4 = it.supportImgName4,
                            supportImgUrl4 = it.supportImgUrl4,
                            supportImgPath4 = it.supportImgPath4,
                            )
                    }.sortedBy { it.foodName })
                    _state.update {
                        it.copy(
                            allFoods = getAllFoodsResponse.data.sortedByDescending { it.foodRating },
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }
    fun getAllIds() {
        viewModelScope.launch {
            when (val getAllId = repository.getOneSignalIds("1")) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    dbRepository.insertIds(getAllId.data)
                    _state.update {
                        it.copy(
                            allIds = getAllId.data,
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }
}