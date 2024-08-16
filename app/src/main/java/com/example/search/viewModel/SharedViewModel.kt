package com.example.search.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    // 삭제된 아이템 url 리스트 선언
    private val _deletedItemUrls = MutableLiveData<List<String>>()
    val deletedItemUrls get() = _deletedItemUrls

    // 삭제된 아이템 url 리스트 갱신
    fun addDeletedItemUrls(url: String) {
        val currentList = _deletedItemUrls.value ?: emptyList()
        _deletedItemUrls.value = currentList + url
    }

    // 삭제된 아이템 url 리스트 초기화
    fun clearDeletedItemUrls() {
        _deletedItemUrls.value = emptyList()
    }
}
