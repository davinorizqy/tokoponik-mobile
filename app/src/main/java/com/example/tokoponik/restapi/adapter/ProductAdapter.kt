package com.example.tokoponik.restapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.R
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.AddressAdapter.AddressViewHolder
import com.example.tokoponik.restapi.models.rating.averageResponse
import com.example.tokoponik.restapi.models.rating.countResponse
import com.example.tokoponik.restapi.services.RatingService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter (
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductCallBack) {

    inner class ProductViewHolder(itemView: View, val onClick: (Product) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val pic_product: ImageView = itemView.findViewById(R.id.pic_product)
        private val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        private val tv_price: TextView = itemView.findViewById(R.id.tv_price)
        private val tv_rating: TextView = itemView.findViewById(R.id.tv_rating)
        private val tv_review: TextView = itemView.findViewById(R.id.tv_review)

        private lateinit var callAvarage: Call<averageResponse>
        private lateinit var callCount: Call<countResponse>
        private lateinit var sessionManager: SessionManager
        private lateinit var ratingService: RatingService

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

            // product pic
            Picasso.get().load(product.product_pics[0].path).into(pic_product)

            //product price
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            val formattedPrice = numberFormat.format(product.price).replace("Rp", "Rp. ")
            tv_price.text = formattedPrice

            sessionManager = SessionManager(itemView.context)
            ratingService = ApiClient.getRatingService(sessionManager)

            countReview(product.id)
            averageRating(product.id)
        }

        private fun countReview(product_id: Int) {
            callCount = ratingService.getReviewCount(product_id)
            callCount.enqueue(object : Callback<countResponse> {
                override fun onResponse(call: Call<countResponse>, response: Response<countResponse>) {
                    if (response.isSuccessful) {
                        Log.d("Data Rating", response.body()?.review_count.toString())
                        tv_review.text = response.body()?.review_count.toString() + " Ulasan"
                    } else {
                        Log.d("Not Success", response.toString())
                    }
                }

                override fun onFailure(call: Call<countResponse>, t: Throwable) {
                    Toast.makeText(itemView.context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.d("Error onFailure", t.localizedMessage)
                }
            })
        }

        private fun averageRating(product_id: Int) {
            callAvarage = ratingService.getAverageRating(product_id)
            callAvarage.enqueue(object : Callback<averageResponse> {
                override fun onResponse(call: Call<averageResponse>, response: Response<averageResponse>) {
                    if (response.isSuccessful) {
                        Log.d("Data Rating", response.body()?.average_rating.toString())
                        tv_rating.text = " " + response.body()?.average_rating.toString()
                    } else {
                        Log.d("Not Success", response.toString())
                    }
                }

                override fun onFailure(call: Call<averageResponse>, t: Throwable) {
                    Toast.makeText(itemView.context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.d("Error onFailure", t.localizedMessage)
                }
            })
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