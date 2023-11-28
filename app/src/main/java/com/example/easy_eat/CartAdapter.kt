package com.example.easy_eat

import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class CartAdapter(private val cartDao : CartDao) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var cartItems: MutableList<CartDatabase> = mutableListOf()
    private var onAddProdukClickListener: OnAddProdukClickListener? = null
    private var onMinusProdukClickListener: OnMinusProdukClickListener? = null

    interface CartDao {
        fun updateItem(item: CartDatabase)
        fun deleteItem(item: CartDatabase)
    }


    interface OnAddProdukClickListener {
        fun onAddProdukClick(position: Int)
    }

    interface OnMinusProdukClickListener {
        fun onMinusProdukClick(position: Int)
    }


    private fun updateItemInDatabase(item: CartDatabase){
        GlobalScope.launch(Dispatchers.IO) {
            cartDao.updateItem(item)
        }
    }

    private fun removeItemFromDatabase(item: CartDatabase){
        GlobalScope.launch(Dispatchers.IO) {
            cartDao.deleteItem(item)
        }
    }

    fun setOnAddProdukClickListener(listener: OnAddProdukClickListener) {
        this.onAddProdukClickListener = listener
    }


    fun setOnMinusProdukClickListener(listener: OnMinusProdukClickListener) {
        this.onMinusProdukClickListener = listener
    }

    fun addItem(item: CartDatabase) {
        cartItems.add(item)
        notifyDataSetChanged()
    }

    fun incrementJumlahproduk(position: Int) {
        val item = cartItems[position]
        item.jumlah++
        updateItemInDatabase(item)
        notifyItemChanged(position)
    }

    fun decrementJumlahProduk(position: Int) {
        val item = cartItems[position]
        if (item.jumlah > 1) {
            item.jumlah--
            updateItemInDatabase(item)
            notifyItemChanged(position)
        } else {
            removeItemFromDatabase(item)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun calculateTotalHarga(): Int {
        var totalHarga = 0
        for (item in cartItems) {
            totalHarga += item.hargaProduk * item.jumlah
        }
        return totalHarga
    }

    fun getItems(): List<CartDatabase> {
        return cartItems
    }

    fun indexOf(item: CartDatabase): Int {
        return cartItems.indexOf(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_produk, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartItems[position]

        Glide.with(holder.itemView.context)
            .load(item.imgProdukUrl)
            .into(holder.imgProduk)

        holder.tvNamaProduk.text = item.namaProduk
        holder.tvHargaProduk.text = "Rp.${item.hargaProduk},00"
        holder.tvJumlahProduk.text = item.jumlah.toString()
        holder.tvTotalHarga?.text = "Rp.${item.hargaProduk * item.jumlah},00"

        holder.btAddProduk?.setOnClickListener {
            onAddProdukClickListener?.onAddProdukClick(position)
        }

        holder.btMinusProduk?.setOnClickListener {
            onMinusProdukClickListener?.onMinusProdukClick(position)
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun setItems(items: List<CartDatabase>) {
        cartItems.clear()
        cartItems.addAll(items)
        notifyDataSetChanged()
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val item = cartItems.removeAt(fromPosition)
        cartItems.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun removeItem(position: Int) {
        cartItems.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduk: ImageView = itemView.findViewById(R.id.imgProduk)
        val tvNamaProduk: TextView = itemView.findViewById(R.id.tvNamaProduk)
        val tvHargaProduk: TextView = itemView.findViewById(R.id.tvHargaProduk)
        val tvJumlahProduk: TextView = itemView.findViewById(R.id.tvJumlahProduk)
        val btAddProduk: ImageButton? = itemView.findViewById(R.id.btAddProduk)
        val btMinusProduk: ImageButton? = itemView.findViewById(R.id.btMinusProduk)
        val tvTotalHarga: TextView? = itemView.findViewById(R.id.tvTotalHarga)

        init {
            itemView?.let{
                btAddProduk?.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onAddProdukClickListener?.onAddProdukClick(position)
                    }
                }
                btMinusProduk?.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onMinusProdukClickListener?.onMinusProdukClick(position)
                    }
                }
            }
        }
    }
}
