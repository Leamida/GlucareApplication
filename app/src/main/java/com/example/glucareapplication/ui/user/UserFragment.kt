package com.example.glucareapplication.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.glucareapplication.databinding.FragmentUserBinding
import com.example.glucareapplication.ui.auth.AuthActivity
import com.example.glucareapplication.ui.user.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private lateinit var auth: FirebaseAuth
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLogout.setOnClickListener {
                Firebase.auth.signOut()
                startActivity(Intent(activity, AuthActivity::class.java))
                activity?.finish()
            }
            btnProfileSettings.setOnClickListener {
                startActivity(Intent(activity, ProfileActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}