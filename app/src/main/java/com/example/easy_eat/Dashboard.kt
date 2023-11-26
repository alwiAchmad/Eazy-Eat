package com.example.easy_eat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_page)

        val btSusu = findViewById<ImageButton>(R.id.btSusu)
        btSusu.setOnClickListener{
            val intent = Intent(this, MilkPage::class.java)
            startActivity(intent)
        }
    }
}