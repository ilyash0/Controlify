package com.example.controlify.server_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.controlify.R
import com.example.controlify.databinding.ActivityMainBinding
import com.example.controlify.server_detail.ServerDetailActivity
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val vm: ServersViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
        binding.fab.setOnClickListener {
            showAddServerDialog()
        }


        recyclerView = findViewById(R.id.recyclerViewServers)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        adapter = ServersAdapter { server ->
            Log.d("ServersAdapter", "Click!")
            val intent = Intent(this, ServerDetailActivity::class.java).apply {
                putExtra(ServerDetailActivity.EXTRA_SERVER, server)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        vm.servers.observe(this) { list ->
            adapter.submitList(list.toList())
        }
        vm.loadServers()

        val searchView = this.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = vm.servers.value?.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                } ?: emptyList()
                adapter.submitList(filteredList)
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showAddServerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_server, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etHost = dialogView.findViewById<EditText>(R.id.etHost)
        val etPort = dialogView.findViewById<EditText>(R.id.etPort)
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)

        AlertDialog.Builder(this)
            .setTitle("New Server")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newItem = ServerItem(
                    id = UUID.randomUUID().toString(),
                    name = etName.text.toString(),
                    imageResId = R.drawable.ic_launcher_background,
                    host = etHost.text.toString(),
                    port = etPort.text.toString().toIntOrNull() ?: 22,
                    username = etUsername.text.toString(),
                    password = etPassword.text.toString()
                )
                vm.addServer(newItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        vm.loadServers()
    }
}