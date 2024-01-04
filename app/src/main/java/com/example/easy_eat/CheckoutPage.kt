package com.example.easy_eat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckoutPage : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var totalHargaTextView: TextView
    private lateinit var payButton: Button
    private lateinit var ongkirTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_page)

        totalHargaTextView = findViewById(R.id.tvTotalHargaCo)
        payButton = findViewById(R.id.btPay)
        ongkirTextView = findViewById(R.id.tvOngkir)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("cart")
        val userID = auth.currentUser?.uid

        fetchCartItems()

        payButton.setOnClickListener {
            // Implementasikan logika pembayaran
            val address = findViewById<TextView>(R.id.tvAddress).text.toString()
            val phoneNumber = findViewById<TextView>(R.id.tvPhoneNumber).text.toString()
            val paymentMethod = findViewById<TextView>(R.id.paymentMethod).text.toString()

            if (address.isNotBlank() && phoneNumber.isNotBlank() && paymentMethod.isNotBlank()) {
                // Lakukan pembayaran sesuai kebutuhan, misalnya menyimpan ke database atau API pembayaran
                // Setelah pembayaran berhasil, hapus semua item dari keranjang
                hapusSemuaProdukDariCart()

                // Update tampilan dan lainnya sesuai kebutuhan
                totalHargaTextView.text = "Rp.0,00"
                Toast.makeText(this, "Pembayaran berhasil", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Harap lengkapi detail pembayaran", Toast.LENGTH_SHORT).show()
            }
        }

        val btAlamat = findViewById<ImageButton>(R.id.btAlamat)

        btAlamat.setOnClickListener {
            val intent = Intent(this, MapView::class.java)
            startActivity(intent)
        }

        val Pay = findViewById<Button>(R.id.btPay)

        Pay.setOnClickListener {
            val intent = Intent(this, SuccesNotif::class.java)
            startActivity(intent)
        }

        val btBack = findViewById<ImageButton>(R.id.btBack)

        btBack.setOnClickListener {
            val intent = Intent(this, MilkPage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCartItems() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: com.google.firebase.database.DataSnapshot) {
                val totalHarga = calculateTotalharga(dataSnapshot.children)
                val ongkir = 2000  // Ganti dengan nilai ongkir yang sesuai

                // Hitung total belanja
                val totalBelanja = totalHarga + ongkir

                // Setel nilai TextView
                totalHargaTextView.text = "Rp.$totalHarga,00"
                ongkirTextView.text = "Rp.$ongkir,00"

                val totalBelanjaTextView = findViewById<TextView>(R.id.tvTotalBelanja)
                totalBelanjaTextView.text = "Rp.$totalBelanja,00"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun calculateTotalharga(items: Iterable<com.google.firebase.database.DataSnapshot>): Int {
        var totalHarga = 0
        for (itemSnapshot in items) {
            val cartItem = itemSnapshot.getValue(CartDatabase::class.java)
            cartItem?.let {
                totalHarga += it.hargaProduk * it.jumlah
            }
        }
        return totalHarga
    }

    private fun hapusSemuaProdukDariCart() {
        databaseReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Semua produk berhasil dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
