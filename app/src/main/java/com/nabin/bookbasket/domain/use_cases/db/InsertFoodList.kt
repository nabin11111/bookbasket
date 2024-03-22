package com.nabin.bookbasket.domain.use_cases.db

import com.nabin.bookbasket.domain.model.AllBooks
import com.nabin.bookbasket.domain.repository.DBRepository
import javax.inject.Inject

class InsertFoodList @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(list: List<AllBooks>) = dbRepository.insertFoodList(list)
}