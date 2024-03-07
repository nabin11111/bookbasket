package com.nabin.bookbasket.data.model

import android.net.Uri
import com.nabin.bookbasket.presentation.common.utils.GenerateRandomNumber

data class ImageStorageDetails(
    val imageUri : Uri = Uri.EMPTY,
    val imagePath : String = "",
    val imageName : String = GenerateRandomNumber.generateRandomNumber(11111111..99999999).toString()
)
