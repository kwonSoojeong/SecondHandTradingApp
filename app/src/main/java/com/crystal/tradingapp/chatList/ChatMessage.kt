package com.crystal.tradingapp.chatList

data class ChatMessage(
    val senderId: String,
    val message: String,
    val time:Long
) {
    constructor() : this("", "", 0L)

}
