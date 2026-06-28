package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.login.LoginActivity
import com.example.myapplication.ui.login.DashboardActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is signed in, go to Dashboard
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                // No user is signed in, go to Login
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()

        }, 3000)
    }
}
