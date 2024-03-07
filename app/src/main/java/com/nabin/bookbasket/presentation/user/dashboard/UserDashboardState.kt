package com.nabin.bookbasket.presentation.user.dashboard

import com.nabin.bookbasket.data.model.GetFoodResponse
import com.nabin.bookbasket.data.model.ProfileRequestResponse
import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.presentation.common.components.OrderDeliveryScreenState
import com.nabin.bookbasket.presentation.common.components.dialogs.Message

data class UserDashboardState(
    val userProfile: ProfileRequestResponse = ProfileRequestResponse(),
    val darkMode: Boolean = false,
    val profileUrl: String = "",
    val allFoods : List<GetFoodResponse> = emptyList(),
    val allIds : List<SetOneSignalId> = emptyList(),
    val isNewNotification: Boolean = false,
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)