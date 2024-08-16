package com.example.search.ui.store

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.search.databinding.FragmentStoreBinding
import com.example.search.model.Item
import com.example.search.viewModel.SharedViewModel
import com.example.search.viewModel.store.StoreViewModel

class StoreFragment : Fragment() {

    // 뷰 바인딩 변수 선언
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    // 어댑터 관련 변수 선언
    private lateinit var mContext: Context
    private lateinit var adapter: StoreAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager

    // 뷰모델 변수 선언
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val viewModel: StoreViewModel by viewModels()

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
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    // UI 초기 설정 진행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStoredItems(mContext) // 데이터 불러오기
        setAdapter()
        observeViewModel()
    }

    private fun setAdapter(){
        // 어댑터 초기화
        adapter = StoreAdapter(mContext)
        binding.rvStoreResult.adapter = adapter

        // 아이템 높이가 불규칙한 그리드 형식으로 배치
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvStoreResult.layoutManager = gridmanager

        // 아이템 클릭 시 각 뷰모델의 LiveData를 변경해 아이템을 삭제처리를 진행함
        adapter.itemClick = object : StoreAdapter.ItemClick {
            override fun onClick(item: Item, position: Int) {
                viewModel.deleteItem(mContext, item)
                sharedViewModel.addDeletedItemUrls(item.url)
            }
        }
    }

    private fun observeViewModel() {
        // 저장된 아이템 리스트를 관찰하여 UI 업데이트
        viewModel.storedItems.observe(viewLifecycleOwner) { items ->
            adapter.items = items
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}