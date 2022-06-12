package com.example.glucareapplication.ui.dashboard.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucareapplication.databinding.CardLayoutBinding
import com.example.glucareapplication.feature.glucose.domain.model.PredictResultsItem
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import com.example.glucareapplication.ui.scan.result.ScanResultActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoriesAdapter(
    private val listPredictResultsItem: List<PredictResultsItem>
) : RecyclerView.Adapter<HistoriesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val parsedDate = LocalDateTime.parse(listPredictResultsItem[position].createdAt, DateTimeFormatter.ISO_DATE_TIME)
        val formattedHour = parsedDate.format(DateTimeFormatter.ofPattern("K:mm a"))
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("EEE, d MMM"))
        holder.binding.apply {
            tvResult.text = listPredictResultsItem[position].result
            tvHour.text = formattedHour
            tvDate.text =formattedDate
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ScanResultActivity::class.java)
            intent.putExtra("data", SavePredictResponse(
                listPredictResultsItem[position].result,
                listPredictResultsItem[position].image,
                listPredictResultsItem[position].createdAt,
                listPredictResultsItem[position].doctorEmail,
                listPredictResultsItem[position].patientEmail,
                listPredictResultsItem[position].id,
                listPredictResultsItem[position].updatedAt
            ))

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listPredictResultsItem.size
    inner class ListViewHolder(var binding: CardLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
