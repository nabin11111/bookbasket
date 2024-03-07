package com.chetan.orderdelivery.presentation.user.notification

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
class NotificationViewModel @Inject constructor(
    private val repository: FirestoreRepository,
    private val preference: Preference
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state: StateFlow<NotificationState> = _state

    init {
        preference.isNewNotification = false
        notification()
    }
    fun notification() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    infoMsg =Message.Loading(
                        description = "Getting Notifications!!!",
                        title = "Loading",
                        yesNoRequired = false,
                        isCancellable = false
                    )
                )
            }
            val notificationList = repository.getNotification()
            when (notificationList) {
                is Resource.Failure -> {

                }

                Resource.Loading -> {

                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            notificationList = notificationList.data.reversed(),
                            infoMsg = null
                        )
                    }
                }
            }
        }
    }

    val onEvent: (event: NotificationEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                NotificationEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null
                        )
                    }
                }
                is NotificationEvent.ChangeToRead -> {
                    val changeToRead = repository.readNotification(event.id)
                    when (changeToRead) {
                        is Resource.Failure -> {

                        }
                        Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            _state.update {
                                it.copy(notificationList = state.value.notificationList.map {
                                    if (it.time == event.id) {
                                        it.copy(readNotice = true)
                                    } else {
                                        it.copy(readNotice = it.readNotice)
                                    }
                                })
                            }
                        }
                    }
                }

                is NotificationEvent.DeleteNotification -> {
                    _state.update {
                        it.copy(notificationList = state.value.notificationList.filterNot { it.time == event.id })
                    }
                    val delete = repository.deleteNotification(event.id)
                    when (delete) {
                        is Resource.Failure -> {
                        }
                        Resource.Loading -> {
                        }
                        is Resource.Success -> {

                        }
                    }

                }

                NotificationEvent.DeleteAll -> {

                }
            }
        }
    }
}