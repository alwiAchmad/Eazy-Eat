package com.example.easy_eat

import android.os.Bundle
import android.view.View.OnCreateContextMenuListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter : CartAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var totalHargaTextView: TextView
    private lateinit var checkButton: Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_page)

        recyclerView = findViewById(R.id.recyclerViewCart)
        totalHargaTextView = findViewById(R.id.tvTotalHarga)
        checkButton = findViewById(R.id.btCheckout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("cart")

        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter

        fetchCartItems()

        checkButton.setOnClickListener{
            //implementasikan logika checkout
        }
    }

    private fun fetchCartItems(){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val cartItems = mutableListOf<CartDatabase>()
                for(itemSnapshot in dataSnapshot.children){
                    val cartItem = itemSnapshot.getValue(CartDatabase::class.java)
                    cartItem?.let { cartItems.add(it) }
                }
                cartAdapter.setItems(cartItems)

                val totalHarga = calculateTotalharga(cartItems)
                totalHargaTextView.text = "Rp.$totalHarga,00"
            }
            override fun onCancelled(databaseError: DatabaseError){
                //Handle eror
            }
        })
    }

    private fun calculateTotalharga(items: List<CartDatabase>):Int{
        var totalHarga = 0
        for(item in items){
            totalHarga += item.hargaProduk * item.jumlah
        }
        return totalHarga
    }

}