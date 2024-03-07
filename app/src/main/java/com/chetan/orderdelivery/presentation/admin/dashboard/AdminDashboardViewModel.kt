package com.chetan.orderdelivery.presentation.admin.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.domain.use_cases.realtime.RealtimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val preference: Preference,
    private val realtimeUseCases: RealtimeUseCases
): ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state : StateFlow<AdminDashboardState> = _state



    init {
        viewModelScope.launch {
            realtimeUseCases.deliveryState().collect{data ->
                when(data){
                    is Resource.Failure -> {
                    }
                    Resource.Loading -> {
                    }
                    is Resource.Success -> {

                        _state.update { it.copy(changeDeliveryState = data.data) }
                    }
                }
            }
        }
        _state.update {
            it.copy(
                darkMode = preference.isDarkMode.value
            )
        }
        viewModelScope.launch {
            realtimeUseCases.getItems().collect{data ->
                when(data){
                    is Resource.Failure -> {

                    }
                    Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        _state.update { it.copy(newRequestList = data.data) }
                    }
                }
            }

        }

    }

    val onEvent : (event : AdminDashboardEvent) -> Unit = {event ->
        viewModelScope.launch {
            when(event){
                is AdminDashboardEvent.ChangeDarkMode -> {
                    preference.isDarkMode = mutableStateOf(!state.value.darkMode)
                    _state.update {
                        it.copy(
                            darkMode = !state.value.darkMode
                        )
                    }
                }

                AdminDashboardEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                AdminDashboardEvent.ChangeDeliveryState -> {
                    realtimeUseCases.changeDeliveryState(!state.value.changeDeliveryState)
                }
            }
        }

    }
}