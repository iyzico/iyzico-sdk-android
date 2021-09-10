package com.android.iyzicosdk.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates


internal abstract class IyziCoBaseAdapter<Data> : RecyclerView.Adapter<IyziCoBaseViewHolder<Data>>() {
    private var onItemClick: ((Data) -> Unit) = {}

    var items: List<Data> by Delegates.observable(emptyList()) { prop, old, new ->
        this@IyziCoBaseAdapter.notifyDataSetChanged()
    }
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IyziCoBaseViewHolder<Data>

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: IyziCoBaseViewHolder<Data>, position: Int) {
        holder.bindItem(items[position], onItemClick)
    }

    fun onItemClick(onClick: ((Data) -> Unit)) {
        this.onItemClick = onClick
    }
}

