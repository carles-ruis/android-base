package com.carles.base.ui.poilist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carles.base.R
import com.carles.base.ui.DebounceClickListener
import com.carles.base.ui.inflate
import com.carles.base.domain.Poi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_poi_list.*

class PoiListAdapter(onPoiClicked: (Poi) -> Unit) : RecyclerView.Adapter<PoiListAdapter.ViewHolder>() {

    private val items = ArrayList<Poi>()
    private val debouncePoiClickListener = DebounceClickListener(onPoiClicked)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_poi_list))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.onBindView(items.get(position)) }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<Poi>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            containerView.setOnClickListener { debouncePoiClickListener.onClick(items.get(adapterPosition)) }
        }

        fun onBindView(item: Poi) {
            poilist_item_title_textview.text = item.title
        }
    }
}