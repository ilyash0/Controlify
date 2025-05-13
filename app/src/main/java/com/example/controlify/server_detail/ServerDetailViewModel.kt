package com.example.controlify.server_detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.controlify.server_list.ServerItem
import com.example.controlify.Prefs

class ServerDetailViewModel(application: Application, private val server: ServerItem) : AndroidViewModel(application) {
    private val _presets = MutableLiveData<MutableList<CommandPreset>>()
    val presets: LiveData<MutableList<CommandPreset>> = _presets

    init { loadPresets() }

    fun loadPresets() {
        Log.d("ServerDetailVM", "Loading presets for server ${server.id}")
        val list = Prefs.getPresets(getApplication(), server.id)
        _presets.value = list.toMutableList()
    }

    fun addPreset(preset: CommandPreset) {
        val current = _presets.value ?: mutableListOf()
        val updated = (current + preset).toMutableList()
        _presets.value = updated
        Prefs.savePresets(getApplication(), server.id, updated)
    }

    fun deletePreset(preset: CommandPreset) {
        val current = _presets.value ?: return
        val updated = current.filter { it.name != preset.name }.toMutableList()
        _presets.value = updated
        Prefs.savePresets(getApplication(), server.id, updated)
    }
}
