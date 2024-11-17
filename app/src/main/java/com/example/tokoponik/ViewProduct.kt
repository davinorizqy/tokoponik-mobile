package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.ProductAdapter
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.restapi.models.product.productsResponse
import com.example.tokoponik.restapi.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewProduct : AppCompatActivity() {

    private lateinit var imgbtn_home: ImageButton
    private lateinit var imgbtn_cart: ImageButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var callGet: Call<productsResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var productService: ProductService
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_product)

        recyclerView = findViewById(R.id.productRecyclerView)
        productAdapter = ProductAdapter { product: Product -> productOnClick(product) }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtn_home = findViewById(R.id.imgbtn_to_home)
        imgbtn_home.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        imgbtn_cart = findViewById(R.id.imgbtn_to_cart)
        imgbtn_cart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        sessionManager = SessionManager(this)
        productService = ApiClient.getProductService(sessionManager)

        getProducts()
    }

    private fun productOnClick(product: Product) {
        val intent = Intent(this, ProductDetail::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    private fun getProducts () {
        callGet = productService.getAllProduct()
        callGet.enqueue(object : Callback<productsResponse> {
            override fun onResponse(
                call: Call<productsResponse>,
                response: Response<productsResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Product", response.body()?.data.toString())
                    productAdapter.submitList(response.body()?.data)
                    productAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<productsResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}