// LoginActivity.kt
package com.example.easy_eat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btDaftar = findViewById<Button>(R.id.btDaftar)

        btDaftar.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }


        val linkLogin = findViewById<TextView>(R.id.linkLogin)
        linkLogin.setOnClickListener{
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}
