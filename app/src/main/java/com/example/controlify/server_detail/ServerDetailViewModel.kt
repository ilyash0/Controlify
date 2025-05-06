package com.example.controlify.server_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.controlify.server_list.ServerItem

class ServerDetailViewModel(private val server: ServerItem) : ViewModel() {
    private val _presets = MutableLiveData<List<CommandPreset>>(emptyList())
    val presets: LiveData<List<CommandPreset>> = _presets

    fun loadPresets() {
        // TODO: загрузить иp Prefs
        _presets.value = listOf(
            CommandPreset("Restart", listOf("sudo reboot")),
            CommandPreset("Uptime", listOf("uptime"))
        )
    }

    fun addPreset(preset: CommandPreset) {
        _presets.value = _presets.value!!.plus(preset)
        // TODO: сохранить в Prefs
    }
}