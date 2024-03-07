package com.chetan.orderdelivery.data.model

data class PushNotificationRequest(
    val app_id :String = "473febeb-3878-459e-a056-6e0f67ad6fb8",
//    val include_subscription_ids : List<String> = listOf(""),
    val include_player_ids : List<String> = emptyList(),
    val included_segments: List<String> = emptyList(),
    val contents: Map<String, String>,
    val headings: Map<String, String>
)