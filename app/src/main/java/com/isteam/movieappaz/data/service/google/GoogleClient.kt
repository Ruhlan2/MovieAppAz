package com.isteam.movieappaz.data.service.google

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.isteam.movieappaz.common.utils.Constants
import javax.inject.Inject

class GoogleClient @Inject constructor(private val auth:FirebaseAuth, private val context: Context){
     fun signInWithGoogle():GoogleSignInClient{
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context,gso)
    }
}