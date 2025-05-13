package com.example.controlify.server_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.controlify.R

class ServersAdapter(private val onClick: (ServerItem) -> Unit) :
    ListAdapter<ServerItem, ServersAdapter.ViewHolder>(ServerDiffCallback()) {

    class ServerDiffCallback : DiffUtil.ItemCallback<ServerItem>() {
        override fun areItemsTheSame(oldItem: ServerItem, newItem: ServerItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ServerItem, newItem: ServerItem): Boolean {
            return oldItem == newItem
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_server, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}