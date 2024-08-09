package com.example.search.layout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.search.util.Constants.API_KEY
import com.example.search.util.Constants.PREF
import com.example.search.util.Constants.QUERY
import com.example.search.adapter.SearchAdapter
import com.example.search.api.APIResponse
import com.example.search.api.Document
import com.example.search.api.RetrofitClient
import com.example.search.databinding.FragmentSerachBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSerachBinding
    private lateinit var adapter: SearchAdapter
    private lateinit var pref: SharedPreferences
    private var documents: MutableList<Document> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSerachBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = requireContext().getSharedPreferences(PREF, 0)

        loadQuery()

        adapter = SearchAdapter(documents, requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)

            val query = binding.etSerach.text.toString()
            search(query)
            saveQuery()
        }
    }

    private fun hideKeyboard(view:View){
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun search(query: String) {
        val authHeader = "KakaoAK ${API_KEY}"
        val call = RetrofitClient.apiService.searchImage(authHeader, query)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(
                call: Call<APIResponse>,
                response: Response<APIResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        documents = apiResponse.documents
                        adapter.updateData(documents)
                    }
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("DEBUG", "통신 실패: ${t.message}")
            }
        })
    }

    private fun saveQuery() {
        pref.edit { putString(QUERY, binding.etSerach.text.toString()) }
    }

    private fun loadQuery() {
        binding.etSerach.setText(pref.getString(QUERY, ""))
    }
}

