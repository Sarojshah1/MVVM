package com.example.mvvm.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl : AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authResult = MutableLiveData<Result<String>>()
    override val authResult: LiveData<Result<String>> = _authResult

    override fun createAccount(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authResult.value = Result.success("Successfully created account. Check email to verify.")
                firebaseAuth.currentUser?.sendEmailVerification()
                firebaseAuth.signOut()
            } else {
                _authResult.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }
}
