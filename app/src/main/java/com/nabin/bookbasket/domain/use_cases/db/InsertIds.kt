package com.nabin.bookbasket.domain.use_cases.db

import com.nabin.bookbasket.domain.model.SetOneSignalId
import com.nabin.bookbasket.domain.repository.DBRepository
import javax.inject.Inject

class InsertIds @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(ids: List<SetOneSignalId>) = dbRepository.insertIds(ids)
}