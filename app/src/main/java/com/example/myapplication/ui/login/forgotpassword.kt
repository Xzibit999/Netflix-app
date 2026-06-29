package com.example.myapplication.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

import com.example.myapplication.repository.UserRepoImpl

class ForgotPasswordActivity : AppCompatActivity() {

    private val userRepo = UserRepoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        val email = findViewById<EditText>(R.id.etEmail)
        val resetBtn = findViewById<Button>(R.id.btnReset)

        resetBtn?.setOnClickListener {
            val userEmail = email?.text.toString().trim()
            if (userEmail.isNotEmpty()) {
                resetBtn.isEnabled = false
                userRepo.forgetPassword(userEmail) { success, message ->
                    resetBtn.isEnabled = true
                    if (success) {
                        Toast.makeText(this, "Reset link sent to your email!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
