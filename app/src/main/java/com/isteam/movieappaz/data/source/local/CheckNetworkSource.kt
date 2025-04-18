package com.isteam.movieappaz.data.source.local

import android.content.Context
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.data.service.connection.CheckConnectivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckNetworkSource @Inject constructor(private val connectivity: CheckConnectivity,private val context: Context) {

    fun checkConnectivity():Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(connectivity.checkNetwork(context)))
        }catch (e:Exception){
            emit(Resource.Error(e))
        }
    }
}