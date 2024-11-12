package com.example.tokoponik

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.adapter.AddressAdapter
import com.example.tokoponik.restapi.adapter.AddressButtonListener
import com.example.tokoponik.restapi.models.address.Address
import com.example.tokoponik.restapi.models.address.cudResponse
import com.example.tokoponik.restapi.models.address.getResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAddress : AppCompatActivity(), AddressButtonListener {

    private lateinit var btnBackToProfileFragment: Button
    private lateinit var btnAddAddress: Button
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var callGet: Call<getResponse>
    private lateinit var callCud: Call<cudResponse>
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_address)

        refreshLayout = findViewById(R.id.refreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
        addressAdapter = AddressAdapter(
            listener = this,
            onClick = { address -> addressOnClick(address) }
        )
        recyclerView.adapter = addressAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBackToProfileFragment = findViewById(R.id.btn_back_to_profile)
        btnAddAddress = findViewById(R.id.fab_add_address)

        btnBackToProfileFragment.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("showProfileFragment", true)
            startActivity(intent)
            finish()
        }
        btnAddAddress.setOnClickListener {
            val intent = Intent(this, AddAddressForm::class.java)
            startActivity(intent)
        }

        getuserAddress(1) // nanti di ganti pake id sesuai id user yang login
    }

    private fun addressOnClick(address: Address) {
        Toast.makeText(applicationContext, address.address, Toast.LENGTH_SHORT).show()
    }

    override fun onEditClick(position: Int) {
        val address = addressAdapter.currentList[position]

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, EditAddressForm::class.java)
            intent.putExtra("address_data", address) // Pass address object
            startActivity(intent)
            finish()
        }, 3000)
    }

    override fun onDeleteClick(position: Int) {
        val address = addressAdapter.currentList[position]

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Kamu yakin ingin menghapus alamat ini?")
            .setPositiveButton("Delete") { dialog, _ ->
                destroyAddress(address.id)
                Toast.makeText(applicationContext, "Deleting address...", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getuserAddress(user_id: Int) {
        refreshLayout.isRefreshing = true

        callGet = ApiClient.addressService.getUserAddress(user_id)
        callGet.enqueue(object : Callback<getResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<getResponse>,
                response: Response<getResponse>
            ) {
                refreshLayout.isRefreshing = false
                if (response.isSuccessful) {
//                    Log.d("Data Address", response.body()?.data.toString())
                    addressAdapter.submitList(response.body()?.data)
                    addressAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<getResponse>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }

        })
    }

    private fun destroyAddress(id: Int) {
        refreshLayout.isRefreshing = true

        callCud = ApiClient.addressService.destroyAddress(id)

        callCud.enqueue(object : Callback<cudResponse> {
            override fun onResponse(call: Call<cudResponse>, response: Response<cudResponse>) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, "Alamat berhasil di hapus", Toast.LENGTH_SHORT).show()
                getuserAddress(1) // nanti di ganti pake id sesuai id user yang login
            }

            override fun onFailure(call: Call<cudResponse>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}