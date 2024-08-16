package com.example.search.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.search.MainActivity
import com.example.search.databinding.ItemBinding
import com.example.search.model.Item
import com.example.search.util.Constants.SEARCH_TYPE_VIDEO
import com.example.search.util.Utils.addStorageItem
import com.example.search.util.Utils.delStorageItem
import com.example.search.util.Utils.getFormatDate


class SearchAdapter(private val mContext: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    // 검색된 아이템들
    var items = ArrayList<Item>()

    // 아이템 전체 삭제
    fun clearItem() {
        items.clear()
        notifyDataSetChanged()
    }

    // 뷰홀더 선언
    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: Item) {
            val type = if (item.type == SEARCH_TYPE_VIDEO) "[동영상] " else "[이미지] "
            Glide.with(mContext).load(item.url).into(binding.ivThumbnail)
            binding.tvTitle.text = type + item.title
            binding.tvDatetime.text = getFormatDate(item.dateTime)
            binding.ivLike.visibility = if (item.isLike) View.VISIBLE else View.INVISIBLE
        }

        init {
            // 아이템의 썸네일과 아이콘을 누르면 북마크 상태 변경
            binding.ivThumbnail.setOnClickListener(this)
            binding.ivLike.setOnClickListener(this)

        }

        // 아이템 좋아요 여부에 따른 클릭 이벤트 처리
        override fun onClick(view: View) {
            val position = adapterPosition
            val item = items[position]

            if (!item.isLike) {
                addStorageItem(mContext, item)
                item.isLike = true
            } else {
                delStorageItem(mContext, item.url)
                item.isLike = false
            }

            notifyItemChanged(position)
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

