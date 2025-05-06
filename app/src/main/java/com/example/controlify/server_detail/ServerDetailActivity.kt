package com.example.controlify.server_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlify.databinding.ActivityServerDetailBinding
import com.example.controlify.server_list.ServerItem

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
    private val vm: ServerDetailViewModel by viewModels {
        ServerDetailViewModelFactory(server)
    }
    private lateinit var adapter: PresetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.apply {
            title = server.name
            setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        // RecyclerView
        adapter = PresetAdapter { preset ->
            // TODO: выполнить команды
        }
        binding.rvPresets.layoutManager = LinearLayoutManager(this)
        binding.rvPresets.adapter = adapter

        // Наблюдаем за списком пресетов
        vm.presets.observe(this) { list ->
            adapter.submitList(list)
        }
        vm.loadPresets()

        // FAB добавить пресет
        binding.fabAddPreset.setOnClickListener {
            // TODO: показать диалог создания команд
        }
    }
}
