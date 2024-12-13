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
import com.example.tokoponik.restapi.models.transacion.Detail
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class TransactionDetailAdapter(
    private val onClick: (Detail) -> Unit
) : ListAdapter<Detail, TransactionDetailAdapter.TransactionDetailViewHolder>(TransactionDetailCallBack) {

    inner class TransactionDetailViewHolder(itemView: View, val onClick: (Detail) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val picProduct: ImageView = itemView.findViewById(R.id.pic_product)
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        private val tvQty: TextView = itemView.findViewById(R.id.tv_qty)

        private var currentDetail: Detail? = null

        init {
            itemView.setOnClickListener {
                currentDetail?.let { onClick(it) }
            }
        }

        fun bind(detail: Detail) {
            currentDetail = detail
            Log.d("Details Data", detail.toString())

            Picasso.get().load(detail.product.product_pics[0].path).into(picProduct)
            tvName.text = detail.product.name

            val total = detail.product.price
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            val formattedPrice = numberFormat.format(total).replace("Rp", "Rp. ")
            tvPrice.text = formattedPrice

            tvQty.text = "x" + detail.qty.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_product, parent, false)
        return TransactionDetailViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TransactionDetailViewHolder, position: Int) {
        val detail = getItem(position)
        holder.bind(detail)
    }
}

object TransactionDetailCallBack : DiffUtil.ItemCallback<Detail>() {
    override fun areItemsTheSame(oldItem: Detail, newItem: Detail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Detail, newItem: Detail): Boolean {
        return oldItem == newItem
    }
}