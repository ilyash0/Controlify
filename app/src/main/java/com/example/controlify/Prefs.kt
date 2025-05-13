package com.example.controlify

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.controlify.server_detail.CommandPreset
import com.example.controlify.server_list.ServerItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Prefs {
    private const val PREFS_NAME = "controlify_prefs"
    private const val KEY_SERVERS = "servers_list"
    private const val KEY_PRESETS_PREFIX = "presets_"

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveServers(context: Context, servers: List<ServerItem>) {
        val json = Gson().toJson(servers)
        prefs(context).edit()
            .putString(KEY_SERVERS, json)
            .apply()
        Log.d("Prefs", "Save Data: $json")

    }

    fun getServers(context: Context): MutableList<ServerItem> {
        val json = prefs(context).getString(KEY_SERVERS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<ServerItem>>() {}.type
        Log.d("Prefs", "Get Data: $json")
        return Gson().fromJson(json, type)
    }

    fun savePresets(context: Context, serverId: String, presets: List<CommandPreset>)
    {
        val key = KEY_PRESETS_PREFIX + serverId
        val json = Gson().toJson(presets)
        prefs(context).edit()
            .putString(key, json)
            .apply()
        Log.d("Prefs", "Saved presets for $serverId: $json")
    }

    fun getPresets(context: Context, serverId: String): MutableList<CommandPreset>
    {
        val key = KEY_PRESETS_PREFIX + serverId
        val json = prefs(context).getString(key, null)
            ?: return mutableListOf()
        val type = object : TypeToken<MutableList<CommandPreset>>() {}.type
        Log.d("Prefs", "Loaded presets for $serverId: $json")
        return Gson().fromJson(json, type)
    }
}