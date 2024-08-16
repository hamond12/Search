package com.example.search.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.search.data.api.RetrofitInstance
import com.example.search.databinding.FragmentSearchBinding
import com.example.search.util.Utils.showToast
import com.example.search.viewModel.SharedViewModel
import com.example.search.viewModel.search.SearchViewModel
import com.example.search.viewModel.search.SearchViewModelFactory

class SearchFragment : Fragment() {

    // 뷰 바인딩 변수 선언
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // 어댑터 관련 변수 선언
    private lateinit var mContext: Context
    private lateinit var adapter: SearchAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager

    // 뷰모델 변수 선언
    private val apiService = RetrofitInstance.apiService
    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(apiService) }
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    // 검색 상태 변수들
    private var query = ""
    private var loading = true
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    // 프래그먼트의 컨텍스트 변수 초기화
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    // 프래그먼트 UI 생성
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    // UI 초기 설정 진행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpListener()

        observeViewModel()
    }

    // 리사이클러뷰 스크롤 리스너 - 스크롤 시 다음 페이지의 데이터 불러옴
    private var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            visibleItemCount = gridmanager.childCount // 현재 화면에 표시하고 있는 아이템 개수
            totalItemCount = gridmanager.itemCount // 어댑터가 포함하고 있는 전체 아이템 개수

            //현재 페이지에서 보이는 첫 번째 아이템 위치 업데이트
            val firstVisibleItems = gridmanager.findFirstVisibleItemPositions(null)
            if (firstVisibleItems.isNotEmpty()) {
                pastVisibleItems = firstVisibleItems[0]
            }

            // 리싸이클러뷰의 끝에 도달 했을 때 다음 페이지의 데이터를 로드해 무한 스크롤 구현
            if (loading && visibleItemCount + pastVisibleItems >= totalItemCount) {
                loading = false
                viewModel.pageCnt += 1
                viewModel.search(query, viewModel.pageCnt)
            }

            // 리싸이클러뷰의 y좌표에 따라 플로팅 액션 버튼 활성화
            if (dy > 0 && binding.fabTop.visibility == View.VISIBLE) {
                binding.fabTop.hide()
            } else if (dy < 0 && binding.fabTop.visibility != View.VISIBLE) {
                binding.fabTop.show()
            }

            // 위쪽으로 스크롤이 불가하다면 (리싸이클러뷰의 스크롤이 최상단에 위치한다면)
            if (!recyclerView.canScrollVertically(-1)) {
                binding.fabTop.hide() // 플로팅 액션 버튼을 숨김
            }
        }
    }

    // 뷰 초기 설정
    private fun setUpView() {
        // 어댑터 설정
        adapter = SearchAdapter(mContext)
        binding.rvSearchResult.adapter = adapter

        // 아이템 높이가 불규칙한 그리드 형식으로 배치
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSearchResult.layoutManager = gridmanager

        // 리싸이클러뷰 설정
        binding.rvSearchResult.addOnScrollListener(onScrollListener) // 무한 스크롤 설정
        binding.rvSearchResult.itemAnimator = null // 아이템 클릭 애니메이션 비활성화

        // 검색 전에 플로팅 액션 버튼은 보이지 않도록 설정
        binding.fabTop.visibility = View.INVISIBLE
    }

    // 키보드 숨김 메소드
    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    // 프래그먼트 내 리스너 설정
    private fun setUpListener() {

        // 플로팅 액션 버튼 동작 정의
        binding.fabTop.setOnClickListener {
            // 리싸이클러뷰의 스크롤을 최상단으로 부드럽게 이동
            binding.rvSearchResult.smoothScrollToPosition(0)
        }

        // 검색 버튼 동작 정의
        binding.btnSearch.setOnClickListener {
            hideKeyboard(it) // 키보드 숨김

            // 검색어를 입력하지 않았을 땐 토스트 메시지를 띄움
            if (binding.etSearch.text.toString() == "") {
                showToast(mContext, "검색어를 입력해 주세요.")
            } else {
                adapter.clearItem() // 기존 아이템들 삭제
                query = binding.etSearch.text.toString() // 검색어 초기화
                loading = false // 로딩 완료
                viewModel.search(query, viewModel.pageCnt) // 검색 실행
            }
        }
    }


    // ViewModel에서 데이터 변화를 관찰
    private fun observeViewModel() {

        // 검색 결과에 변화가 감지되면 아이템 리스트 초기화
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            adapter.items.addAll(items)
            adapter.notifyDataSetChanged()
        }

        // 로딩 상태의 변화를 감지해 프로그래스바 활성화/비활성화
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbSearch.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
            loading = !isLoading
        }

        // 삭제된 아이템 url 리스트에 변화가 감지되면 (보관함에서 아이템을 삭제하면)
        sharedViewModel.deletedItemUrls.observe(viewLifecycleOwner) { urls ->
            urls.forEach { url ->
                // url을 통해 보관함에서 삭제된 아이템을 찾아
                val targetItem = adapter.items.find { it.url == url }

                // 해당 아이템의 북마크를 비활성화함
                targetItem?.let {
                    it.isLike = false
                    val itemIndex = adapter.items.indexOf(it)
                    adapter.notifyItemChanged(itemIndex)
                }
            }

            // url 리스트를 빈 목록으로 초기화한다.
            sharedViewModel.clearDeletedItemUrls()
        }
    }

    // 프래그먼트를 전환할 떄 바인딩 객체를 null로 설정
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}