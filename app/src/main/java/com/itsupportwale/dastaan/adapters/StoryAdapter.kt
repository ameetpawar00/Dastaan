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
import com.itsupportwale.dastaan.databinding.RowItemMySubscriptionBinding
import com.itsupportwale.dastaan.databinding.RowItemStoryBinding


class StoryAdapter(activity: Context, arrayList: ArrayList<ResponseHomeData.Story>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<ResponseHomeData.Story>

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
        holder.listBinding.thisWriterName.text = "By "+arrayList[position].writerData!!.name
        holder.listBinding.storyTags.text = arrayList[position].storyTags
        holder.listBinding.storyDate.text = arrayList[position].timestamp
        holder.listBinding.storyReads.text = arrayList[position].view+" Reads"
        holder.listBinding.ratingTxt.text = arrayList[position].ratings

        if(arrayList[position].isFavourite!!)
        {
            holder.listBinding.bookmarked.visibility = View.VISIBLE
            holder.listBinding.bookmarkedNot.visibility = View.GONE
        }else{
            holder.listBinding.bookmarkedNot.visibility = View.VISIBLE
            holder.listBinding.bookmarked.visibility = View.GONE
        }
        Glide.with(activity)
            .load(arrayList[position].photo!![0])
            .error(R.drawable.default_image_icon)
            .into(holder.listBinding.bannerImage)




        /*holder.listBinding.titleTxt.text = arrayList[position].title
        holder.listBinding.price.text =""+activity.resources.getString(R.string.dollar)+" "+ NumberFormat.getNumberInstance(Locale.US).format(arrayList[position].price!!.toDouble())
        holder.listBinding.distanceTxt.text = arrayList[position].propertySize+" "+activity.resources.getString(R.string.square_meter)
        holder.listBinding.propertyAddressTxt.text = arrayList[position].address
        holder.listBinding.ratingTxt.text = arrayList[position].rating
        if(arrayList[position].photo!=null&& arrayList[position].photo!!.isNotEmpty()){
            Glide.with(activity).load(arrayList[position].photo!![0]).error(R.drawable.ic_default_image).into( holder.listBinding.propertyImg)
        }
        holder.listBinding.parentPanel.setOnClickListener{
            if (mItemClickListener != null) {
                mItemClickListener?.onItemListItemClickListener(position, TAB_PROP_MOST)
            }
        }

        if(arrayList[position].type.equals("1")){
            holder.listBinding.propertyType.text  = activity.resources.getString(R.string.rent)
        }else{
            holder.listBinding.propertyType.text  = activity.resources.getString(R.string.sale)
        }*/
    }

    private var mItemClickListener: onRecyclerViewItemClickListener? = null

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface onRecyclerViewItemClickListener {
        fun onItemListItemClickListener(position: Int, tabType: Int)
    }
}

