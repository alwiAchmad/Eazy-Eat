// LoginActivity.kt
package com.example.easy_eat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var lgEmail: EditText
    private lateinit var lgPassword: EditText
    private lateinit var lgLogin: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lgEmail = findViewById(R.id.lgEmail) // Update the ID to match your layout
        lgPassword = findViewById(R.id.lgPassword)
        lgLogin = findViewById(R.id.lgLogin)

        firebaseAuth = FirebaseAuth.getInstance()

        lgLogin.setOnClickListener {
            loginUser()
        }

        val lgRegister = findViewById<Button>(R.id.lgRegister)

        lgRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = lgEmail.text.toString().trim()
        val password = lgPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login berhasil
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    // Tambahan: Pindah ke halaman utama aplikasi setelah login
                    val intent = Intent(this, MilkPage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Login gagal. Periksa kembali email dan password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
