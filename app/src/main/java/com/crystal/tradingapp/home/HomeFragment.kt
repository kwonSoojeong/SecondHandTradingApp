package com.crystal.tradingapp.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.tradingapp.R
import com.crystal.tradingapp.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding? = null
    private lateinit var articalAdapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding= FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding
        articalAdapter = ArticleAdapter()
        articalAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("001","가방", 100000, "5000원",""))
            add(ArticleModel("002","신발", 100001, "4000원",""))
        })

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter =articalAdapter


    }
}