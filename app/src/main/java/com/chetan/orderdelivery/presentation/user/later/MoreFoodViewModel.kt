package com.chetan.orderdelivery.presentation.user.later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreFoodViewModel @Inject constructor(
    private val repository: FirestoreRepository
): ViewModel(){

    private val _state = MutableStateFlow(MoreFoodState())
    val state : StateFlow<MoreFoodState> = _state

    init {

    }

    val onEvent: (event: MoreFoodEvent) -> Unit = { event ->
            viewModelScope.launch {
                when(event){
                    MoreFoodEvent.Test -> {

                    }
                }
            }
    }
}