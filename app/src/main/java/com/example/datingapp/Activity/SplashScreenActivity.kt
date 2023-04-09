package com.example.datingapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.datingapp.Auth.LoginActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)


        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({

            if (user == null){
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity , MainActivity::class.java))

            }
            finish()
        },2000)
    }
}