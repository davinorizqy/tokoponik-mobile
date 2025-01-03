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
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var user: User? = null

    //variabel button
    private lateinit var btnToEditAccount: Button
    private lateinit var btnToEditProfile: Button
    private lateinit var imgbtnLogout: ImageButton
    private lateinit var btnToAddress: Button

    private lateinit var  tvUsername: TextView
    private lateinit var  tvName: TextView
    private lateinit var  tvPhonenumber: TextView

    private lateinit var session: SessionManager
    private lateinit var callUser: Call<userResponse>
    private lateinit var callLogout: Call<logoutResponse>
    private lateinit var userService: UserService
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(requireContext())
        authService = ApiClient.getAuthService(session)
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    // function intent
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh penggunaan tombol untuk membuka Activity
        btnToEditAccount = view.findViewById(R.id.btn_to_editaccount)
        btnToEditProfile = view.findViewById(R.id.btn_to_editprofile)
        imgbtnLogout = view.findViewById(R.id.imgbtn_logout)
        btnToAddress = view.findViewById(R.id.btn_to_address)

        tvUsername = view.findViewById(R.id.tv_username)
        tvName = view.findViewById(R.id.tv_name)
        tvPhonenumber = view.findViewById(R.id.tv_phonenumber)

        btnToEditAccount.setOnClickListener {
            val intent = Intent(activity, EditAccount::class.java)
            startActivity(intent)
        }
        btnToEditProfile.setOnClickListener {
            Log.d("User Data", user.toString())
            if (user != null) {
                val intent = Intent(activity, EditProfile::class.java)

                intent.putExtra("user", user)

                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "User info not available", Toast.LENGTH_SHORT).show()
            }
        }
        imgbtnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Kamu yakin ingin logout dari akun ini?")
                .setPositiveButton("Delete") { dialog, _ ->
                    logout()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        btnToAddress.setOnClickListener {
            val intent = Intent(activity, ViewAddress::class.java)
            startActivity(intent)
        }

        session= SessionManager(requireContext())
        userService = ApiClient.getUserService(session)

        getUserInfo()
    }

    private fun getUserInfo() {
        callUser = userService.getUserInfo()

        callUser.enqueue(object : Callback<userResponse> {
            override fun onResponse(call: Call<userResponse>, response: Response<userResponse>) {
                if (response.isSuccessful) {
                    Log.d("Data Product", response.body()?.data.toString())
                    user = response.body()?.data
                    if (user != null) {
                        tvUsername.text = user?.username
                        tvName.text = user?.name
                        val formattedPhoneNumber = formatPhoneNumber(user?.phone_number ?: "")
                        tvPhonenumber.text = formattedPhoneNumber
                    }
                } else {
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error onFailure", t.localizedMessage)
            }
        })
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith("0")) {
            "+62 ${phoneNumber.substring(1)}"
        } else {
            phoneNumber // In case the phone number is already formatted or valid
        }
    }

    private fun logout() {
        callLogout = authService.logout()

        callLogout.enqueue(object : Callback<logoutResponse> {
            override fun onResponse(
                call: Call<logoutResponse>,
                response: Response<logoutResponse>
            ) {
                if (response.isSuccessful) {
                    session.clearAuthToken()

                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 1000)
                } else {
                    Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
                    Log.d("Not Success", response.toString())
                }
            }

            override fun onFailure(call: Call<logoutResponse>, t: Throwable) {
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}