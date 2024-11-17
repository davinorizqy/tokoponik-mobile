package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.CartAdapter
import com.example.tokoponik.restapi.adapter.CartButtonListener
import com.example.tokoponik.restapi.models.cart.Cart
import com.example.tokoponik.restapi.models.cart.cudResponse
import com.example.tokoponik.restapi.models.cart.getResponse
import com.example.tokoponik.restapi.services.CartService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cart : AppCompatActivity(), CartButtonListener {

    private lateinit var imgbtn_home: ImageButton
    private lateinit var btn_checkout: Button
    private lateinit var cartRecyclerView: RecyclerView

    private lateinit var callGet: Call<getResponse>
    private lateinit var callCud: Call<cudResponse>

    private lateinit var cartAdapter: CartAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var cartService: CartService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        cartAdapter = CartAdapter(
            listener = this,
            onClick = { cart -> cartOnClick(cart) }
        )
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        imgbtn_home = findViewById(R.id.imgbtn_to_home)
        imgbtn_home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        btn_checkout = findViewById(R.id.btn_to_checkout)
        btn_checkout.setOnClickListener {
            val intent = Intent(this, Checkout::class.java)
            startActivity(intent)
        }

        sessionManager = SessionManager(this)
        cartService = ApiClient.getCartService(sessionManager)

        getUserCart()
    }

    private fun cartOnClick(cart: Cart) {
        Toast.makeText(applicationContext, cart.product.name, Toast.LENGTH_SHORT).show()
    }

    override fun onEditClick(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(position: Int) {
        TODO("Not yet implemented")
    }

    private fun getUserCart() {
        callGet = cartService.getUserCart()
        callGet.enqueue(object : Callback<getResponse> {
            override fun onResponse(
                call: Call<getResponse>,
                response: Response<getResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Cart", response.body()?.data.toString())
                    cartAdapter.submitList(response.body()?.data)
                    cartAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<getResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}