package com.example.search.util

import android.content.Context
import android.widget.Toast
import androidx.core.content.edit
import com.example.search.util.Constants.LAST_QUERY
import com.example.search.util.Constants.PREFS_NAME
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun getFormatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date? = inputFormat.parse(date)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun saveLastQuery(context: Context, query: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putString(LAST_QUERY, query) }
    }

    fun getLastQuery(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(LAST_QUERY, null)
    }

    fun showToast(context: Context, text: String ){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}