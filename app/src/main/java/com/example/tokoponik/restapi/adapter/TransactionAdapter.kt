package com.example.tokoponik.restapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.R
import com.example.tokoponik.restapi.models.transacion.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter (
    private  val listener: TransactionButtonListener,
    private val onClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionCallBack) {

    inner class TransactionViewHolder(itemView: View, val onClick: (Transaction) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val tv_date: TextView = itemView.findViewById(R.id.tv_date)
        private val tv_status: TextView = itemView.findViewById(R.id.tv_status)
        private val tv_grandtotal: TextView = itemView.findViewById(R.id.tv_grandtotal)

        val detailButton: Button = itemView.findViewById(R.id.btn_detail)

        private var currentTransaction: Transaction? = null

        init {
            itemView.setOnClickListener {
                currentTransaction?.let { onClick(it) }
            }

            detailButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDetailCLick(adapterPosition)
                }
            }
        }

        fun bind(transaction: Transaction) {
            currentTransaction = transaction
            Log.d("Transactions Data", transaction.toString())

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(transaction.created_at)
            tv_date.text = outputFormat.format(date)

            tv_status.text = transaction.status.replaceFirstChar { it.uppercase() }

            val total = transaction.grand_total.toDouble().toInt()
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            val formattedPrice = numberFormat.format(total).replace("Rp", "Rp. ")
            tv_grandtotal.text = formattedPrice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }
}

object TransactionCallBack : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}