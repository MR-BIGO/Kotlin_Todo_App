package com.example.finalsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalsproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerSignupBTN.setOnClickListener {
            if (checkFields()) {
                firebaseAuth.createUserWithEmailAndPassword(
                    binding.registerMailET.text.toString(),
                    binding.registerPasswordET.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(
                    this,
                    "Make Sure All The Fields Are Filled Out Correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.registerHaveAccountTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun checkFields(): Boolean {

        val mail: String = binding.registerMailET.text.toString()
        val password: String = binding.registerPasswordET.text.toString()
        val repeatPassword: String = binding.registerRepeatPasswordET.text.toString()
        val check: Boolean = binding.registerCheckBox.isChecked

        if (mail.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) return false

        if (password != repeatPassword) return false

        if (!check) return false

        val mailSymbol: Int = mail.count { it == '@' }

        if (mailSymbol != 1) return false

        val mailDot: Int = mail.indexOf(".", mail.indexOf("@"))

        if (mailDot < 1) return false

        if (password.count() < 8) return false

        if (!password.contains("[0-9]".toRegex())) return false

        return true
    }
}