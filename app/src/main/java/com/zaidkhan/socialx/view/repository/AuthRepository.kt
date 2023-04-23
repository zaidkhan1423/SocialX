package com.zaidkhan.socialx.view.repository

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {

    // A function to get the sign-in intent from the GoogleSignInClient
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    // A function to handle the sign-in result and return a LiveData of FirebaseUser
    fun handleSignInResult(data: Intent?): LiveData<FirebaseUser?> {
        val result = MutableLiveData<FirebaseUser?>()
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update the result with the signed-in user's information
                        result.value = auth.currentUser
                    } else {
                        // If sign in fails, update the result with null
                        result.value = null
                    }
                }
        } catch (e: ApiException) {
            // Google Sign In failed, update the result with null
            result.value = null
        }
        return result
    }

    // A function to sign out the user from both Firebase and Google
    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}