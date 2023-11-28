package com.example.easy_eat

import com.google.firebase.database.FirebaseDatabase

class FirebaseCartDao(private val userID: String?) : CartAdapter.CartDao {

    val databaseReference = FirebaseDatabase.getInstance().getReference("cart")
    override fun updateItem(item: CartDatabase) {
        databaseReference.child(item.id).setValue(item)
    }

    override fun deleteItem(item: CartDatabase) {
        // Implementasikan logika delete item di Firebase
        databaseReference.child(item.id).setValue(item)
    }
}