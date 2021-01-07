package com.itsupportwale.dastaan.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.ResponseHomeData
import com.itsupportwale.dastaan.beans.ResponseMyStory
import com.itsupportwale.dastaan.databinding.RowItemMySubscriptionBinding
import com.itsupportwale.dastaan.databinding.RowItemStoryBinding
import com.itsupportwale.dastaan.utility.CLICK_FROM_FAV
import com.itsupportwale.dastaan.utility.CLICK_FROM_NOT_FAV
import com.itsupportwale.dastaan.utility.CLICK_FROM_PARENT


class MyStoryAdapter(activity: Context, arrayList: ArrayList<ResponseMyStory.Story>) : RecyclerView.Adapter<MyStoryAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<ResponseMyStory.Story>

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
        val listBinding = DataBindingUtil.inflate<RowItemStoryBinding>(LayoutInflater.from(parent.context), R.layout.row_item_story, parent, false)
        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)
        holder.listBinding.bannerImage.setColorFilter(filter)

        holder.listBinding.thisTitle.text = arrayList[position].title
        holder.listBinding.thisWriterName.visibility = View.GONE
        holder.listBinding.storyTags.text = arrayList[position].storyTags
        holder.listBinding.storyDate.text = arrayList[position].timestamp
        holder.listBinding.storyReads.text = arrayList[position].view+" Reads"
        holder.listBinding.ratingTxt.text = arrayList[position].ratings

        holder.listBinding.bookmarked.visibility = View.GONE
        holder.listBinding.bookmarkedNot.visibility = View.GONE

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
        fun onItemListItemClickListener(position: Int)
    }
}


