package com.example.tokoponik

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tokoponik.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showProfileFragment(intent)
        showTransactionFragment(intent)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.transaction -> {
                    replaceFragment(TransactionFragment())
                    true
                }
                R.id.wishlist -> {
                    replaceFragment(WishlistFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    //function buat nampilin ProfileFragment
    fun showProfileFragment(intent: Intent?) {
        intent?.let {
            if (it.getBooleanExtra("showProfileFragment", false)) {
                Log.d("HomeActivity", "Navigating to ProfileFragment")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, ProfileFragment())
                    .commit()
            }
        }
    }

    //function buat nampilin TransactionFragment
    fun showTransactionFragment(intent: Intent?) {
        intent?.let {
            if (it.getBooleanExtra("showTransactionFragment", false)) {
                Log.d("HomeActivity", "Navigating to TransactionFragment")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, TransactionFragment())
                    .commit()
            }
        }
    }
}