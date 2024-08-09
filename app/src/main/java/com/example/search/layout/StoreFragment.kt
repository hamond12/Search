package com.example.search.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.search.util.Storage
import com.example.search.adapter.StoreAdapter
import com.example.search.api.Document
import com.example.search.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private lateinit var binding: FragmentStoreBinding
    private lateinit var adapter: StoreAdapter
    private lateinit var documents: MutableList<Document>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = Storage(requireContext())
        documents = storage.getStorageItems()

        adapter = StoreAdapter(documents, requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
    }
}