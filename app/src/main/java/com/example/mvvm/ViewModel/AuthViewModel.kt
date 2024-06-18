package com.example.mvvm.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.Repository.AuthRepository
import com.example.mvvm.Repository.AuthRepositoryImpl


class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository = AuthRepositoryImpl()

    val authResult: LiveData<Result<String>> = authRepository.authResult

    fun createAccount(email: String, password: String) {
        authRepository.createAccount(email, password)
    }
}
