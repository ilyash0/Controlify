package com.example.controlify.server_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ServersViewModel(application: Application) : AndroidViewModel(application) {
    private val _servers = MutableLiveData<MutableList<ServerItem>>()
    val servers: LiveData<MutableList<ServerItem>> = _servers

    init { loadServers() }

    fun loadServers() {
        Log.d("ServersViewModel", "loadServers!")
        val list = Prefs.getServers(getApplication())
        _servers.value = list.toMutableList()
    }

    fun addServer(item: ServerItem) {
        val current = _servers.value ?: emptyList()
        val updated = current + item
        _servers.value = updated.toMutableList()
        Prefs.saveServers(getApplication(), updated)
    }

    fun deleteServer(item: ServerItem) {
        val current = _servers.value ?: return
        val updated = current.filter { it.id != item.id }.toMutableList()
        _servers.value = updated
        Prefs.saveServers(getApplication(), updated)
    }
}
