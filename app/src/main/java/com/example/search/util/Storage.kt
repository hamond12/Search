package com.example.search.util

import android.content.Context
import androidx.core.content.edit
import com.example.search.util.Constants.PREF
import com.example.search.util.Constants.STORAGE_ITEMS
import com.example.search.api.Document
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Storage(context: Context) {
    private val pref = context.getSharedPreferences(PREF, 0)

    fun getStorageItems(): MutableList<Document> {
        val jsonString = pref.getString(STORAGE_ITEMS, "")
        return if (jsonString.isNullOrEmpty()) {
            mutableListOf()
        } else {
            Gson().fromJson(jsonString, object : TypeToken<List<Document>>() {}.type)
        }
    }

    fun saveStorageItems(items: List<Document>) {
        val jsonString = Gson().toJson(items)
        pref.edit { putString(STORAGE_ITEMS, jsonString) }
    }

    fun addStorageItem(item: Document) {
        val items = getStorageItems()
        items.add(item)
        saveStorageItems(items)
    }

    fun removeStorageItem(document: Document) {
        val favoriteItems = getStorageItems().toMutableList()
        favoriteItems.removeAll { it.thumbnail_url == document.thumbnail_url }
        saveStorageItems(favoriteItems)
    }

}