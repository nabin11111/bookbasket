package com.chetan.orderdelivery.di

import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface HiltEntryPoint {
    fun firestoreUseCases() : FirestoreUseCases
}