package com.example.controlify.server_detail

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.controlify.server_list.ServerItem
import com.example.controlify.Prefs
import com.example.controlify.R
import com.example.controlify.SSHManager
import com.example.controlify.SoundPlayer
import com.jcraft.jsch.JSchException
import kotlinx.coroutines.launch

class ServerDetailViewModel(application: Application, private val server: ServerItem) :
    AndroidViewModel(application) {
    private val _presets = MutableLiveData<MutableList<CommandPreset>>()
    val presets: LiveData<MutableList<CommandPreset>> = _presets

    private val _output = MutableLiveData<String>()
    val output: LiveData<String> = _output

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadPresets()
    }

    fun loadPresets() {
        Log.d("ServerDetailVM", "Loading presets for server ${server.id}")
        val list = Prefs.getPresets(getApplication(), server.id)
        _presets.value = list.toMutableList()
    }

    fun addPreset(preset: CommandPreset) {
        val current = _presets.value ?: emptyList()
        val updated = current + preset
        _presets.value = updated.toMutableList()
        Log.d("ServerDetailVM", "Presets now: ${_presets.value}")
        Prefs.savePresets(getApplication(), server.id, updated)
    }

    fun deletePreset(preset: CommandPreset) {
        val current = _presets.value ?: return
        val updated = current.filter { it.name != preset.name }.toMutableList()
        _presets.value = updated
        Prefs.savePresets(getApplication(), server.id, updated)
    }

    fun runPreset(command: String, context: Context) {
        viewModelScope.launch {
            try {
                val session = SSHManager.connect(
                    host = server.host,
                    port = server.port,
                    username = server.username,
                    password = server.password
                )
                val (exitStatus, result) = SSHManager.execCommand(session, command)
                _output.value = result
                session.disconnect()
                Log.d("ServerDetailVM", "_output: $_output")
                if (exitStatus == 0) {
                    SoundPlayer.play(context, R.raw.success)
                    return@launch
                }
                SoundPlayer.play(context, R.raw.error)
            } catch (e: Exception) {
                val msg = when (e) {
                    is SecurityException -> "No Internet Access."
                    is JSchException -> "SSH Error: ${e.message}"
                    else -> "Unknown Error: ${e.message}"
                }
                Log.e("ServerDetailVM", msg, e)
                _output.value = msg
                SoundPlayer.play(context, R.raw.error)
            }
        }
    }
}
