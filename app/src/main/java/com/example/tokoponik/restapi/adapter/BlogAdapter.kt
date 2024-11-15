package com.example.tokoponik.restapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.R
import com.example.tokoponik.restapi.models.blog.Blog
import com.squareup.picasso.Picasso

class BlogAdapter (
    private val onClick: (Blog) -> Unit
) : ListAdapter<Blog, BlogAdapter.BlogViewHolder>(BlogCallBack) {

    inner class BlogViewHolder (itemView: View, val onClick: (Blog) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val pic_blog: ImageView = itemView.findViewById(R.id.pic_blog)
        private val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        private val tv_desc: TextView = itemView.findViewById(R.id.tv_desc)
        private val tv_date: TextView = itemView.findViewById(R.id.tv_date)

        private var currentBlog: Blog? = null

        init {
            itemView.setOnClickListener {
                currentBlog?.let { onClick(it) }
            }
        }

        fun bind(blog: Blog) {
            currentBlog = blog
            Log.d("Blog Data", blog.toString())

            Picasso.get().load(blog.blog_pics[0].pic_path).into(pic_blog)

            tv_title.text = blog.title
            tv_desc.text = blog.description
            tv_date.text = blog.created_at
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog, parent, false)
        return BlogViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = getItem(position)
        holder.bind(blog)
    }
}

object BlogCallBack : DiffUtil.ItemCallback<Blog>() {
    override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem == newItem
    }

}
