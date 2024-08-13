package com.example.search.fragment.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.search.MainActivity
import com.example.search.api.APIResponse
import com.example.search.api.Document
import com.example.search.databinding.ItemBinding
import com.example.search.util.Utils
import com.example.search.util.Utils.getFormatDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SearchAdapter(private val mContext: Context) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var items = ArrayList<Document>()

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var thumbnail = binding.ivThumbnail
        var sitename = binding.tvSitename
        var datetime = binding.tvDatetime
        var likeIcon = binding.ivLike
            
        fun bind(item: Document) {
            Glide.with(mContext).load(item.thumbnailUrl).into(binding.ivThumbnail)
            sitename.text = item.displaySitename
            datetime.text = getFormatDate(item.datetime)
            likeIcon.visibility = if (item.isSelected) View.VISIBLE else View.INVISIBLE
        }
        
        init {
            thumbnail.setOnClickListener(this)
            likeIcon.setOnClickListener(this)
        }

        // 아이템 클릭 시 발생하는 이벤트 처리
        override fun onClick(view: View) {
            val item = items[adapterPosition]

            item.isSelected = !item.isSelected
            
            if(item.isSelected){
                (mContext as MainActivity).addLikedItem(item)
            } else{
                (mContext as MainActivity).removeLikedItem(item)
            }

            notifyItemChanged(adapterPosition)
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

    // 아이템 목록 갱신
    fun updateItem(newItems: ArrayList<Document>) {
        items = newItems
        notifyDataSetChanged()
    }

    // 아이템 목록 초기화
    fun clearItem() {
        items.clear()
        notifyDataSetChanged()
    }
}

