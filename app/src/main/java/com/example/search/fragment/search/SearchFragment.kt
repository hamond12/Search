package com.example.search.fragment.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.search.util.Constants.API_KEY
import com.example.search.api.APIResponse
import com.example.search.api.Document
import com.example.search.api.RetrofitInstance
import com.example.search.databinding.FragmentSerachBinding
import com.example.search.util.Utils.getLastQuery
import com.example.search.util.Utils.saveLastQuery
import com.example.search.util.Utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSerachBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: Context
    private lateinit var adapter: SearchAdapter

    private var items: ArrayList<Document> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSerachBinding.inflate(inflater, container, false)

        setUpView()
        setUpListener()

        return binding.root
    }


    private fun setUpView(){
        adapter = SearchAdapter(mContext)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        val lastSearch = getLastQuery(requireContext())
        binding.etSearch.setText(lastSearch)
    }

    private fun setUpListener(){
        binding.btnSearch.setOnClickListener {
            hideKeyboard(it)

            val query = binding.etSearch.text.toString()
            if (query.isNotBlank()) {
                adapter.clearItem()
                search(query)
            } else {
                showToast(requireContext(), "검색어를 입력해주세요.")
            }

        }
    }

    private fun hideKeyboard(view:View){
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun search(query: String) {
        val authHeader = "KakaoAK ${API_KEY}"
        val call = RetrofitInstance.apiService.searchImage(authHeader, query)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(
                call: Call<APIResponse>,
                response: Response<APIResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("DEBUG", "${response.body()}")
                    val apiResponse = response.body()!!
                    items = apiResponse.documents
                    adapter.updateItem(items)
                    saveLastQuery(requireContext(),query)
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("DEBUG", "통신 실패: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

