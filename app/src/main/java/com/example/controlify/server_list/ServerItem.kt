package com.example.controlify.server_list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServerItem(
    val id: String,
    val name: String,
    val imageResId: Int,
    val host: String,
    val port: Int = 22,
    val username: String = "",
    val password: String = "",
) : Parcelable