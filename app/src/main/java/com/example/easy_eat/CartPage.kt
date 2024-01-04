package com.example.easy_eat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var totalHargaTextView: TextView
    private lateinit var checkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_page)

        recyclerView = findViewById(R.id.recyclerViewCart)
        totalHargaTextView = findViewById(R.id.tvTotalHarga)
        checkButton = findViewById(R.id.btCheckout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("cart")
        val userID = auth.currentUser?.uid


        val cartDao = FirebaseCartDao(userID)
        cartAdapter = CartAdapter(cartDao)
        recyclerView.adapter = cartAdapter

        fetchCartItems()

        checkButton.setOnClickListener {
            // Implementasikan logika checkout
        }

        val Checkout = findViewById<TextView>(R.id.btCheckout)

        Checkout.setOnClickListener {
            val intent = Intent(this, CheckoutPage::class.java)
            startActivity(intent)
        }

        val btBack = findViewById<ImageButton>(R.id.btBackk)

        btBack.setOnClickListener {
            val intent = Intent(this, MilkPage::class.java)
            startActivity(intent)
        }

        cartAdapter.setOnAddProdukClickListener(object : CartAdapter.OnAddProdukClickListener {
            override fun onAddProdukClick(position: Int) {
                onAddProdukclick(position)
            }
        })

        cartAdapter.setOnMinusProdukClickListener(object : CartAdapter.OnMinusProdukClickListener {
            override fun onMinusProdukClick(position: Int) {
                onMinusProdukclick(position)
            }
        })
    }

    private fun updateTotalHarga() {
        val totalHarga = cartAdapter.calculateTotalHarga()
        totalHargaTextView.text = "Rp.$totalHarga,00"
    }

    private fun onAddProdukclick(position: Int) {
        cartAdapter.incrementJumlahproduk(position)
        updateTotalHarga()
    }

    private fun onMinusProdukclick(position: Int) {
        val item = cartAdapter.getItem(position)
        cartAdapter.decrementJumlahProduk(position)
        updateTotalHarga()

        if (item.jumlah == 0){
            hapusProdukDariCart(item.id)
        }
    }
    private fun hapusProdukDariCart(itemId: String) {
        databaseReference.child(itemId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()

                // Update tampilan setelah menghapus produk dari database
                fetchCartItems()
            } else {
                Toast.makeText(this, "Produk gagal dihapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCartItems() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartDatabase>()
                for (itemSnapshot in dataSnapshot.children) {
                    val cartItem = itemSnapshot.getValue(CartDatabase::class.java)
                    cartItem?.let {
                        val existingItem = cartItems.find { existingItem -> existingItem.id == it.id }
                        if (existingItem != null) {
                            it.jumlah = existingItem.jumlah
                        }
                        cartItems.add(it)
                    }
                }
                cartAdapter.setItems(cartItems)

                val totalHarga = calculateTotalharga(cartItems)
                totalHargaTextView.text = "Rp.$totalHarga,00"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun calculateTotalharga(items: List<CartDatabase>): Int {
        var totalHarga = 0
        for (item in items) {
            totalHarga += item.hargaProduk * item.jumlah
        }
        return totalHarga
    }
}
