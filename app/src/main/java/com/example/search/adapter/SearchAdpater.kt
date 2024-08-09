package com.example.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.search.util.Storage
import com.example.search.api.Document
import com.example.search.databinding.ItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SearchAdapter(private var documents: MutableList<Document>, private val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val storage = Storage(context)
    private val selectedItems = storage.getStorageItems()

    private fun formatDateTime(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateTimeString)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Document) {
            Glide.with(itemView.context).load(item.thumbnail_url).into(binding.ivThumbnail)
            binding.tvSitename.text = item.display_sitename
            binding.tvDatetime.text = formatDateTime(item.datetime)

            binding.ivStar.visibility =
                if (item in selectedItems) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener {

                if (item in selectedItems) {
                    selectedItems.remove(item)
                    storage.removeStorageItem(item)
                } else {
                    selectedItems.add(item)
                    storage.addStorageItem(item)
                }

                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = documents[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = documents.size

    fun updateData(newDocuments: MutableList<Document>) {
        documents = newDocuments
        notifyDataSetChanged()
    }
}

