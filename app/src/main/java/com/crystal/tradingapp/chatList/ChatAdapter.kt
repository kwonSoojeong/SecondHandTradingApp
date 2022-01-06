package com.crystal.tradingapp.chatList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crystal.tradingapp.databinding.ItemChatBinding


class ChatAdapter(val onItemClicked: (ChatModel) -> Unit) :
    ListAdapter<ChatModel, ChatAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatModel: ChatModel) {
            binding.chatRoomTitleTextView.text = chatModel.itemTitle
            binding.root.setOnClickListener {
                onItemClicked(chatModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                //고유한 키값을 비교
                return oldItem.key == newItem.key
            }
            override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}
