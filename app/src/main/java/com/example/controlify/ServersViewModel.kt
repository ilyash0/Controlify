package com.example.controlify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServersViewModel : ViewModel() {
    private val _servers = MutableLiveData<MutableList<ServerItem>>(mutableListOf())
    val servers: MutableLiveData<MutableList<ServerItem>> = _servers

    fun addServer(item: ServerItem) {
        _servers.value?.let {
            it.add(item)
            _servers.value = it   // Обновляем LiveData
        }
    }
}
