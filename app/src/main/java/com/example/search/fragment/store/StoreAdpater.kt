package com.example.search.fragment.store

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.search.api.Document
import com.example.search.databinding.ItemBinding
import com.example.search.util.Utils.getFormatDate

class StoreAdapter(private val mContext: Context) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    var items = mutableListOf<Document>()

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Document) {
            Glide.with(mContext).load(item.thumbnailUrl).into(binding.ivThumbnail)
            binding.tvSitename.text = item.displaySitename
            binding.tvDatetime.text = getFormatDate(item.datetime)

            binding.ivThumbnail.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}
