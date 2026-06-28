package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.login.LoginActivity

import com.example.myapplication.model.UserModel
import com.example.myapplication.repository.UserRepoImpl

class RegisterActivity : AppCompatActivity() {

    private val userRepo = UserRepoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val fullName = findViewById<EditText>(R.id.etFullName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val registerBtn = findViewById<Button>(R.id.btnRegister)
        val loginLink = findViewById<TextView>(R.id.tvLoginLink)

        registerBtn?.setOnClickListener {
            val nameStr = fullName?.text.toString().trim()
            val emailStr = email?.text.toString().trim()
            val passStr = password?.text.toString().trim()

            if (nameStr.isNotEmpty() && emailStr.isNotEmpty() && passStr.length >= 6) {
                registerBtn.isEnabled = false
                val user = UserModel(name = nameStr, email = emailStr)
                userRepo.register(user, passStr) { success, message ->
                    registerBtn.isEnabled = true
                    if (success) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }
            } else if (passStr.isNotEmpty() && passStr.length < 6) {
                Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        loginLink?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
