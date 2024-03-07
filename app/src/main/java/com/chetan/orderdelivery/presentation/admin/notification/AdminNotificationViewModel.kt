package com.chetan.orderdelivery.presentation.admin.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.local.Preference
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminNotificationViewModel @Inject constructor(
    private val repository: FirestoreRepository, private val preference: Preference
) : ViewModel() {

    private val _state = MutableStateFlow(AdminNotificationState())
    val state: StateFlow<AdminNotificationState> = _state

    init {
        notification()
    }

    fun notification() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg = Message.Loading(
                        description = "Getting Notifications!!!",
                        title = "Loading",
                        yesNoRequired = false,
                        isCancellable = false
                    )
                )
            }
            val notificationList = repository.getAdminNotification()
            when (notificationList) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            notificationList = notificationList.data.reversed(), infoMsg = null
                        )
                    }
                }
            }
        }
    }

    val onEvent: (event: AdminNotificationEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                AdminNotificationEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }

                is AdminNotificationEvent.DeleteNotification -> {
                    _state.update {
                        it.copy(notificationList = state.value.notificationList.filterNot { it.time == event.id })
                    }
                    val delete = repository.deleteAdminNotification(event.id)
                    when (delete) {
                        is Resource.Failure -> {
                        }

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