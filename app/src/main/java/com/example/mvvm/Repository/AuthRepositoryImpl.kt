package com.example.mvvm.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authResult = MutableLiveData<Result<String>>()
    override val authResult: LiveData<Result<String>> = _authResult

    override fun createAccount(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // User creation successful, send email verification
                firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                    if (verificationTask.isSuccessful) {
                        _authResult.value = Result.success("Successfully created account. Check email to verify.")
                    } else {
                        _authResult.value = Result.failure(verificationTask.exception ?: Exception("Email verification failed"))
                    }
                }
            } else {
                // Handle specific exceptions
                val exception = task.exception
                when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        _authResult.value = Result.failure(Exception("Email already in use."))
                    }
                    else -> {
                        _authResult.value = Result.failure(exception ?: Exception("Unknown error occurred"))
                    }
                }
            }
        }
    }
}
