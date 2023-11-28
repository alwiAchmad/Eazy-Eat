package com.example.easy_eat

class CartDatabase(
    val id : String,
    val imgProdukUrl : String,
    val namaProduk: String,
    val hargaProduk: Int = 0,
    var jumlah : Int = 0
){
    constructor() : this("", "", "", 0, 0)
}