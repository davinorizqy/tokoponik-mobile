package com.example.tokoponik.restapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.restapi.models.address.Address
import com.example.tokoponik.R

class AddressAdapter(
    private val listener: AddressButtonListener,
    private val onClick: (Address) -> Unit
) : ListAdapter<Address, AddressAdapter.AddressViewHolder>(AddressCallBack) {

    inner class AddressViewHolder(itemView: View, val onClick: (Address) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val tv_receivername: TextView = itemView.findViewById(R.id.tv_receivername)
        private val tv_address: TextView = itemView.findViewById(R.id.tv_address)
        private val tv_detail: TextView = itemView.findViewById(R.id.tv_detail)
        private val tv_postcode: TextView = itemView.findViewById(R.id.tv_postcode)
        private val tv_note: TextView = itemView.findViewById(R.id.tv_note)

        val editButton: ImageButton = itemView.findViewById(R.id.imgbtn_edit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.imgbtn_delete)

        private var currentAddress: Address? = null

        init {
            itemView.setOnClickListener {
                currentAddress?.let { onClick(it) }
            }

            editButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onEditClick(adapterPosition)
                }
            }
            deleteButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(adapterPosition)
                }
            }
        }

        fun bind(address: Address) {
            currentAddress = address
            tv_receivername.text = address.receiver_name
            tv_address.text = address.address
            tv_detail.text = "${address.subdistrict}, ${address.district}, ${address.province}"
            tv_postcode.text = address.post_code
            tv_note.text = address.note
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }
}

object AddressCallBack : DiffUtil.ItemCallback<Address>() {
    override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem.id == newItem.id // Use unique identifier if available
    }

    override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem == newItem
    }
}

