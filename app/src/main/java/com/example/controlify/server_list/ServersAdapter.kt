package com.example.controlify.server_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.controlify.R

class ServersAdapter(private val items: MutableList<ServerItem>, private val onClick: (ServerItem) -> Unit) :
    RecyclerView.Adapter<ServersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_server, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<ServerItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun insert(item: ServerItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageServer: ImageView = itemView.findViewById(R.id.imageServer)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)

        fun bind(item: ServerItem) {
            imageServer.setImageResource(item.imageResId)
            textTitle.text = item.name
            itemView.setOnClickListener { onClick(item) }
        }
    }
}