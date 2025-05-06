package com.example.controlify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.controlify.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val vm: ServersViewModel by viewModels { ViewModelFactory(this) }
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

        adapter = ServersAdapter(vm.servers.value!!) { server ->
            // обработка клика по серверу
        }
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        AlertDialog.Builder(this)
            .setTitle("New Server")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newItem = ServerItem(
                    id = UUID.randomUUID().toString(),
                    name = etName.text.toString(),
                    updatedInfo = "Updated today",
                    imageResId = R.drawable.ic_launcher_background,
                    host = etHost.text.toString(),
                    port = etPort.text.toString().toIntOrNull() ?: 22
                )
                vm.addServer(newItem)
                adapter.insert(newItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}