package com.example.mvvm.Repository

import androidx.lifecycle.LiveData

interface AuthRepository {
    val authResult: LiveData<Result<String>>
    fun createAccount(email: String, password: String)
}
