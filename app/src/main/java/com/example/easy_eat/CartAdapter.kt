package com.example.easy_eat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var CartDatabase: List<CartDatabase> = emptyList()
    private var cartItems: MutableList<CartDatabase> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_page, parent, false)
        return ViewHolder(view)
    }

    fun addItem(item: CartDatabase){
        cartItems.add(item)
        notifyDataSetChanged()
    }

    fun incrementJumlahproduk(position: Int){
        val item = cartItems[position]
        item.jumlah++
        notifyItemChanged(position)
    }
    fun decrementJumlahProduk(position: Int){
        val item = cartItems[position]
        if (item.jumlah > 1) {
            item.jumlah--
            notifyItemChanged(position)
        }else{
            cartItems.removeAt(position)
            notifyItemChanged(position)
        }
    }

    fun calculateTotalHarga():Int {
        var totalHarga = 0
        for(item in cartItems){
            totalHarga += item.hargaProduk*item.jumlah
        }
        return totalHarga
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = CartDatabase[position]

        Glide.with(holder.itemView.context)
            .load(item.imgProdukUrl)
            .into(holder.imgProduk)

        holder.tvNamaProduk.text = item.namaProduk
        holder.tvHargaProduk.text = "Rp.${item.hargaProduk},00"
        holder.tvJumlahProduk.text = item.jumlah.toString()

        holder.btAddProduk.setOnClickListener{
            //Tambahkan kode untuk tambah
            incrementJumlahproduk(position)
            holder.tvTotalHarga.text = "Rp.${calculateTotalHarga()},00"
        }
        holder.btMinusProduk.setOnClickListener{
            //Tambahkan kode untuk kurang
            decrementJumlahProduk(position)
            holder.tvTotalHarga.text = "Rp.${calculateTotalHarga()},00"
        }
    }

    override fun getItemCount(): Int {
        return CartDatabase.size
    }
    fun setItems(items: List<CartDatabase>){
        CartDatabase = items
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        val imgProduk: ImageView = itemView.findViewById(R.id.imgProduk)
        val tvNamaProduk: TextView = itemView.findViewById(R.id.tvNamaProduk)
        val tvHargaProduk: TextView = itemView.findViewById(R.id.tvHargaProduk)
        val tvJumlahProduk: TextView = itemView.findViewById(R.id.tvJumlahProduk)
        val btAddProduk: ImageButton = itemView.findViewById(R.id.btAddProduk)
        val btMinusProduk: ImageButton = itemView.findViewById(R.id.btMinusProduk)
        val tvTotalHarga : TextView = itemView.findViewById(R.id.tvTotalHarga)
    }
}