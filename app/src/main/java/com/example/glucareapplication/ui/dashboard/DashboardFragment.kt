package com.example.glucareapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.glucareapplication.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            tvName.text = "Hi, " + auth.currentUser?.displayName
            tvGreeting.text = "How are you feeling today?"
        }
        Glide.with(requireActivity())
            .load(auth.currentUser?.photoUrl)
            .circleCrop()
            .into(binding.ivUser)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}