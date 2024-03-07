package com.chetan.orderdelivery.data.repositoryImpl

import com.chetan.orderdelivery.data.Resource
import com.chetan.orderdelivery.data.model.RealtimeModelResponse
import com.chetan.orderdelivery.domain.repository.RealtimeRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RealtimeRepositoryImpl @Inject constructor(
    private val realtime: FirebaseDatabase
) : RealtimeRepository {
    override suspend fun insert(item: RealtimeModelResponse.RealTimeNewOrderRequest): Resource<String> {
        return try {
     realtime.getReference("orders").child("foodOrder").push().setValue(item).await()
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun getItems(): Flow<Resource<List<RealtimeModelResponse>>> = callbackFlow {
                val valueEvent = object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items = snapshot.children.map {
                            RealtimeModelResponse(
                                item = it.getValue(RealtimeModelResponse.RealTimeNewOrderRequest::class.java)?:RealtimeModelResponse.RealTimeNewOrderRequest(false,""),
                                key = it.key?:""
                            )
                        }
                        trySend(Resource.Success(items))
                        println(items)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Resource.Failure(error.toException())
                    }

                }
                realtime.getReference("orders").child("foodOrder").addValueEventListener(valueEvent)
                awaitClose {
                    realtime.getReference("orders").child("foodOrder").removeEventListener(valueEvent)
                    close()
                }

    }

    override fun deliveryState(): Flow<Resource<Boolean>> = callbackFlow {
        val valueEvent = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(Resource.Success(snapshot.getValue(Boolean::class.java)?:false))
            }
            override fun onCancelled(error: DatabaseError) {
                Resource.Failure(error.toException())
            }

        }
        realtime.getReference("orders").child("state").addValueEventListener(valueEvent)
        awaitClose {
            realtime.getReference("orders").child("state").removeEventListener(valueEvent)
            close()
        }

    }

    override suspend fun changeDeliveryState(onOff: Boolean): Resource<Boolean> {
        return try {
            realtime.getReference("orders").child("state").setValue(onOff).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun deleteOrders(): Resource<Boolean> {
        return try {
            realtime.getReference("orders").child("foodOrder").removeValue().await()
            Resource.Success(true)
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

}