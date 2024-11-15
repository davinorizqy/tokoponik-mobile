package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tokoponik.helper.SessionManager
import com.example.tokoponik.restapi.ApiClient
import com.example.tokoponik.restapi.models.user.User
import com.example.tokoponik.restapi.models.user.logoutResponse
import com.example.tokoponik.restapi.models.user.userResponse
import com.example.tokoponik.restapi.services.AuthService
import com.example.tokoponik.restapi.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile : AppCompatActivity() {

    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var imgbtnBackToProfile: ImageButton
    private lateinit var etName: EditText
    private lateinit var etPhonenumber: EditText

    private var user: User? = null
    private lateinit var session: SessionManager
    private lateinit var call: Call<userResponse>
    private lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCancel = findViewById(R.id.btn_batal)
        btnSave = findViewById(R.id.btn_simpan)
        imgbtnBackToProfile = findViewById(R.id.imgbtn_backtoprofile)
        etName = findViewById(R.id.et_name)
        etPhonenumber = findViewById(R.id.et_phonenumber)

        user = intent.getParcelableExtra("user")

        if (user != null) {
            etName.setText(user?.name)
            etPhonenumber.setText(user?.phone_number)
        } else {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
        }

        btnSave.setOnClickListener {
            if (user != null) {
                val userId = user?.id
                if (userId != null) {
                    updateProfile(
                        userId,
                        etName.text.toString(),
                        etPhonenumber.text.toString()
                    )
                } else {
                    Toast.makeText(applicationContext, "ID tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "User data tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("showProfileFragment", true)
            startActivity(intent)
            finish()
        }

        imgbtnBackToProfile.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("showProfileFragment", true)
            startActivity(intent)
            finish()
        }

        session= SessionManager(this)
        userService = ApiClient.getUserService(session)
    }

    private fun updateProfile(id: Int, name: String, phone_number: String) {
        call = userService.updateUserProfile(id, name, phone_number)
        call.enqueue(object : Callback<userResponse> {
            override fun onResponse(call: Call<userResponse>, response: Response<userResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data User", response.body()?.data.toString())

                    Toast.makeText(applicationContext, "Profile berhasil diubah.", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@EditProfile, ProfileFragment::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        intent.putExtra("showProfileFragment", true)
                        startActivity(intent)
                        finish()
                    }, 1000)
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }
}