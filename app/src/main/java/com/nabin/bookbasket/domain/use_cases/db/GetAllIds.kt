package com.nabin.bookbasket.domain.use_cases.db

import com.nabin.bookbasket.domain.repository.DBRepository
import javax.inject.Inject

class GetAllIds @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke() = dbRepository.getAllIds()
}