package com.example.search.util

import android.content.Context
import android.widget.Toast
import androidx.core.content.edit
import com.example.search.model.Item
import com.example.search.util.Constants.PREFS_NAME
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    // 날짜 문자열 포맷팅
    fun getFormatDate(date: String): String {
        // 입출력 형식 지정
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val strToDate = inputFormat.parse(date) // 입력 문자열을 Date? 객체로 변환
        return strToDate?.let { outputFormat.format(it) } ?: "" // null 체크 후 지정한 형식으로 변환
    }

    // 선택한 아이템 저장 (키: 아이템 JSON 문자열, 값: 아이템 url)
    fun addStorageItem(context: Context, item: Item) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(item.url, Gson().toJson(item)) }
    }

    // 선택한 아이템 삭제
    fun delStorageItem(context: Context, url: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { remove(url) } // 키(url)를 기준으로 아이템 삭제
    }

    // 저장된 아이템들 가져오기
    fun getStorageItems(context: Context): ArrayList<Item> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val selectedItems = ArrayList<Item>()
        // prefs.all은 북마크 된 모든 아이템들에 해당함
        for ((_, value) in prefs.all) {
            // 북마크된 아이템(JSON 문자열)을 Item 객체로 변환 후 추가
            val item = Gson().fromJson(value as String, Item::class.java)
            selectedItems.add(item)
        }
        return selectedItems
    }

    // 토스트 메시지 띄우기
    fun showToast(context: Context, text: String ){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}