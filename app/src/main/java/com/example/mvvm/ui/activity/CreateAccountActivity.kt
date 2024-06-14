package com.example.mvvm.ui.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm.ViewModel.AuthViewModel
import com.example.mvvm.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (validateData(email, password, confirmPassword)) {
                createAccount(email, password)
            }
        }

        binding.loginTextViewBtn.setOnClickListener {
            // Navigate to LoginActivity
            finish() // Assuming LoginActivity is the parent activity
        }

        authViewModel.authResult.observe(this, { result ->
            result.onSuccess { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
            result.onFailure { exception ->
             showError(exception.message ?: "Unknown error occurred")
                changeInProgress(false)
            }
        })
    }

    private fun validateData(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showError("Please fill in all fields.")
            return false
        }
        if (password != confirmPassword) {
            showError("Passwords do not match.")
            return false
        }
        return true
    }

    private fun createAccount(email: String, password: String) {
        changeInProgress(true)
        authViewModel.createAccount(email, password)
    }

    private fun changeInProgress(inProgress: Boolean) {
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.createAccountBtn.isEnabled = !inProgress
        binding.loginTextViewBtn.isEnabled = !inProgress
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        // Navigate to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        finish() // Assuming LoginActivity is the parent activity
    }
}
