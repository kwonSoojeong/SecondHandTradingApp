package com.crystal.tradingapp.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.tradingapp.DBKey
import com.crystal.tradingapp.R
import com.crystal.tradingapp.chatList.ChatLabelModel
import com.crystal.tradingapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var articleDB: DatabaseReference
    private lateinit var usersDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter
    private val articleList = mutableListOf<ArticleModel>()
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            //모델클래스 자체를 업로드한다. 따라서 클래스로 받는다
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return
            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated")

        binding = FragmentHomeBinding.bind(view)

        articleList.clear()
        initDBs()

        initRecyclerView(view)
        initAddButton(view)
    }

    private fun initDBs() {
        articleDB = Firebase.database.reference.child(DBKey.DB_ARTICLES)
        articleDB.addChildEventListener(listener)
        usersDB = Firebase.database.reference.child(DBKey.DB_USERS)
    }

    private fun initAddButton(view: View) {
        binding.addFloatingButton.setOnClickListener {
            if (auth.currentUser != null) {
                startActivity(Intent(requireContext(), AddArticleActivity::class.java))
            } else {
                //need to login
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView(view: View) {
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        articleAdapter = ArticleAdapter(onItemClicked = { articleModle ->
            if (auth.currentUser != null) {
                //로그인 함
                auth.currentUser?.let { currentUser->
                    if (auth.currentUser?.uid == articleModle.sellerId) {
                        //내가 올린 물건
                        Snackbar.make(view, "내가 올린 아이템 입니다", Snackbar.LENGTH_SHORT).show()
                    } else {
                        val chatRoom = ChatLabelModel(
                            buyerId = currentUser.uid,
                            sellerID = articleModle.sellerId,
                            itemTitle = articleModle.title,
                            key = System.currentTimeMillis()
                        )
                        usersDB.child(currentUser.uid)
                            .child(DBKey.CHAT_LIST)
                            .push()
                            .setValue(chatRoom)
                        usersDB.child(articleModle.sellerId)
                            .child(DBKey.CHAT_LIST)
                            .push()
                            .setValue(chatRoom)

                        Snackbar.make(view, "채팅방 생성되었습니다", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                //로그인을 안한상태
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_SHORT).show()
            }
        })
        binding.articleRecyclerView.adapter = articleAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
    }
    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }
}