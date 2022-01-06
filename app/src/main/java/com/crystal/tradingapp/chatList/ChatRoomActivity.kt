package com.crystal.tradingapp.chatList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.tradingapp.DBKey
import com.crystal.tradingapp.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private var chatDB: DatabaseReference? = null

    private val chatList = mutableListOf<ChatMessage>()
    private val chatMessageAdapter = ChatMessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initChatDB()

        initRecyclerView()
    }

    private fun initChatDB() {
        val chatKey = intent.getLongExtra("chatKey", -1)

        chatDB = Firebase.database.reference.child(DBKey.DB_CHATS).child("$chatKey")
        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                chatMessage ?: return
                chatList.add(chatMessage)
                chatMessageAdapter.submitList(chatList)
                chatMessageAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun initRecyclerView() {
        binding.chatRoomRecyclerView.adapter = chatMessageAdapter
        binding.chatRoomRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.sendButton.setOnClickListener {
            val chatItem = ChatMessage(
                auth.currentUser?.uid.orEmpty(),
                binding.chatEditText.text.toString(),
                System.currentTimeMillis()
            )
            chatDB?.let {
                it.push().setValue(chatItem)
            }
        }
    }

}