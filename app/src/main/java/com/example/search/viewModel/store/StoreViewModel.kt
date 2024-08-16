package com.example.search.viewModel.store

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.search.model.Item
import com.example.search.util.Utils

class StoreViewModel : ViewModel() {

    // 저장된 아이템들에 대한 LiveData 선언
    private val _storedItems = MutableLiveData<ArrayList<Item>>()
    val storedItems get() = _storedItems

    // 저장된 아이템들을 가져오는 함수
    fun getStoredItems(context: Context) {
        _storedItems.value = Utils.getStorageItems(context)
    }

    // 특정 아이템을 삭제하는 함수
    fun deleteItem(context: Context, item: Item) {

        // 아이템 삭제 여부를 키값에 저장
        Utils.delStorageItem(context, item.url)

        // 삭제된 아이템 정보를 반영하여 LiveData 업데이트
        _storedItems.value?.let { items ->
            items.remove(item)
            _storedItems.value = items
        }
    }
}