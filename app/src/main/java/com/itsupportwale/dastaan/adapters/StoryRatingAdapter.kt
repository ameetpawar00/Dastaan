package com.itsupportwale.dastaan.adapters

import com.itsupportwale.dastaan.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.itsupportwale.dastaan.beans.GetStoryDetailsModel
import com.itsupportwale.dastaan.beans.ResponseStoryDetailsData
import com.itsupportwale.dastaan.databinding.RowItemPropertyRatingBinding

import kotlin.collections.ArrayList

class StoryRatingAdapter(activity: Context, arrayList:ArrayList<ResponseStoryDetailsData.RatingDatum>) : RecyclerView.Adapter<StoryRatingAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<ResponseStoryDetailsData.RatingDatum>

    init {
        this.activity = activity
        this.arrayList = arrayList
    }

    class ListViewHolder(listBinding: RowItemPropertyRatingBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowItemPropertyRatingBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowItemPropertyRatingBinding>(LayoutInflater.from(parent.context), R.layout.row_item_property_rating ,parent ,false  )

        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.listBinding.reviewTxt.text = arrayList[position].review
        holder.listBinding.thisRatingBar.score = arrayList[position].rating!!
    }

    private var mItemClickListener: onRecyclerViewItemClickListener? = null

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface onRecyclerViewItemClickListener {
        fun onItemListItemClickListener( position: Int,tabType: Int)
    }
}