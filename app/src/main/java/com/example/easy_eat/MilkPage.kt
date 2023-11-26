package com.example.easy_eat

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID

class MilkPage : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var tvNamaProduk : TextView
    private lateinit var tvHargaProduk : TextView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.milk_page)

        FirebaseApp.initializeApp(this)

        cartAdapter = CartAdapter()

        imageView  = findViewById(R.id.imageProduk)
        tvNamaProduk = findViewById(R.id.tvNamaProduk)
        tvHargaProduk  = findViewById(R.id.tvHargaProduk)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cartAdapter

        fetchProductDetails()

        val addToCartButton : Button = findViewById(R.id.btAddToCart)
        addToCartButton.setOnClickListener{
            //Tambahkan fungsi untuk menambahkan ke keranjang
            addtoCart()
        }

        val btToCart : ImageButton = findViewById(R.id.btToCart)
        btToCart.setOnClickListener{
            val intent = Intent(this, CartPage::class.java)
            startActivity(intent)
        }

    }

    // Metode untuk mengambil dan menampilkan gambar produk
    private fun fetchProductDetails() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("produk")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Hanya mengambil gambar dari satu produk (silakan sesuaikan dengan kebutuhan)
                val productSnapshot = dataSnapshot.children.firstOrNull()
                if (productSnapshot != null) {
                    val imageUrl = productSnapshot.child("imgBarangUrl").value.toString()
                    val namaProduk = productSnapshot.child("nama").value.toString()
                    val hargaProduk = productSnapshot.child("harga").value.toString()

                    // Gunakan UI yang diberikan
                    displayProductImage(imageUrl, namaProduk, hargaProduk)

                    //Menambahkan produk ke keranjang
                    val cartDatabase = CartDatabase(
                        id = productSnapshot.key.toString(),
                        imgProdukUrl = imageUrl,
                        namaProduk = namaProduk,
                        hargaProduk = hargaProduk.toInt(),
                        jumlah = 1
                    )
                    cartAdapter.addItem(cartDatabase)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MilkPage", "onCancelled called.Error: ${databaseError.message}")
            }
        })
    }

    private fun addtoCart(){
        val imgProduk: ImageView = findViewById(R.id.imageProduk)
        val tvNamaProduk: TextView = findViewById(R.id.tvNamaProduk)
        val tvHargaProduk: TextView = findViewById(R.id.tvHargaProduk)

        val imageUrl = Glide.with(this).load(imgProduk).toString()
        val namaProduk = tvNamaProduk.text.toString()
        val hargaProduk = tvHargaProduk.text.toString()

        val cartDatabase = CartDatabase(
            id = UUID.randomUUID().toString(),
            imgProdukUrl = imageUrl,
            namaProduk = namaProduk,
            hargaProduk = hargaProduk.toInt(),
            jumlah = 1
        )
    }

    private fun updateTotalHarga(){
        val totalHarga = cartAdapter.calculateTotalHarga()
    }

    fun onAddProdukclick(position: Int) {
        cartAdapter.incrementJumlahproduk(position)
        updateTotalHarga()
    }

    fun onMinusProdukClick(position: Int) {
        cartAdapter.decrementJumlahProduk(position)
        updateTotalHarga()
    }

    private fun displayProductImage(imageUrl: String, namaProduk: String, hargaProduk: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        tvNamaProduk.text = namaProduk
        tvHargaProduk.text = "Rp.$hargaProduk,00"
    }
}
