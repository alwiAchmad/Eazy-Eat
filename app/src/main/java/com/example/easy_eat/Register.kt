package com.example.easy_eat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class Register : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNumCall: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPass: EditText

    private lateinit var svRegister: Button

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        inisialisasiView()

        svRegister.apply {
            setOnClickListener {
                registerUser()
            }
        }

        val toLogin = findViewById<Button>(R.id.toLogin)

        toLogin.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
    }

    private fun inisialisasiView() {
        etName = findViewById(R.id.etName)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etNumCall = findViewById(R.id.etCallNum)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPass = findViewById(R.id.etConfirmPass)

        svRegister = findViewById(R.id.svRegister)
    }

    private fun getDataPengguna(): Pengguna {
        return Pengguna(
            id = UUID.randomUUID().toString(),
            username = etUsername.text.toString(),
            NamaLengkap = etName.text.toString().trim(),
            Email = etEmail.text.toString(),
            NoTelp = etNumCall.text.toString(),
            Password = etPassword.text.toString(),
            KonfirmPass = etConfirmPass.text.toString()
        )
    }

    private fun registerUser() {
        val pengguna = getDataPengguna()

        if (pengguna.NamaLengkap.isNotEmpty() && pengguna.username.isNotEmpty() && pengguna.Email.isNotEmpty() &&
            pengguna.NoTelp.isNotEmpty() && pengguna.Password.isNotEmpty() && pengguna.KonfirmPass.isNotEmpty()
        ) {
            svRegister.isEnabled = false

            // Membuat pengguna baru dengan email dan password
            auth.createUserWithEmailAndPassword(pengguna.Email, pengguna.Password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Pengguna berhasil dibuat
                        etUsername.setText("")
                        etName.setText("")
                        etEmail.setText("")
                        etNumCall.setText("")
                        etPassword.setText("")
                        etConfirmPass.setText("")
                        etConfirmPass != etPassword

                        svRegister.isEnabled = true

                        Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    } else {
                        // Registrasi gagal
                        svRegister.isEnabled = true
                        Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Menyembunyikan Keyboard
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }
}
