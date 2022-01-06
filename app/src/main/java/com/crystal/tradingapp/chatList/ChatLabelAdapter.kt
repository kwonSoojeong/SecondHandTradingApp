package com.crystal.tradingapp.chatList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crystal.tradingapp.databinding.ItemChatBinding


class ChatLabelAdapter(val onItemClicked: (ChatLabelModel) -> Unit) :
    ListAdapter<ChatLabelModel, ChatLabelAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatLabelModel: ChatLabelModel) {
            binding.chatRoomTitleTextView.text = chatLabelModel.itemTitle
            binding.root.setOnClickListener {
                onItemClicked(chatLabelModel)
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
        val diffUtil = object : DiffUtil.ItemCallback<ChatLabelModel>() {
            override fun areItemsTheSame(oldItem: ChatLabelModel, newItem: ChatLabelModel): Boolean {
                //고유한 키값을 비교
                return oldItem.key == newItem.key
            }
            override fun areContentsTheSame(oldItem: ChatLabelModel, newItem: ChatLabelModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}
