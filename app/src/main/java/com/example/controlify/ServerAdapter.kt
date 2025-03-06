package com.example.controlify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServerAdapter(private val items: List<ServerItem>) :
    RecyclerView.Adapter<ServerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_server, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageServer: ImageView = itemView.findViewById(R.id.imageServer)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textUpdated: TextView = itemView.findViewById(R.id.textUpdated)

        fun bind(item: ServerItem) {
            imageServer.setImageResource(item.imageResId)
            textTitle.text = item.title
            textUpdated.text = item.updatedInfo
        }
    }
}