package com.example.lostandfound.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfound.MainActivity
import com.example.lostandfound.R
import com.example.lostandfound.modelfiles.ItemsModel

class AdapterItems (var dataModelItemsModel : List<ItemsModel>, var mContext: Context) :
    RecyclerView.Adapter<AdapterItems.RVHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_recyclerview, parent, false)
        return RVHolder(listItem)
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.titleTv.text = dataModelItemsModel[position].itemDescription
        holder.itemMainHolder.setOnClickListener {
            val a = (mContext as MainActivity)
            a.intentToDetails(dataModelItemsModel[position].itemId!!.toLong(),
                dataModelItemsModel[position].itemName!!, dataModelItemsModel[position].itemDescription!!,
                dataModelItemsModel[position].itemLocation!!, dataModelItemsModel[position].itemDate!!,
                dataModelItemsModel[position].itemPhone!!)

        }
    }

    override fun getItemCount(): Int {
        return dataModelItemsModel.size
    }

    class RVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTv: TextView = itemView.findViewById<View>(R.id.titleTv) as TextView
        var itemMainHolder: ConstraintLayout = itemView.findViewById<View>(R.id.itemMainHolder) as ConstraintLayout
    }
}