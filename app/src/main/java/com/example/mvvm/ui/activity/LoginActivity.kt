package com.example.mvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mvvm.ViewModel.AuthViewModel
import com.example.mvvm.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.authResult.observe(this, Observer { result ->
            result.onSuccess { message ->
                // Handle success, navigate to NotesActivity
                navigateToNotes()
            }
            result.onFailure { exception ->
                // Handle error
                showError(exception.message ?: "Unknown error occurred")
                changeInProgress(false)
            }
        })

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (validateData(email, password)) {
                changeInProgress(true)
                authViewModel.createAccount(email,password)
            }
        }

        binding.createAccountTextViewBtn.setOnClickListener {
            // Navigate to CreateAccountActivity
            navigateToCreateAccount()
        }
    }

    private fun changeInProgress(inProgress: Boolean) {
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.loginBtn.visibility = if (inProgress) View.GONE else View.VISIBLE
    }

    private fun validateData(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            // Show error message
            showError("Email or password cannot be empty")
            return false
        }
        return true
    }

    private fun showError(message: String) {
        // Show error message to the user (e.g., Toast or Snackbar)
    }

    private fun navigateToNotes() {
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close login activity after successful login
    }

    private fun navigateToCreateAccount() {
        // Example navigation to CreateAccountActivity
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }
}
