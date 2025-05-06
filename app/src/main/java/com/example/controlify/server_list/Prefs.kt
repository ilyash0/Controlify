package com.example.controlify.server_list

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Prefs {
    private const val PREFS_NAME = "controlify_prefs"
    private const val KEY_SERVERS = "servers_list"

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
}