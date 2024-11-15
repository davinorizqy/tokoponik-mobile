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
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.product.productResponse
import com.example.tokoponik.restapi.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetail : AppCompatActivity() {

    private lateinit var imgbtnBackToProduct: ImageButton
    private lateinit var imgbtnToCart: ImageButton
    private lateinit var btnToReview: Button

    private lateinit var callGet: Call<productResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var productService: ProductService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgbtnBackToProduct = findViewById(R.id.imgbtn_to_product)
        imgbtnBackToProduct.setOnClickListener {
            val intent = Intent(this, ViewProduct::class.java)
            startActivity(intent)
        }

        imgbtnToCart = findViewById(R.id.imgbtn_to_cart)
        imgbtnToCart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        btnToReview = findViewById(R.id.btn_to_review)
        btnToReview.setOnClickListener {
            val intent = Intent(this, ViewReview::class.java)
            startActivity(intent)
        }

        val productId = intent.getIntExtra("product_id", -1)

        sessionManager = SessionManager(this)
        productService = ApiClient.getProductService(sessionManager)

        if (productId != -1) {
            Log.d("Product Id", productId.toString())
            getProductDetail(productId)
        } else {
            Log.e("ProductDetail", "Invalid Product ID")
        }
    }

    private fun getProductDetail(productId: Int) {
        callGet = productService.getProductById(productId)
        callGet.enqueue(object : Callback<productResponse> {
            override fun onResponse(
                call: Call<productResponse>,
                response: Response<productResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Product", response.body()?.data.toString())
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<productResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}