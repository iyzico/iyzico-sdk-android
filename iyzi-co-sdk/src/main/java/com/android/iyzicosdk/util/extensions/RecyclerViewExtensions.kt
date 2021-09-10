package com.android.iyzicosdk.util.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


internal fun RecyclerView.vertical(
    adapter: RecyclerView.Adapter<*>,
    layoutManager: RecyclerView.LayoutManager? = null
) {
    this.run {
        this.layoutManager = layoutManager ?: LinearLayoutManager(context)
        this.adapter = adapter
    }

}