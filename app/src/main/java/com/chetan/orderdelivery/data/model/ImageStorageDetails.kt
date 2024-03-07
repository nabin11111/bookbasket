package com.chetan.orderdelivery.data.model

import android.net.Uri
import com.chetan.orderdelivery.presentation.common.utils.GenerateRandomNumber

data class ImageStorageDetails(
    val imageUri : Uri = Uri.EMPTY,
    val imagePath : String = "",
    val imageName : String = GenerateRandomNumber.generateRandomNumber(11111111..99999999).toString()
)
