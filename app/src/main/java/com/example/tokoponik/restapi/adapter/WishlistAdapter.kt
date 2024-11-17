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
import com.example.tokoponik.R
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.rating.averageResponse
import com.example.tokoponik.restapi.models.rating.countResponse
import com.example.tokoponik.restapi.models.wishlist.Wishlist
import com.example.tokoponik.restapi.services.RatingService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class WishlistAdapter (
    private val listener: WishlistButtonListener,
    private val onClick: (Wishlist) -> Unit
) : ListAdapter<Wishlist, WishlistAdapter.WishlistViewHolder>(WishlistCallBack) {

    inner class WishlistViewHolder(itemView: View, val onClick: (Wishlist) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val pic_product: ImageView = itemView.findViewById(R.id.pic_product)
        private val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        private val tv_price: TextView = itemView.findViewById(R.id.tv_price)
        private val tv_rating: TextView = itemView.findViewById(R.id.tv_rating)
        private val tv_review: TextView = itemView.findViewById(R.id.tv_review)

        val deleteButton: ImageButton = itemView.findViewById(R.id.imgbtn_delete)

        private lateinit var callAvarage: Call<averageResponse>
        private lateinit var callCount: Call<countResponse>
        private lateinit var sessionManager: SessionManager
        private lateinit var ratingService: RatingService

        private var currentWishlist: Wishlist? = null

        init {
            itemView.setOnClickListener {
                currentWishlist?.let { onClick(it) }
            }

            deleteButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(adapterPosition)
                }
            }
        }

        fun bind(wishlist: Wishlist) {
            currentWishlist = wishlist
            Log.d("Products Data", wishlist.toString())

            tv_name.text = wishlist.product.name

            // product pic
            Picasso.get().load(wishlist.product.product_pics[0].path).into(pic_product)

            //product price
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(wishlist.product.price)
            tv_price.text = formattedPrice

            sessionManager = SessionManager(itemView.context)
            ratingService = ApiClient.getRatingService(sessionManager)

            countReview(wishlist.product_id)
            averageRating(wishlist.product_id)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist, parent, false)
        return WishlistViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val wishlist = getItem(position)
        holder.bind(wishlist)
    }
}

object WishlistCallBack : DiffUtil.ItemCallback<Wishlist>() {
    override fun areItemsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Wishlist, newItem: Wishlist): Boolean {
        return oldItem == newItem
    }

}