package com.example.search.ui.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.search.databinding.ItemBinding
import com.example.search.model.Item
import com.example.search.util.Constants.SEARCH_TYPE_VIDEO
import com.example.search.util.Utils.getFormatDate

class StoreAdapter(private val mContext: Context) :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    // 북마크된 아이템
    var items = ArrayList<Item>()

    // 항목 클릭 리스너 인터페이스
    interface ItemClick {
        fun onClick(item: Item, position: Int)
    }

    var itemClick: ItemClick? = null

    // 뷰홀더 선언
    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            val type = if (item.type == SEARCH_TYPE_VIDEO) "[동영상] " else "[이미지] "
            Glide.with(mContext).load(item.url).into(binding.ivThumbnail)
            binding.tvTitle.text = type + item.title
            binding.tvDatetime.text = getFormatDate(item.dateTime)

            binding.itemView.setOnClickListener {
                val position = adapterPosition
                itemClick?.onClick(items[position], position)
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