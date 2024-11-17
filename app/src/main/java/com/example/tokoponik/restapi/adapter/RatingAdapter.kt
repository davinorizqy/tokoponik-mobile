package com.example.tokoponik.restapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.R
import com.example.tokoponik.restapi.models.blog.Blog
import com.example.tokoponik.restapi.models.rating.Rating
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class RatingAdapter (
    private val onClick: (Rating) -> Unit
) : ListAdapter<Rating, RatingAdapter.RatingViewHolder>(RatingCallBack) {

    inner class RatingViewHolder (itemView: View, val onClick: (Rating) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val tvUser: TextView = itemView.findViewById(R.id.tv_user)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvRating: TextView = itemView.findViewById(R.id.tv_rating)
        private val tvReview: TextView = itemView.findViewById(R.id.tv_review)

        private var currentRating: Rating? = null

        init {
            itemView.setOnClickListener {
                currentRating?.let { onClick(it) }
            }
        }

        fun bind(rating: Rating) {
            currentRating = rating
            Log.d("Blog Data", rating.toString())

            tvUser.text = rating.user.name
            tvRating.text = " " + rating.rating.toString()
            tvReview.text = rating.comment

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(rating.created_at)
            val formattedDate = outputFormat.format(date)

            tvDate.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return RatingViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val rating = getItem(position)
        holder.bind(rating)
    }
}

object RatingCallBack : DiffUtil.ItemCallback<Rating>() {
    override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem == newItem
    }

}