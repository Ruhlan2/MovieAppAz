package com.isteam.movieappaz.domain.useCase

import com.isteam.movieappaz.data.repository.ConnectionRepository
import javax.inject.Inject

class CheckConnectionUseCase @Inject constructor(private val repo:ConnectionRepository) {

    fun checkConnection()=repo.checkNetwork()
}