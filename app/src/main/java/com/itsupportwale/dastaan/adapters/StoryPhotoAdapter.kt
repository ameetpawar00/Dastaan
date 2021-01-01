package com.itsupportwale.dastaan.adapters

import com.itsupportwale.dastaan.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itsupportwale.dastaan.databinding.RowItemStoryBinding
import com.itsupportwale.dastaan.utility.TAB_PROP_LATEST

import kotlin.collections.ArrayList

class StoryPhotoAdapter(activity: Context, arrayList:ArrayList<String>) : RecyclerView.Adapter<StoryPhotoAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<String>

    init {
        this.activity = activity
        this.arrayList = arrayList
    }

    class ListViewHolder(listBinding: RowItemStoryBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowItemStoryBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowItemStoryBinding>(LayoutInflater.from(parent.context), R.layout.row_item_story_photo ,parent ,false  )

        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //holder.listBinding.thisText.text = arrayList[position]

      /*     Glide.with(activity).load(arrayList[position])
                   .into(holder.listBinding.thisImage)

        holder.listBinding.parentPanel.setOnClickListener{
            if (mItemClickListener != null) {
                mItemClickListener?.onItemListItemClickListener( position, TAB_PROP_LATEST)
            }
        }*/
    }

    private var mItemClickListener: onRecyclerViewItemClickListener? = null

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface onRecyclerViewItemClickListener {
        fun onItemListItemClickListener( position: Int,tabType: Int)
    }
}