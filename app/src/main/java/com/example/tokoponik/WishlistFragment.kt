package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.WishlistAdapter
import com.example.tokoponik.restapi.adapter.WishlistButtonListener
import com.example.tokoponik.restapi.models.wishlist.Wishlist
import com.example.tokoponik.restapi.models.wishlist.cdResponse
import com.example.tokoponik.restapi.models.wishlist.getResponse
import com.example.tokoponik.restapi.services.WishlistService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WishlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishlistFragment : Fragment(), WishlistButtonListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var wishlistRecyclerView: RecyclerView

    private lateinit var callGet : Call<getResponse>
    private lateinit var callCud : Call<cdResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var wishlistService: WishlistService
    private lateinit var wishlistAdapter: WishlistAdapter


    //variabel button
    private lateinit var imgbtnCart: ImageButton

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
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
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

        wishlistRecyclerView = view.findViewById(R.id.wishlistRecyclerView)
        wishlistAdapter = WishlistAdapter(
            listener = this,
            onClick = { wishlist: Wishlist -> wishlistOnClick(wishlist) }
        )
        wishlistRecyclerView.adapter = wishlistAdapter
        wishlistRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        sessionManager = SessionManager(requireContext())
        wishlistService = ApiClient.getWishlistService(sessionManager)

        getWishlist()
    }

    private fun wishlistOnClick(wishlist: Wishlist) {
        val intent = Intent(activity, ProductDetail::class.java)
        intent.putExtra("product", wishlist.product)
        startActivity(intent)
    }

    override fun onDeleteClick(position: Int) {
        val wishlist = wishlistAdapter.currentList[position]

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Hapus")
            .setMessage("Kamu yakin ingin menghapus produk ini dari wishlist?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteWishlist(wishlist.id)
                Toast.makeText(requireContext(), "Removing product ...", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getWishlist() {
        callGet = wishlistService.getWishlist()
        callGet.enqueue(object : Callback<getResponse> {
            override fun onResponse(
                call: Call<getResponse>,
                response: Response<getResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Product", response.body()?.data.toString())
                    wishlistAdapter.submitList(response.body()?.data)
                    wishlistAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<getResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun deleteWishlist(id: Int) {
        callCud = wishlistService.removeFromWishlist(id)
        callCud.enqueue(object : Callback<cdResponse> {
            override fun onResponse(
                call: Call<cdResponse>,
                response: Response<cdResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Produk berhasil di hapus", Toast.LENGTH_SHORT).show()
                    getWishlist()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<cdResponse>, t: Throwable) {
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
         * @return A new instance of fragment WishlistFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WishlistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}