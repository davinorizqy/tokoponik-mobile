package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import com.example.tokoponik.restapi.models.cart.totalResponse
import com.example.tokoponik.restapi.services.CartService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class Cart : AppCompatActivity(), CartButtonListener {

    private lateinit var imgbtn_home: ImageButton
    private lateinit var btn_checkout: Button
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var tvTotal: TextView

    private lateinit var callGet: Call<getResponse>
    private lateinit var callCud: Call<cudResponse>
    private lateinit var callTotal: Call<totalResponse>

    private lateinit var cartAdapter: CartAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var cartService: CartService

    private var totalAmount: Int = 0

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
            intent.putExtra("totalAmount", totalAmount)
            startActivity(intent)
        }

        tvTotal = findViewById(R.id.tv_total)

        sessionManager = SessionManager(this)
        cartService = ApiClient.getCartService(sessionManager)

        getUserCart()
        getCartTotal()
    }

    private fun cartOnClick(cart: Cart) {
        Toast.makeText(applicationContext, cart.product.name, Toast.LENGTH_SHORT).show()
    }

//    override fun onEditClick(position: Int) {
//        TODO("Not yet implemented")
//    }

    override fun onDeleteClick(position: Int) {
        val cart = cartAdapter.currentList[position]

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Kamu yakin ingin menghapus produk ini?")
            .setPositiveButton("Delete") { dialog, _ ->
                destroyCartItem(cart.id)
                Toast.makeText(applicationContext, "Deleting product...", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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

    private fun getCartTotal() {
        callTotal = cartService.getCartTotal()
        callTotal.enqueue(object : Callback<totalResponse> {
            override fun onResponse(
                call: Call<totalResponse>,
                response: Response<totalResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Total", response.body()?.data.toString())
                    val total = response.body()?.data

                    if (total != null) {
                        totalAmount = total
                    }

                    val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                    numberFormat.maximumFractionDigits = 2
                    numberFormat.minimumFractionDigits = 2
                    val formattedPrice = numberFormat.format(total).replace("Rp", "Rp. ")

                    tvTotal.text = formattedPrice
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<totalResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun destroyCartItem(id: Int) {
        callCud = cartService.destroyCartItem(id)
        callCud.enqueue(object : Callback<cudResponse> {
            override fun onResponse(
                call: Call<cudResponse>,
                response: Response<cudResponse>
            ) {
                Toast.makeText(applicationContext, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
                getUserCart()
                getCartTotal()
            }

            override fun onFailure(call: Call<cudResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}