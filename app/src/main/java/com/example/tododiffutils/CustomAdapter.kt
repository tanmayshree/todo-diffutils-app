package com.practice.project1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tododiffutils.R

class CustomAdapter(
    private var itemList: ArrayList<String>,
    context: Context,
    private val clickEvents: AdapterClickEvents
) :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    var adapterList = ArrayList<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomAdapter.CustomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_list_item, parent, false)
        return CustomViewHolder(view, clickEvents)
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int,
    ) {
        holder.onBind(holder.adapterPosition, itemList[position])
    }

    fun setMediaList(newList: ArrayList<String>) {
        var diffUtil = DiffUtilCustom(adapterList, newList)
        var diffRes = DiffUtil.calculateDiff(diffUtil)
        adapterList.clear()
        adapterList.addAll(newList)
        diffRes.dispatchUpdatesTo(this)
    }


    class CustomViewHolder(
        view: View, private val clickEvents: AdapterClickEvents
    ) :
        RecyclerView.ViewHolder(view) {
        private val customText: TextView = view.findViewById(R.id.customText)
        private val customEdit: ImageView = view.findViewById(R.id.customEdit)
        private val customDelete: ImageView = view.findViewById(R.id.customDelete)

        fun onBind(position: Int, text: String) {
            customText.text = text + " $position"
            customDelete.setOnClickListener {
                clickEvents.deleteClickCallback(position)
            }
            customEdit.setOnClickListener {
                clickEvents.editClickCallback(position)
            }
        }
    }
}

class DiffUtilCustom(val oldList: ArrayList<String>, val newList: ArrayList<String>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}