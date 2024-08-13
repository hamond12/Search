package com.example.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.search.api.Document
import com.example.search.databinding.ActivityMainBinding
import com.example.search.fragment.search.SearchFragment
import com.example.search.fragment.store.StoreFragment

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    var selectedItems: ArrayList<Document> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            btn1.setOnClickListener { setFragment(SearchFragment()) }
            btn2.setOnClickListener { setFragment(StoreFragment()) }
        }

        setFragment(SearchFragment())
    }

    private fun setFragment(frag : Fragment) {
        supportFragmentManager.commit {
            replace(R.id.frameLayout, frag)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

    fun addLikedItem(item: Document) {
        if(!selectedItems.contains(item)) {
            selectedItems.add(item)
        }
    }

    fun removeLikedItem(item: Document) {
        selectedItems.remove(item)
    }
}