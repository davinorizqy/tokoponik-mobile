package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

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