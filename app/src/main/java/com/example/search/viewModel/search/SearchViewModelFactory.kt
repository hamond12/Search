package com.example.search.viewModel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.search.data.api.RetrofitInterface

class SearchViewModelFactory(private val apiService: RetrofitInterface) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(apiService) as T
    }
}