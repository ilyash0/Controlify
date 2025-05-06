package com.example.controlify.server_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.controlify.server_list.ServerItem

class ServerDetailViewModelFactory(private val server: ServerItem) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServerDetailViewModel(server) as T
    }
}