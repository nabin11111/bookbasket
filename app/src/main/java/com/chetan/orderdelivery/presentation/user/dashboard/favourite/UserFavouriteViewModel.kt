package com.chetan.orderdelivery.presentation.user.dashboard.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.domain.repository.FirestoreRepository
import com.chetan.orderdelivery.domain.use_cases.db.DBUseCases
import com.chetan.orderdelivery.domain.use_cases.firestore.FirestoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFavouriteViewModel @Inject constructor(
    private val dbUseCases: DBUseCases,
    private val firestoreUseCases: FirestoreUseCases
) : ViewModel(){
    private val _state = MutableStateFlow(UserFavouriteState())
    val state : StateFlow<UserFavouriteState> = _state

    init {
        getFavList()
    }

    private fun getFavList(){
        viewModelScope.launch {
            val allFavList = firestoreUseCases.getFavouriteList()
            when(allFavList){
                is Resource.Failure -> {

                }
                Resource.Loading -> {

                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            allFoods = dbUseCases.getAllFoods().filter { food ->
                                allFavList.data.any { it.foodId == food.foodId }
                            }
                        )
                    }
                }
            }
        }
    }
    val onEvent: (event: UserFavouriteEvent) -> Unit = {event ->
        viewModelScope.launch {
            when(event){
                UserFavouriteEvent.OrderAgain -> {

                }

                is UserFavouriteEvent.RemoveFavourite -> {
                    val setFav = firestoreUseCases.setFavourite(
                        foodId = event.id
                    )
                    when (setFav) {
                        is Resource.Failure -> {

                        }

                        Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            if (setFav.data) {
                                getFavList()
                            }
                        }
                    }
                }
            }
        }
    }
}