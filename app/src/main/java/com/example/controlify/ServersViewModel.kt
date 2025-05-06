package com.example.controlify

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServersViewModel(private val context: Context) : ViewModel() {
    private val initial = Prefs.getServers(context)
    private val _servers = MutableLiveData<MutableList<ServerItem>>(initial)
    val servers: LiveData<MutableList<ServerItem>> = _servers

    fun addServer(item: ServerItem) {
        val current = _servers.value ?: emptyList()
        val updated = current + item
        _servers.value = updated.toMutableList()
        Prefs.saveServers(context, updated)
    }
}
