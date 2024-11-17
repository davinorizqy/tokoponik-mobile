package com.example.tokoponik.restapi.adapter

interface CartButtonListener {
    fun onEditClick(position: Int)
    fun onDeleteClick(position: Int)
}