package com.example.tokoponik.restapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.R
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.models.cart.Cart
import com.example.tokoponik.restapi.models.cart.getResponse
import com.example.tokoponik.restapi.services.RatingService
import com.squareup.picasso.Picasso
import retrofit2.Call
import java.text.NumberFormat
import java.util.Locale

class CartAdapter (
    private val listener: CartButtonListener,
    private val onClick: (Cart) -> Unit
) : ListAdapter<Cart, CartAdapter.CartViewHolder>(CartCallBack) {
    inner class CartViewHolder(itemView: View, val onClick: (Cart) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val pic_cart: ImageView = itemView.findViewById(R.id.pic_product)
        private val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        private val tv_price: TextView = itemView.findViewById(R.id.tv_price)
        private val tv_qty: TextView = itemView.findViewById(R.id.tv_qty)

//        val editButton: ImageButton = itemView.findViewById(R.id.imgbtn_edit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.imgbtn_delete)

        private var currentCart: Cart? = null

        init {
            itemView.setOnClickListener {
                currentCart?.let { onClick(it) }
            }

//            editButton.setOnClickListener {
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    listener.onEditClick(adapterPosition)
//                }
//            }

            deleteButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(adapterPosition)
                }
            }
        }

        fun bind(cart: Cart) {
            currentCart = cart
            Log.d("Carts Data", cart.toString())

            tv_name.text = cart.product.name
            tv_qty.text = cart.qty.toString()

            // cart pic
            Picasso.get().load(cart.product.product_pics[0].path).into(pic_cart)

            //cart price
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            val formattedPrice = numberFormat.format(cart.product.price).replace("Rp", "Rp. ")
            tv_price.text = formattedPrice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = getItem(position)
        holder.bind(cart)
    }
}

object CartCallBack : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }
}