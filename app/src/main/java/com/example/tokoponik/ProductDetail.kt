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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.RatingAdapter
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.restapi.models.rating.Rating
import com.example.tokoponik.restapi.models.rating.averageResponse
import com.example.tokoponik.restapi.models.rating.countResponse
import com.example.tokoponik.restapi.models.rating.ratingResponse
import com.example.tokoponik.restapi.services.RatingService
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
    private lateinit var tvType: TextView
    private lateinit var tvDesc: TextView
    private lateinit var ratingRecycleView: RecyclerView

    private lateinit var callRating: Call<ratingResponse>
    private lateinit var callAvarage: Call<averageResponse>
    private lateinit var callCount: Call<countResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var ratingService: RatingService
    private lateinit var ratingAdapter: RatingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ratingRecycleView = findViewById(R.id.ratingRecyclerView)
        ratingAdapter = RatingAdapter { rating: Rating -> ratingOnCLick(rating) }
        ratingRecycleView.adapter = ratingAdapter
        ratingRecycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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

        sessionManager = SessionManager(this)
        ratingService = ApiClient.getRatingService(sessionManager)

        val product: Product? = intent.getParcelableExtra("product")

        picProduct = findViewById(R.id.pic_product)
        tvName = findViewById(R.id.tv_name)
        tvPrice = findViewById(R.id.tv_price)
        tvRating1 = findViewById(R.id.tv_rating1)
        tvRating2 = findViewById(R.id.tv_rating2)
        tvReview1 = findViewById(R.id.tv_review1)
        tvReview2 = findViewById(R.id.tv_review2)
        tvType = findViewById(R.id.tv_type)
        tvDesc = findViewById(R.id.tv_desc)

        if (product != null) {
            Log.d("Product", product.toString())

            Picasso.get().load(product.product_pics[0].path).into(picProduct)

            tvName.text = product.name
            tvType.text = product.type.replaceFirstChar { it.uppercase() }
            tvDesc.text = product.description

            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(product.price)
            tvPrice.text = formattedPrice

            getProductRating(product.id, 5)
            averageRating(product.id)
            countReview(product.id)
        } else {
            Log.e("ProductDetail", "Invalid Product ID")
        }
    }

    private fun ratingOnCLick(rating: Rating) {
        Log.d("Rating", rating.toString())
    }

    private fun getProductRating(product_id: Int, limit: Int) {
//        callRating = ratingService.getProductReviewLimit(product_id, limit)
        callRating = ratingService.getProductReview(product_id)
        callRating.enqueue(object : Callback<ratingResponse> {
            override fun onResponse(
                call: Call<ratingResponse>,
                response: Response<ratingResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Rating", response.body()?.data.toString())
                    ratingAdapter.submitList(response.body()?.data)
                    ratingAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<ratingResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun countReview(product_id: Int) {
        callCount = ratingService.getReviewCount(product_id)
        callCount.enqueue(object : Callback<countResponse> {
            override fun onResponse(call: Call<countResponse>, response: Response<countResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data Rating", response.body()?.review_count.toString())
                    tvReview1.text = response.body()?.review_count.toString() + " Ulasan"
                    tvReview2.text = "(" + response.body()?.review_count.toString() + ")"
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<countResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun averageRating(product_id: Int) {
        callAvarage = ratingService.getAverageRating(product_id)
        callAvarage.enqueue(object : Callback<averageResponse> {
            override fun onResponse(call: Call<averageResponse>, response: Response<averageResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data Rating", response.body()?.average_rating.toString())
                    tvRating1.text = " " + response.body()?.average_rating.toString()
                    tvRating2.text = " " + response.body()?.average_rating.toString()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<averageResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}