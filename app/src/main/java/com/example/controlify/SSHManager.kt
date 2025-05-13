package com.example.controlify

import android.content.ContentValues.TAG
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jcraft.jsch.ChannelExec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import android.util.Log
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Logger
import kotlinx.coroutines.delay

object SSHManager {
    private const val TIMEOUT = 10_000

    init {
        JSch.setLogger(object : Logger {
            override fun isEnabled(level: Int) = true

            override fun log(level: Int, message: String) {
                val lvl = when (level) {
                    Logger.DEBUG   -> Log.DEBUG
                    Logger.INFO    -> Log.INFO
                    Logger.WARN    -> Log.WARN
                    Logger.ERROR,
                    Logger.FATAL   -> Log.ERROR
                    else            -> Log.VERBOSE
                }
                Log.println(lvl, TAG, "[JSch] $message")
            }
        })
    }

    suspend fun connect(
        host: String,
        port: Int = 22,
        username: String,
        password: String
    ): Session = withContext(Dispatchers.IO) {
        Log.d(TAG, "Connecting to $username@$host:$port ...")

        val jsch = JSch()
        val session = jsch.getSession(username, host, port)
        session.setPassword(password)
        session.setConfig(
            "StrictHostKeyChecking",
            "no"
        )
        session.timeout = TIMEOUT
        try {
            session.connect()
            Log.d(TAG, "Session connected: isConnected=${session.isConnected}")
        } catch (e: JSchException) {
            Log.e(TAG, "SSH connect failed: ${e.message}", e)
            throw e
        }
        session
    }

    suspend fun execCommand(session: Session, command: String): Pair<Int, String> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Executing command: $command")

            val channel = session.openChannel("exec") as ChannelExec
            channel.setCommand(command)

            val outputStream = ByteArrayOutputStream()
            channel.outputStream = outputStream

            channel.connect()

            while (!channel.isClosed) {
                delay(100)
            }

            val result = outputStream.toString(Charsets.UTF_8.name())
            val exitStatus = channel.exitStatus

            Log.d(TAG, "Command output: $result")
            Log.d(TAG, "Exit status: $exitStatus")

            channel.disconnect()

            Pair(exitStatus, result)
        }
}
