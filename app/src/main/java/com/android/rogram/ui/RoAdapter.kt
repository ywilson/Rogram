package com.android.rogram.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.rogram.R
import com.android.rogram.data.RoData
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.view.*


class RoAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<RoAdapter.RoViewHolder>() {

    private val dataList = mutableListOf<RoData>()

    fun addData(data: List<RoData>) {
        val oldPosition = data.size
        dataList.addAll(data)

        notifyItemRangeChanged(oldPosition, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_main, parent, false)
        return RoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class RoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: RoData) {
            itemView.name.text = data.title
            val id = data.id
            itemView.card.transitionName = "$id"
            Picasso.get().load(data.thumbnailUrl)
                    .error(R.drawable.ic_baseline_error_24)
                     .placeholder(R.drawable.ic_baseline_image_24)
                .into(itemView.img)
            itemView.setOnClickListener {
                onClickListener.onClick(data, it.card, it.name)
            }
        }
    }

    open class OnClickListener(val clickListener: (RoData, MaterialCardView, TextView) -> Unit) {
        fun onClick(
            roData: RoData,
            img: MaterialCardView,
            title: TextView
        ) = clickListener(roData, img, title)
    }
}