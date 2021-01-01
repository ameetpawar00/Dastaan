package com.itsupportwale.dastaan.adapters

import com.itsupportwale.dastaan.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itsupportwale.dastaan.beans.GetUserProfileData
import com.itsupportwale.dastaan.databinding.RowRecentlyViewedBinding
import com.itsupportwale.dastaan.utility.TAB_PROP_MOST
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RecentlyViewedAdapter(activity: Context, arrayList:ArrayList<String>) : RecyclerView.Adapter<RecentlyViewedAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<String>

    init {
        this.activity = activity
        this.arrayList = arrayList
    }

    class ListViewHolder(listBinding: RowRecentlyViewedBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowRecentlyViewedBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowRecentlyViewedBinding>(LayoutInflater.from(parent.context), R.layout.row_recently_viewed ,parent ,false  )

        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //holder.listBinding.thisText.text = arrayList[position]

   /*     holder.listBinding.titleTxt.text = arrayList[position].title
        holder.listBinding.price.text =""+activity.resources.getString(R.string.dollar)+" "+ NumberFormat.getNumberInstance(Locale.US).format(arrayList[position].price!!.toDouble())
        holder.listBinding.distanceTxt.text = arrayList[position].propertySize+" "+activity.resources.getString(R.string.square_meter)
        holder.listBinding.thisAssistantRatingStar.score = arrayList[position].rating!!
        if(arrayList[position].photo!=null&& arrayList[position].photo!!.isNotEmpty()){
            Glide.with(activity).load(arrayList[position].photo!![0]).error(R.drawable.ic_default_image).into( holder.listBinding.propertyImg)
        }
        holder.listBinding.parentPanel.setOnClickListener{
            if (mItemClickListener != null) {
                mItemClickListener?.onItemListItemClickListener(position)
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
        fun onItemListItemClickListener( position: Int)
    }
}