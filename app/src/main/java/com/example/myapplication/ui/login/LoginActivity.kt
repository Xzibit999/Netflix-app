package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.RegisterActivity
import com.google.android.material.textfield.TextInputEditText

import com.example.myapplication.repository.UserRepoImpl

class LoginActivity : AppCompatActivity() {

    private val userRepo = UserRepoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<TextInputEditText>(R.id.etEmail)
        val password = findViewById<TextInputEditText>(R.id.etPassword)

        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val register = findViewById<TextView>(R.id.tvRegister)
        val forgot = findViewById<TextView>(R.id.tvForgot)

        loginBtn?.setOnClickListener {
            val userEmail = email?.text.toString().trim()
            val userPassword = password?.text.toString().trim()

            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                loginBtn.isEnabled = false
                userRepo.login(userEmail, userPassword) { success, message ->
                    loginBtn.isEnabled = true
                    if (success) {
                        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed: $message", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }

        register?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgot?.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}
