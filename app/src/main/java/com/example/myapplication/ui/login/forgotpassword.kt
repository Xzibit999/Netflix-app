package com.example.myapplication.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        val email = findViewById<EditText>(R.id.etEmail)
        val resetBtn = findViewById<Button>(R.id.btnReset)

        resetBtn?.setOnClickListener {
            val userEmail = email?.text.toString().trim()
            if (userEmail.isNotEmpty()) {
                Toast.makeText(this, getString(R.string.reset_link_sent), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
