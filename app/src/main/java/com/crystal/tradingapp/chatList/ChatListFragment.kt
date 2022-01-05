package com.crystal.tradingapp.chatList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.crystal.tradingapp.R

class ChatListFragment: Fragment(R.layout.fragment_chat_list)  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ChatListFragment", "onCreated")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ChatListFragment", "onResume")
    }
}