package com.itsupportwale.dastaan.adapters

import com.itsupportwale.dastaan.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.itsupportwale.dastaan.beans.GetNotificationData
import com.itsupportwale.dastaan.databinding.RowItemNotificationBinding
import kotlin.collections.ArrayList

class NotificationDataAdapter(activity: Context, arrayList:ArrayList<GetNotificationData.Datum>) : RecyclerView.Adapter<NotificationDataAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<GetNotificationData.Datum>

    init {
        this.activity = activity
        this.arrayList = arrayList
    }

    class ListViewHolder(listBinding: RowItemNotificationBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowItemNotificationBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowItemNotificationBinding>(LayoutInflater.from(parent.context), R.layout.row_item_notification ,parent ,false  )

        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.listBinding.title.text = arrayList[position].title
        holder.listBinding.description.text = arrayList[position].body

        holder.listBinding.parentPanel.setOnClickListener{
            if (mItemClickListener != null) {
                mItemClickListener?.onItemListItemClickListener( position)
            }
        }
    }

    private var mItemClickListener: onRecyclerViewItemClickListener? = null

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface onRecyclerViewItemClickListener {
        fun onItemListItemClickListener( position: Int)
    }
}