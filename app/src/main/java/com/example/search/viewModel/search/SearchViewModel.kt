package com.example.search.viewModel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.search.data.api.RetrofitInterface
import com.example.search.data.model.ImageResponse
import com.example.search.data.model.VideoResponse
import com.example.search.model.Item
import com.example.search.util.Constants.AUTH_HEADER
import com.example.search.util.Constants.SEARCH_TYPE_IMAGE
import com.example.search.util.Constants.SEARCH_TYPE_VIDEO
import com.example.search.util.Utils.getStorageItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.Collections

class SearchViewModel(private val apiService: RetrofitInterface) : ViewModel() {

    // 검색 결과에 대한 LiveData 선언
    private val _searchResults = MutableLiveData<List<Item>>()
    val searchResults: LiveData<List<Item>> get() = _searchResults

    // 로딩 상태에 대한 LiveData 선언
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 페이지, 결과 아이템 등의 변수 선언
    var pageCnt = 1
    var itemList = ArrayList<Item>()
    var maxImagePage = 1
    var maxVideoPage = 1

    var isImageSearchFinished = false
    var isVideoSearchFinished = false

    // 검색을 실행하는 메서드
    fun search(query: String, page: Int) {
        itemList.clear() // 이전 검색결과를 모두 제거
        _isLoading.value = true // 검색이 끝날 때까지 로딩 실행

        // 비동기 작업을 위해 검색 완료 여부를 변수로 선언
        isImageSearchFinished = false
        isVideoSearchFinished = false

        // 페이지 범위 내에서 이미지와 비디오 검색을 수행
        if (page <= maxImagePage) getImageItems(query, page)
        if (page <= maxVideoPage) getVideoItems(query, page)
    }

    // 이미지 검색 결과를 가져오는 메소드
    private fun getImageItems(query: String, page: Int) {
        apiService.searchImage(AUTH_HEADER, query, "accuracy", page, 40)
            .enqueue(object : Callback<ImageResponse> {
                override fun onResponse(
                    call: Call<ImageResponse>,
                    response: Response<ImageResponse>
                ) {
                    // api 호출의 반환값이 null이 아닌지 1차로 확인
                    response.body()?.meta?.let { meta ->
                        // 검색 결과가 존재하는지 2차로 확인
                        if (meta.totalCount > 0) {
                            // 검색 결과를 itemList에 추가
                            for (document in response.body()!!.documents) {
                                val title = document.displaySitename
                                val datetime = document.datetime
                                val url = document.thumbnailUrl
                                itemList.add(Item(SEARCH_TYPE_IMAGE, title, datetime, url))
                            }
                            // 검색 가능한 페이지 수 갱신
                            maxImagePage = meta.pageableCount
                        }
                    }
                    isImageSearchFinished = true
                    checkSearchCompletion()
                }

                override fun onFailure(p0: Call<ImageResponse>, p1: Throwable) {
                    Log.e("#jblee", "onFailure: ${p1.message}")
                }

            })
    }

    // 비디오 검색 결과를 가져오는 메서드
    private fun getVideoItems(query: String, page: Int) {
        apiService.searchVideo(AUTH_HEADER, query, "accuracy", page, 15)
            .enqueue(object : Callback<VideoResponse> {
                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>
                ) {
                    response.body()?.meta?.let { meta ->
                        if (meta.totalCount > 0) {
                            for (document in response.body()!!.documents) {
                                val title = document.title
                                val datetime = document.datetime
                                val url = document.thumbnail
                                itemList.add(Item(SEARCH_TYPE_VIDEO, title, datetime, url))
                            }
                            maxVideoPage = meta.pageableCount
                        }
                    }
                    isVideoSearchFinished = true
                    checkSearchCompletion()
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.e("##jblee", "onFailure: ${t.message}")
                }

            })
    }

    // 이미지와 비디오 검색이 모두 완료되었는지 확인하는 메서드
    private fun checkSearchCompletion() {
        if (isImageSearchFinished && isVideoSearchFinished) {
            searchResult()
        }
    }

    // 검색 결과를 LiveData에 설정하는 메서드
    private fun searchResult() {

        // 날짜 별로 정렬
        Collections.sort(itemList) { list, c -> c.dateTime.compareTo(list.dateTime) }

        // 검색 결과를 LiveData에 설정
        _searchResults.value = itemList

        // 로딩 상태 업데이트
        _isLoading.value = false
    }
}