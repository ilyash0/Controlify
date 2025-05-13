package com.example.controlify.server_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlify.R
import com.example.controlify.databinding.ActivityServerDetailBinding
import com.example.controlify.server_list.ServerItem
import com.example.controlify.server_list.ServersViewModel
import com.google.android.material.snackbar.Snackbar

class ServerDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SERVER = "extra_server"
        fun start(ctx: Context, server: ServerItem) {
            val i = Intent(ctx, ServerDetailActivity::class.java)
            i.putExtra(EXTRA_SERVER, server)
            ctx.startActivity(i)
        }
    }

    private lateinit var binding: ActivityServerDetailBinding
    private val server by lazy { intent.getParcelableExtra<ServerItem>(EXTRA_SERVER)!! }
    private val vm: ServerDetailViewModel by viewModels { ServerDetailViewModelFactory(application, server) }
    private val viewModel: ServersViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
    private lateinit var adapter: PresetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.apply {
            title = server.name
            setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = PresetAdapter { preset ->
            vm.runPreset(preset.command, this)
        }
        binding.rvPresets.layoutManager = LinearLayoutManager(this)
        binding.rvPresets.adapter = adapter

        vm.presets.observe(this) { list ->
            Log.d("ServerDetailActivity", "Observe got: $list")
            adapter.submitList(list.toList())
        }
        vm.loadPresets()

        binding.fabAddPreset.setOnClickListener {
            showAddPresetDialog()
        }

        vm.output.observe(this) { text ->
            val message = text.takeUnless { it.isNullOrBlank() }
                ?: "No Response"
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_server, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteServer(server)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddPresetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_preset, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etCommand = dialogView.findViewById<EditText>(R.id.etCommand)

        AlertDialog.Builder(this)
            .setTitle("New Command")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newItem = CommandPreset(
                    name = etName.text.toString(),
                    command = etCommand.text.toString(),
                )
                vm.addPreset(newItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
