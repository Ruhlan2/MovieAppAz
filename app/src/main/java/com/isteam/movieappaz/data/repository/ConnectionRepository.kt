package com.isteam.movieappaz.data.repository

import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.data.service.connection.CheckConnectivity
import com.isteam.movieappaz.data.source.local.CheckNetworkSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConnectionRepository @Inject constructor(private val source:CheckNetworkSource) {
     fun checkNetwork():Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.checkConnectivity().collect{
            when(it){
                is Resource.Error ->emit(Resource.Error(it.throwable))
                Resource.Loading->Unit
                is Resource.Success->emit(Resource.Success(it.result))
            }
        }

    }

}