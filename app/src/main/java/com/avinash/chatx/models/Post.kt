package com.avinash.chatx.models

data class Post(
    val text: String = "",
    val imageUrl: String? = null,
    val user: User = User(),
    val time: Long = 0L,
    val likeList: MutableList<String> = mutableListOf()
)
