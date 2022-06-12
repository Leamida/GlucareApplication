package com.example.glucareapplication.ui.dashboard.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.glucareapplication.databinding.CardLayoutBinding
import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResultsItem

class HistoriesAdapter(
    private val listPredictResultsItem: List<PredictResultsItem?>
) : RecyclerView.Adapter<HistoriesAdapter.ListViewHolder>() {

//    private lateinit var onItemClickCallback: OnItemClickCallback

//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvResult.text = listPredictResultsItem[position]?.createdAt
        }

//        holder.itemView.setOnClickListener {
//            listPredictResultsItem[holder.adapterPosition]?.let { onItemClickCallback.onItemClicked(it) }
//        }
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
//            intent.putExtra("Data", listStory[position])
//
//            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
//        }
    }

    override fun getItemCount(): Int = listPredictResultsItem.size
    inner class ListViewHolder(var binding: CardLayoutBinding) : RecyclerView.ViewHolder(binding.root)
//    interface OnItemClickCallback {
//        fun onItemClicked(data: PredictResultsItem)
//    }
}
