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
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.R
import com.example.tokoponik.restapi.adapter.AddressAdapter.AddressViewHolder
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter (
//    private val listener: ProductButtonListener,
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductCallBack) {

    inner class ProductViewHolder(itemView: View, val onClick: (Product) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val pic_product: ImageView = itemView.findViewById(R.id.pic_product)
        private val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        private val tv_price: TextView = itemView.findViewById(R.id.tv_price)
        private val tv_rating: TextView = itemView.findViewById(R.id.tv_rating)
        private val tv_review: TextView = itemView.findViewById(R.id.tv_review)
        private val btn_wishlist: ImageButton = itemView.findViewById(R.id.btn_wishlist)

        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener {
                currentProduct?.let { onClick(it) }
            }
        }

        fun bind(product: Product) {
            currentProduct = product
            Log.d("Products Data", product.toString())

            tv_name.text = product.name
            tv_rating.text = product.average_rating.toString()
            tv_review.text = "Review"

            // product pic
            Picasso.get().load(product.product_pics[0].path).into(pic_product)

            //product price
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(product.price)
            tv_price.text = formattedPrice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

object ProductCallBack : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}