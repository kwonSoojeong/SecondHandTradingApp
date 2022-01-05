package com.crystal.tradingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.crystal.tradingapp.chatList.ChatListFragment
import com.crystal.tradingapp.home.HomeFragment
import com.crystal.tradingapp.mypage.MyPageFragment

import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    private val homeFragment = HomeFragment()
    private val chatListFragment = ChatListFragment()
    private val myPageFragment = MyPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)

        val bottomNaviMenuView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNaviMenuView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home ->{replaceFragment(homeFragment)}
                R.id.chatList -> {replaceFragment(chatListFragment)}
                R.id.myPage -> {replaceFragment(myPageFragment)}
            }
            true

        }
    }
    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}