package com.chetan.orderdelivery.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SetOneSignalId(
    @PrimaryKey
    val id: String = "",
    val branch: String = ""
)
