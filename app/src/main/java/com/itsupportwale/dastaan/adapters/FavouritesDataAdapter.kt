package com.itsupportwale.dastaan.adapters

import com.itsupportwale.dastaan.R
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itsupportwale.dastaan.beans.GetFavData
import com.itsupportwale.dastaan.beans.ResponseBookmarkData
import com.itsupportwale.dastaan.databinding.RowFavouritesDataBinding
import com.itsupportwale.dastaan.utility.CLICK_FROM_FAV
import com.itsupportwale.dastaan.utility.CLICK_FROM_NOT_FAV
import com.itsupportwale.dastaan.utility.CLICK_FROM_PARENT
import com.itsupportwale.dastaan.utility.TAB_PROP_MOST
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class FavouritesDataAdapter(activity: Context, arrayList:ArrayList<ResponseBookmarkData.Datum>) : RecyclerView.Adapter<FavouritesDataAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<ResponseBookmarkData.Datum>

    init {
        this.activity = activity
        this.arrayList = arrayList
    }

    class ListViewHolder(listBinding: RowFavouritesDataBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowFavouritesDataBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowFavouritesDataBinding>(LayoutInflater.from(parent.context), R.layout.row_favourites_data ,parent ,false  )

        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)
        holder.listBinding.bannerImage.setColorFilter(filter)
        holder.listBinding.thisTitle.text = arrayList[position].title
        holder.listBinding.thisWriterName.text = "By "+arrayList[position].writerData!!.name
        holder.listBinding.storyTags.text = arrayList[position].storyTags
        holder.listBinding.storyDate.text = arrayList[position].timestamp
        holder.listBinding.storyReads.text = arrayList[position].view+" Reads"
        holder.listBinding.ratingTxt.text = arrayList[position].ratings

        Glide.with(activity)
            .load(arrayList[position].photo!![0])
            .error(R.drawable.default_image_icon)
            .into(holder.listBinding.bannerImage)

        holder.listBinding.parentPanel.setOnClickListener{
            if (mItemClickListener != null) {
                mItemClickListener?.onItemListItemClickListener(position)
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