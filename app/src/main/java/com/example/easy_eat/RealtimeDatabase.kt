package com.example.easy_eat

import com.google.firebase.Firebase
import com.google.firebase.database.database

object RealtimeDatabase {
    fun instance() = Firebase.database
}