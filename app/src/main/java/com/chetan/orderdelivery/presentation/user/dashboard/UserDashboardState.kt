package com.chetan.orderdelivery.presentation.user.dashboard

import com.chetan.orderdelivery.data.model.GetFoodResponse
import com.chetan.orderdelivery.data.model.ProfileRequestResponse
import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.presentation.common.components.OrderDeliveryScreenState
import com.chetan.orderdelivery.presentation.common.components.dialogs.Message

data class UserDashboardState(
    val userProfile: ProfileRequestResponse = ProfileRequestResponse(),
    val darkMode: Boolean = false,
    val profileUrl: String = "",
    val allFoods : List<GetFoodResponse> = emptyList(),
    val allIds : List<SetOneSignalId> = emptyList(),
    val isNewNotification: Boolean = false,
    override val infoMsg: Message? = null
) : OrderDeliveryScreenState(infoMsg)