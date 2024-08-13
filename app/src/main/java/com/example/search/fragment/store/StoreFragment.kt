package com.example.search.fragment.store

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.search.MainActivity
import com.example.search.api.Document
import com.example.search.databinding.FragmentSerachBinding
import com.example.search.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: Context
    private lateinit var adapter: StoreAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainActivity = activity as MainActivity

        adapter = StoreAdapter(mContext).apply {
            items = mainActivity.selectedItems.toMutableList()
        }

        _binding = FragmentStoreBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(context, 2)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}