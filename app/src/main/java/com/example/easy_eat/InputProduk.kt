package com.example.easy_eat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class InputProduk : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1

    private lateinit var etJenisProduk: EditText
    private lateinit var btnPickImage: Button
    private lateinit var etNamaproduk: EditText
    private lateinit var etHarga: EditText
    private lateinit var etStok: EditText

    private lateinit var btInputProduk: Button
    private lateinit var imageView: ImageView

    private val auth by lazy { RealtimeDatabase.instance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_barang)

        etJenisProduk = findViewById(R.id.etJenisProduk)
        btnPickImage = findViewById(R.id.btnPickImage)
        etNamaproduk = findViewById(R.id.etNamaProduk)
        etHarga = findViewById(R.id.etHarga)
        etStok = findViewById(R.id.etStok)
        btInputProduk = findViewById(R.id.btInputProduk)
        imageView = findViewById(R.id.imgProduk)

        btInputProduk.setOnClickListener {
            inputProduk()
        }

        imageView.setOnClickListener {
            pickImageFromGallery()
        }

        val btBack = findViewById<ImageButton>(R.id.btBack)

        btBack.setOnClickListener {
            val intent = Intent(this, MilkPage::class.java)
            startActivity(intent)
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            imageView.setImageURI(selectedImageUri)
            btnPickImage.text = selectedImageUri.toString() // Set text to image URL
        }
    }

    private fun getDataProduk(): DatabaseBarang {
        val harga = etHarga.text.toString()
        val stok = etStok.text.toString()

        return DatabaseBarang(
            id = UUID.randomUUID().toString(),
            imgBarangUrl = btnPickImage.text.toString(),
            jenis = etJenisProduk.text.toString(),
            nama = etNamaproduk.text.toString(),
            Harga = harga.toInt(),
            stok = stok.toInt()
        )
    }

    private fun inputProduk() {
        val produk = getDataProduk()

        if (
            produk.imgBarangUrl.isNotEmpty() &&
            produk.jenis.isNotEmpty() && produk.nama.isNotEmpty()
        ) {
            btInputProduk.isEnabled = false
            auth.getReference("produk").child(produk.id).setValue(produk)
                .addOnSuccessListener {
                    btnPickImage.text = ""
                    etJenisProduk.setText("")
                    etHarga.setText("")
                    etStok.setText("")

                    btInputProduk.isEnabled = true

                    Toast.makeText(this, "Data produk berhasil disimpan", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    btInputProduk.isEnabled = true
                    Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                }
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
