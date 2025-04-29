package com.example.controlify

data class ServerItem(
    val id: String,
    val name: String,
    val updatedInfo: String,
    val imageResId: Int,
    val host: String,
    val port: Int = 22
)