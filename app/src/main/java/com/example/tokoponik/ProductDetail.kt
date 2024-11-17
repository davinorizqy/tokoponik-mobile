package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.restapi.models.product.productResponse
import com.example.tokoponik.restapi.services.ProductService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class ProductDetail : AppCompatActivity() {

    private lateinit var imgbtnBackToProduct: ImageButton
    private lateinit var imgbtnToCart: ImageButton
    private lateinit var btnToReview: Button

    private lateinit var picProduct: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvRating1: TextView
    private lateinit var tvRating2: TextView
    private lateinit var tvReview1: TextView
    private lateinit var tvReview2: TextView
    private lateinit var tvDesc: TextView
    private lateinit var reviewRecycleView: RecyclerView

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

        val product: Product? = intent.getParcelableExtra("product")

        picProduct = findViewById(R.id.pic_product)
        tvName = findViewById(R.id.tv_name)
        tvPrice = findViewById(R.id.tv_price)
        tvRating1 = findViewById(R.id.tv_rating1)
        tvRating2 = findViewById(R.id.tv_rating2)
        tvReview1 = findViewById(R.id.tv_review1)
        tvReview2 = findViewById(R.id.tv_review2)
        tvDesc = findViewById(R.id.tv_desc)
        reviewRecycleView = findViewById(R.id.reviewRecyclerView)

        if (product != null) {
            Log.d("Product", product.toString())

            Picasso.get().load(product.product_pics[0].path).into(picProduct)

            tvName.text = product.name
            tvRating1.text = product.average_rating.toString()
            tvRating2.text = product.average_rating.toString()
            tvReview1.text = "88"
            tvReview2.text = "88"
            tvDesc.text = product.description

            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(product.price)
            tvPrice.text = formattedPrice
        } else {
            Log.e("ProductDetail", "Invalid Product ID")
        }
    }
}