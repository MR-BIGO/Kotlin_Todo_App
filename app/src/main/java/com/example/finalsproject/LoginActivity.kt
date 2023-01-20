package com.example.finalsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val logInBtn: Button = findViewById(R.id.loginSignInBTN)
        logInBtn.setOnClickListener {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }

        val registerBtn: Button = findViewById(R.id.loginRegisterBTN)
        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}