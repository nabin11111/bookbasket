package com.chetan.orderdelivery.domain.use_cases.db

import com.chetan.orderdelivery.domain.model.SetOneSignalId
import com.chetan.orderdelivery.domain.repository.DBRepository
import javax.inject.Inject

class InsertIds @Inject constructor(
    private val dbRepository: DBRepository
) {
    suspend operator fun invoke(ids: List<SetOneSignalId>) = dbRepository.insertIds(ids)
}