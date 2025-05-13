package com.example.controlify.server_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.controlify.databinding.ItemPresetBinding

class PresetAdapter(
    private val onClick: (CommandPreset) -> Unit
) : ListAdapter<CommandPreset, PresetAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CommandPreset>() {
            override fun areItemsTheSame(a: CommandPreset, b: CommandPreset) =
                a.name == b.name

            override fun areContentsTheSame(a: CommandPreset, b: CommandPreset) =
                a == b
        }
    }

    inner class ViewHolder(private val b: ItemPresetBinding)
        : RecyclerView.ViewHolder(b.root) {
        fun bind(item: CommandPreset) {
            b.tvPresetName.text = item.name
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemPresetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}
