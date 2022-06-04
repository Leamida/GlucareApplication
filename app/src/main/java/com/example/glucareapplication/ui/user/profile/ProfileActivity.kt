package com.example.glucareapplication.ui.user.profile;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle
import com.bumptech.glide.Glide

import com.example.glucareapplication.R;
import com.example.glucareapplication.databinding.ActivityProfileBinding;
import com.example.glucareapplication.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()


        binding.apply {
            etName.setText(auth.currentUser?.displayName)
            etEmail.setText(auth.currentUser?.email)
            tvLastUpdated.text = "Last update on 15/5/2022"
        }

        Glide.with(this)
            .load(auth.currentUser?.photoUrl)
            .circleCrop()
            .into(binding.ivUser)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}