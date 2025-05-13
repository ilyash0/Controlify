package com.example.controlify.server_detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.controlify.server_list.ServerItem

class ServerDetailViewModelFactory(
    private val application: Application,
    private val server: ServerItem
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServerDetailViewModel::class.java)) {
            return ServerDetailViewModel(application, server) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
