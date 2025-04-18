package com.isteam.movieappaz.common.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun connectionObserve():Flow<ConnectionStatus>

    enum class ConnectionStatus{
        Available,Unavailable,Lost,Losing
    }
}