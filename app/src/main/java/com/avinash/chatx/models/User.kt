package com.avinash.chatx.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val following: MutableList<String> = mutableListOf()
)