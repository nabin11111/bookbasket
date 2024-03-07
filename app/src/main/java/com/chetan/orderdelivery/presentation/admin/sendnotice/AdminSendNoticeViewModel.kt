package com.chetan.orderdelivery.presentation.admin.sendnotice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.PushNotificationRequest
import com.chetan.orderdelivery.domain.repository.OneSignalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AdminSendNoticeViewModel @Inject constructor(
    private val oneSignalRepository: OneSignalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AdminSendNoticeState())
    val state: StateFlow<AdminSendNoticeState> = _state

    val onEvent: (event: AdminSendNoticeEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                is AdminSendNoticeEvent.OnNoticeChange -> {
                    _state.update {
                        it.copy(
                            notice = event.value,
                        )
                    }
                }

                is AdminSendNoticeEvent.OnTitleChange -> {
                    _state.update {
                        it.copy(
                            title = event.value,
                        )
                    }
                }

                AdminSendNoticeEvent.DismissInfoMsg -> {
                    _state.update {
                        it.copy(
                            infoMsg = null,
                        )
                    }
                }

                AdminSendNoticeEvent.SendNotice -> {
                    try {
                        val sendNotice = oneSignalRepository.pushNotification(
                            PushNotificationRequest(
                                contents = mapOf("en" to state.value.notice),
                                headings = mapOf("en" to state.value.title),
                                included_segments = listOf(
                                    "All"
                                )
                            )
                        )
                        when (sendNotice) {
                            is Resource.Failure -> {

                            }

                            Resource.Loading -> {

                            }

                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        notice = "",
                                        title = "",
                                        infoMsg = null
                                    )
                                }
                            }
                        }
                    } catch (e: HttpException) {
                        _state.update {
                            it.copy(
                                notice = "",
                                title = "",
                                infoMsg = null
                            )
                        }
                        e.printStackTrace()
                    } catch (e: Throwable) {
                        _state.update {
                            it.copy(
                                notice = "",
                                title = "",
                                infoMsg = null
                            )
                        }
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}