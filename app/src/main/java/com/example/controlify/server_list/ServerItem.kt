package com.example.controlify.server_list

data class ServerItem(
    val id: String,
    val name: String,
    val updatedInfo: String,
    val imageResId: Int,
    val host: String,
    val port: Int = 22
)