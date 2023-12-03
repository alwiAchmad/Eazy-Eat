package com.example.easy_eat

class Pengguna (
    val id : String,
    val username: String,
//    val NamaLengkap: String,
    val Email: String,
//    val NoTelp: String,
    val Password: String,
    val KonfirmPass: String
){
    // Konstruktor kosong diperlukan untuk deserialization dari Firebase
    constructor() : this("", "", "", "", "")
}