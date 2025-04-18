package com.isteam.movieappaz.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isteam.movieappaz.common.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel<STATE : State> : ViewModel() {

    private val _state: MutableLiveData<STATE> = MutableLiveData<STATE>()
    val state: LiveData<STATE> get() = _state

    fun setState(state: STATE) {
        _state.value = state
    }

    inline fun <T> Flow<Resource<T>>.handleResult(
        crossinline onComplete: (T) -> Unit,
        crossinline onError: (Exception) -> Unit,
        crossinline onLoading: () -> Unit
    ) {
        onEach { response ->
            when (response) {
                is Resource.Error -> {
                    onError(response.throwable)
                }

                is Resource.Success -> {
                    response.result?.let { onComplete(it) }
                }

                is Resource.Loading -> {
                    onLoading()
                }
            }
        }.launchIn(viewModelScope)
    }
}

interface State
