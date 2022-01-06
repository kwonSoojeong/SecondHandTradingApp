package com.crystal.tradingapp.chatList

data class ChatLabelModel(
    val buyerId: String,
    val sellerID: String,
    val itemTitle: String,
    val key: Long //date time
) {
    constructor() : this("", "", "", 0L)
}