package com.nabin.bookbasket.presentation.common.utils

import kotlin.random.Random

object GenerateRandomNumber {
    fun generateRandomNumber(range: IntRange) : Int {
        return Random.nextInt(range.first, range.last + 1)
    }
}