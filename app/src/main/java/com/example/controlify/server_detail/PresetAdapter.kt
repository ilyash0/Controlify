package com.example.controlify.server_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.controlify.databinding.ItemPresetBinding

class PresetAdapter(
    private val onClick: (CommandPreset) -> Unit
) : ListAdapter<CommandPreset, PresetAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object: DiffUtil.ItemCallback<CommandPreset>() {
            override fun areItemsTheSame(a: CommandPreset, b: CommandPreset) = a.name == b.name
            override fun areContentsTheSame(a: CommandPreset, b: CommandPreset) = a == b
        }
    }

    inner class VH(val b: ItemPresetBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: CommandPreset) {
            b.tvPresetName.text = p.name
            b.root.setOnClickListener { onClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemPresetBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))
}