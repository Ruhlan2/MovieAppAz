package com.isteam.movieappaz.domain.useCase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.isteam.movieappaz.data.repository.FirebaseAuthRepository
import javax.inject.Inject

class FirebaseAuthUseCase @Inject constructor(private val repo: FirebaseAuthRepository) {

    fun registerUserWithEmailAndPassword(email: String, password: String) =
        repo.registerUserWithEmailAndPassword(email, password)

    fun loginUserWithEmailAndPassword(email: String, password: String) =
        repo.loginUserWithEmailAndPassword(email, password)

    fun resetPassword(email: String) = repo.resetPassword(email)

    fun deleteUser() = repo.deleteCurrentUser()

    fun signOut()=repo.signOut()

    fun signInWithGoogle(account:GoogleSignInAccount)=repo.signInWithGoogle(account)

    fun checkVerification() = repo.checkVerification()

    fun sendVerification() = repo.sendVerificationEmail()

    fun deleteAccount(token: String) = repo.deleteAccount(token)


}