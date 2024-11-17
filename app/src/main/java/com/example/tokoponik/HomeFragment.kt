package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.BlogAdapter
import com.example.tokoponik.restapi.adapter.ProductAdapter
import com.example.tokoponik.restapi.models.blog.Blog
import com.example.tokoponik.restapi.models.blog.blogResponse
import com.example.tokoponik.restapi.models.product.Product
import com.example.tokoponik.restapi.models.product.productsResponse
import com.example.tokoponik.restapi.services.BlogService
import com.example.tokoponik.restapi.services.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //variabel button
    private lateinit var imgbtnCart: ImageButton
    private lateinit var tv_see_all_product: TextView
    private lateinit var tv_see_all_blog: TextView
    private lateinit var imgbtnVegetable: ImageButton
    private lateinit var imgbtnSeed: ImageButton
    private lateinit var imgbtnTools: ImageButton
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var blogRecyclerView: RecyclerView

    private lateinit var callProduct: Call<productsResponse>
    private lateinit var callBlog: Call<blogResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var productService: ProductService
    private lateinit var blogService: BlogService
    private lateinit var productAdapter: ProductAdapter
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // function intent
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productRecyclerView = view.findViewById(R.id.productRecyclerView)
        productAdapter = ProductAdapter { product: Product -> productOnClick(product) }
        productRecyclerView.adapter = productAdapter
        productRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        blogRecyclerView = view.findViewById(R.id.blogRecyclerView)
        blogAdapter = BlogAdapter { blog: Blog -> blogOnClick(blog) }
        blogRecyclerView.adapter = blogAdapter
        blogRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Contoh penggunaan tombol untuk membuka Activity
        imgbtnCart = view.findViewById(R.id.imgbtn_to_cart)
        imgbtnCart.setOnClickListener {
            val intent = Intent(activity, Cart::class.java)
            startActivity(intent)
        }

        tv_see_all_product = view.findViewById(R.id.tv_see_all_product)
        tv_see_all_product.setOnClickListener {
            val intent = Intent(activity, ViewProduct::class.java)
            startActivity(intent)
        }

        tv_see_all_blog = view.findViewById(R.id.tv_see_all_blog)
        tv_see_all_blog.setOnClickListener {
            val intent = Intent(activity, ViewBlog::class.java)
            startActivity(intent)
        }

        imgbtnVegetable = view.findViewById(R.id.imgbtn_vegetable_category)
        imgbtnVegetable.setOnClickListener {
            val intent = Intent(activity, ViewProductCategory::class.java)
            startActivity(intent)
        }

        imgbtnSeed = view.findViewById(R.id.imgbtn_seed_category)
        imgbtnSeed.setOnClickListener {
            val intent = Intent(activity, ViewProductCategory::class.java)
            startActivity(intent)
        }

        imgbtnTools = view.findViewById(R.id.imgbtn_tools_category)
        imgbtnTools.setOnClickListener {
            val intent = Intent(activity, ViewProductCategory::class.java)
            startActivity(intent)
        }

        sessionManager = SessionManager(requireContext())
        productService = ApiClient.getProductService(sessionManager)
        blogService = ApiClient.getBlogService(sessionManager)

        getProducts(6)
        getBlogs()
    }

    private fun productOnClick(product: Product) {
        val intent = Intent(activity, ProductDetail::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    private fun getProducts (limit: Int) {
        callProduct = productService.getProductLimit(limit)
        callProduct.enqueue(object : Callback<productsResponse> {
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
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun blogOnClick(blog: Blog) {
        val intent = Intent(activity, BlogDetail::class.java)
        intent.putExtra("blog", blog)
        startActivity(intent)
    }

    private fun getBlogs () {
        callBlog = blogService.getAllBlogs()
        callBlog.enqueue(object : Callback<blogResponse> {
            override fun onResponse(
                call: Call<blogResponse>,
                response: Response<blogResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Blog", response.body()?.data.toString())
                    blogAdapter.submitList(response.body()?.data)
                    blogAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<blogResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}