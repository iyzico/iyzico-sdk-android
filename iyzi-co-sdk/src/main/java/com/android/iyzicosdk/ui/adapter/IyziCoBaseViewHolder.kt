package com.android.iyzicosdk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

internal abstract class IyziCoBaseViewHolder<Data> : RecyclerView.ViewHolder {

    constructor(parent: ViewGroup, @LayoutRes layoutId: Int) : this(
        LayoutInflater.from(parent.context).inflate(
            layoutId,
            parent,
            false
        )
    )

    constructor(itemView: View) : super(itemView)

    abstract fun bind(item: Data)

    internal fun bindItem(item: Data, onItemClick: (Data) -> Unit) {
        bind(item)
        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}