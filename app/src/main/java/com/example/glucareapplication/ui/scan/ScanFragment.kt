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
    private lateinit var userPreferences: UserPreferences
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        userPreferences = UserPreferences(requireActivity().applicationContext)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences.getUser().asLiveData().observe(viewLifecycleOwner){user->
            if (user[0]=="doctor" || user[0]=="Doctor"){
                binding.apply {
                    tvAsk.text = "scan glucose x-ray document and \nshare the result with \nyour patient"
                    tvAnswer.visibility = View.GONE
                }
            }
        }
        binding.apply {
            btnScan.setOnClickListener{
                startActivity(Intent(activity, PreviewImageActivity::class.java))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}