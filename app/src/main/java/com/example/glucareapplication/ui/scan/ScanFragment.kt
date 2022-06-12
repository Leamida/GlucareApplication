package com.example.glucareapplication.ui.scan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

import com.example.glucareapplication.databinding.FragmentScanBinding
import com.example.glucareapplication.feature.auth.data.source.local.preferences.UserPreferences
import com.example.glucareapplication.ui.scan.camera.CameraActivity
import com.example.glucareapplication.ui.scan.camera.PreviewImageActivity
import com.example.glucareapplication.ui.user.profile.ProfileActivity

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnScan.setOnClickListener{
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}