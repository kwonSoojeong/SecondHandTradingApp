package com.crystal.tradingapp.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.crystal.tradingapp.R

class MyPageFragment: Fragment(R.layout.fragment_mypage)  {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyPageFragment", "onViewCreated")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MyPageFragment", "onCreated")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MyPageFragment", "onResume")
    }
}