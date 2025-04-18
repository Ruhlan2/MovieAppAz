package com.isteam.movieappaz.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.isteam.movieappaz.common.network.Resource
import com.isteam.movieappaz.data.source.remote.FirebaseAuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(private val source: FirebaseAuthDataSource) {

    fun registerUserWithEmailAndPassword(email: String, password: String): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading)
            source.registerUserWithEmailAndPassword(email, password).collect {
                when (it) {
                    is Resource.Error -> emit(Resource.Error(it.throwable))
                    Resource.Loading -> Unit
                    is Resource.Success -> emit(Resource.Success(it.result))
                }
            }
        }

    fun loginUserWithEmailAndPassword(email: String, password: String): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading)
            source.loginUserWithEmailAndPassword(email, password).collect {
                when (it) {
                    is Resource.Error -> emit(Resource.Error(it.throwable))
                    Resource.Loading -> Unit
                    is Resource.Success -> emit(Resource.Success(it.result))
                }
            }
        }

    fun signInWithGoogle(account: GoogleSignInAccount):Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        source.signInWithGoogle(account).collect{
            when(it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading->Unit
                is Resource.Success ->emit(Resource.Success(it.result))
            }
        }
    }


    fun resetPassword(email: String): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            source.resetPassword(email).collect {
                when (it) {
                    is Resource.Error -> emit(Resource.Error(it.throwable))
                    Resource.Loading -> Unit
                    is Resource.Success -> emit(Resource.Success(it.result))
                }
            }
        }

    fun deleteCurrentUser(): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)
            source.deleteCurrentUser().collect {
                when (it) {
                    is Resource.Error -> emit(Resource.Error(it.throwable))
                    Resource.Loading -> Unit
                    is Resource.Success -> emit(Resource.Success(it.result))
                }
            }
        }
    fun signOut():Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.logOut().collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun checkVerification(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.checkVerification().collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun sendVerificationEmail(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.sendVerificationEmail().collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }

    fun deleteAccount(token: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        source.deleteAccount(token).collect {
            when (it) {
                is Resource.Error -> emit(Resource.Error(it.throwable))
                Resource.Loading -> Unit
                is Resource.Success -> emit(Resource.Success(it.result))
            }
        }
    }


}