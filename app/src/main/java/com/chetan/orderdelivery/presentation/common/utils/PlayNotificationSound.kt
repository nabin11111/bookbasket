package com.chetan.orderdelivery.presentation.common.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

fun PlayNotificationSound(context: Context){
    val notificationSound: Uri = RingtoneManager.getDefaultUri(
        RingtoneManager.TYPE_NOTIFICATION
    )
    val mediaPlayer = MediaPlayer.create(context, notificationSound)
    mediaPlayer.start()
}