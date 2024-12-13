package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.TransactionAdapter
import com.example.tokoponik.restapi.adapter.TransactionButtonListener
import com.example.tokoponik.restapi.models.transacion.Transaction
import com.example.tokoponik.restapi.models.transacion.transactionResponse
import com.example.tokoponik.restapi.services.TransactionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFragment : Fragment(), TransactionButtonListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var callTransaction : Call<transactionResponse>
    private lateinit var sessionManager: SessionManager
    private lateinit var transactionService: TransactionService
    private lateinit var transactionAdapter: TransactionAdapter

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
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    // function intent
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Contoh penggunaan tombol untuk membuka Activity
        imgbtnCart = view.findViewById(R.id.imgbtn_cart)
        imgbtnCart.setOnClickListener {
            val intent = Intent(activity, Cart::class.java)
            startActivity(intent)
        }

        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView)
        transactionAdapter = TransactionAdapter(
            listener = this,
            onClick = { transaction: Transaction -> transactionOnClick(transaction) }
        )
        transactionRecyclerView.adapter = transactionAdapter
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        sessionManager = SessionManager(requireContext())
        transactionService = ApiClient.getTransactionService(sessionManager)

        getTransaction()
    }

    private fun transactionOnClick(transaction: Transaction) {
//        val intent = Intent(activity, TransactionDetail::class.java)
//        intent.putExtra("data", transaction)
//        startActivity(intent)
    }

    private fun getTransaction() {
        callTransaction = transactionService.getTransaction()
        callTransaction.enqueue(object : Callback<transactionResponse> {
            override fun onResponse(
                call: Call<transactionResponse>,
                response: Response<transactionResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("Data Product", response.body()?.data.toString())
                    transactionAdapter.submitList(response.body()?.data)
                    transactionAdapter.notifyDataSetChanged()
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<transactionResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    override fun onDetailCLick(position: Int) {
        val transaction = transactionAdapter.currentList[position]
        val intent = Intent(activity, TransactionDetail::class.java)
        intent.putExtra("transaction", transaction)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}