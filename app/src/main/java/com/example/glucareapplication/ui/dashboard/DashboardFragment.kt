package com.example.glucareapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.glucareapplication.R
import com.example.glucareapplication.core.line_chart.CustomXAxisFormater
import com.example.glucareapplication.core.line_chart.CustomYValueFormater
import com.example.glucareapplication.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
        setLineChartdata()

        binding.apply {
            tvName.text = "Hi, " + auth.currentUser?.displayName
            tvGreeting.text = "How are you feeling today?"

            lineChart.apply {
                axisRight.isEnabled = false
                description.text = "Glucose History"
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                xAxis.granularity = 1f
                xAxis.isGranularityEnabled = true
                setPinchZoom(true)
            }
        }
        Glide.with(requireActivity())
            .load(auth.currentUser?.photoUrl)
            .circleCrop()
            .into(binding.ivUser)


    }

    private fun setLineChartdata() {

        val xValsDateLabel = ArrayList<String>()

        val xValsOriginalMillis = ArrayList<Long>()
        xValsOriginalMillis.add(1554875423736L)
        xValsOriginalMillis.add(1555275494836L)
        xValsOriginalMillis.add(1585578525900L)
        xValsOriginalMillis.add(1596679626245L)
        xValsOriginalMillis.add(1609990727820L)
        xValsOriginalMillis.add(1709990727820L)

        for (i in xValsOriginalMillis) {
            val mm = i / 60 % 60
            val hh = i / (60 * 60) % 24
            val mDateTime = "$hh:$mm"
            xValsDateLabel.add(mDateTime)
        }



        val lineEntry = ArrayList<Entry>()
        lineEntry.add(Entry(1f, 100f))
        lineEntry.add(Entry(2f, 120f))
        lineEntry.add(Entry(3f, 110f))
        lineEntry.add(Entry(4f, 118f))

        val lineEntryTwo = ArrayList<Entry>()
        lineEntryTwo.add(Entry(1f, 110f))
        lineEntryTwo.add(Entry(2f, 128f))
        lineEntryTwo.add(Entry(3f, 115f))
        lineEntryTwo.add(Entry(4f, 125f))

        val lineDataset = LineDataSet(lineEntry, "Low")
        lineDataset.apply {
            setCircleColor(resources.getColor(R.color.primary))
            color = resources.getColor(R.color.primary)
            setDrawCircleHole(false)
            valueTextSize = 10F

        }

        val lineDatasetTwo = LineDataSet(lineEntryTwo, "High")
        lineDatasetTwo.apply {
            setCircleColor(resources.getColor(R.color.blood))
            color = resources.getColor(R.color.blood)
            setDrawCircleHole(false)
            valueTextSize = 10F

        }
        val iLineDataSet = ArrayList<ILineDataSet>()

        iLineDataSet.add(lineDataset)
        iLineDataSet.add(lineDatasetTwo)

        val data = LineData(iLineDataSet)
        binding.lineChart.xAxis.valueFormatter = (CustomXAxisFormater(xValsDateLabel))
        binding.lineChart.data = data
        binding.lineChart.data.setValueFormatter(CustomYValueFormater())
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.invalidate()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}