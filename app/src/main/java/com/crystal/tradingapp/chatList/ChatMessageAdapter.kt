package com.crystal.tradingapp.chatList

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crystal.tradingapp.databinding.ItemChatMessageBinding
import java.text.SimpleDateFormat
import java.util.*


class ChatMessageAdapter : ListAdapter<ChatMessage, ChatMessageAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(message: ChatMessage) {
            binding.messageTextView.text = message.message
            binding.senderTextView.text = message.senderId

            val timeText = SimpleDateFormat("hh:mm:ss").format(Date(message.time))
//                .format(DateTimeFormatter.ofPattern("HH:mm"))

            binding.timeTextView.text = timeText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }
        }
    }

}
