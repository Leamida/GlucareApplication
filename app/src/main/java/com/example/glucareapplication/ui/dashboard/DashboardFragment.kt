package com.example.glucareapplication.ui.dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.databinding.FragmentDashboardBinding
import com.example.glucareapplication.feature.auth.data.source.local.preferences.UserPreferences
import com.example.glucareapplication.ui.dashboard.adapter.HistoriesAdapter
import com.example.glucareapplication.ui.scan.ScanViewModel
import com.example.glucareapplication.ui.scan.camera.CameraActivity
import com.example.glucareapplication.ui.scan.camera.PreviewImageActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var userPreferences: UserPreferences
    private val dashboardViewModel: DashboardViewModel by viewModels()

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
        userPreferences = UserPreferences(requireActivity().applicationContext)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGlucoseHistory.layoutManager = LinearLayoutManager(context)


        binding.apply {
            tvName.text = "Hi, " + auth.currentUser?.displayName
            tvGreeting.text = "How are you feeling today?"
            btnScan.setOnClickListener{
//                startActivity(Intent(activity, PreviewImageActivity::class.java))
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivity(intent)
            }
        }
        userPreferences.getToken().asLiveData().observe(viewLifecycleOwner) {
            dashboardViewModel.getHistories("Bearer $it").observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.apply {
                            pbGlucoseHistories.visibility = View.VISIBLE
                            rvContainer.visibility = View.INVISIBLE
                        }
                    }
                    is Result.Success -> {
                        binding.apply {
                            pbGlucoseHistories.visibility = View.INVISIBLE
                            rvContainer.visibility = View.VISIBLE
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            userPreferences.setUser(result.data.user)
                        }
                        if (result.data.predictResults.isEmpty()) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.rvGlucoseHistory.adapter =
                                HistoriesAdapter(result.data.predictResults)
                        }
                    }
                    is Result.Error -> {
                        binding.pbGlucoseHistories.visibility = View.INVISIBLE
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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