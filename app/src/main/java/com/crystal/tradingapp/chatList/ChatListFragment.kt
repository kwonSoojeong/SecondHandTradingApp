package com.crystal.tradingapp.chatList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.tradingapp.DBKey
import com.crystal.tradingapp.R
import com.crystal.tradingapp.databinding.FragmentChatListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private val chatRoomList = mutableListOf<ChatLabelModel>()
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val usersDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DBKey.DB_USERS)
    }

    private val chatLabelAdapter: ChatLabelAdapter by lazy {
        ChatLabelAdapter { chatLabelModel ->
            context?.let { it ->
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatLabelModel.key)
                startActivity(intent)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatListBinding.bind(view)
        binding.chatRecyclerView.adapter = chatLabelAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)

        //데이터 불러오기
        loadChatList()
    }

    private fun loadChatList() {
        chatRoomList.clear()
        if (auth.currentUser == null) {
            return
        }

        auth.currentUser?.let {
            val chatDB = usersDB.child(it.uid).child(DBKey.CHAT_LIST)

            chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { data ->
                        val model = data.getValue(ChatLabelModel::class.java)
                        model ?: return
                        chatRoomList.add(model)
                    }
                    chatLabelAdapter.submitList(chatRoomList)
                    chatLabelAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        chatLabelAdapter.notifyDataSetChanged()
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        _binding = null
    }
}