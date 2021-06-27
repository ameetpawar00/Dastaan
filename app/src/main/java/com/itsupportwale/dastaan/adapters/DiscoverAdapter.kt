package com.itsupportwale.dastaan.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.ResponseHomeData
import com.itsupportwale.dastaan.databinding.RowItemDiscoverBinding
import com.itsupportwale.dastaan.databinding.RowItemMySubscriptionBinding
import org.jetbrains.anko.textColor


class DiscoverAdapter(activity: Context, arrayList: ArrayList<ResponseHomeData.Genre>) : RecyclerView.Adapter<DiscoverAdapter.ListViewHolder>() {

    var activity: Context
    var arrayList:ArrayList<ResponseHomeData.Genre>

    init {
        this.activity = activity
        this.arrayList = arrayList
    }

    class ListViewHolder(listBinding: RowItemDiscoverBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowItemDiscoverBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowItemDiscoverBinding>(LayoutInflater.from(parent.context), R.layout.row_item_discover, parent, false)
        return ListViewHolder(listBinding)
    }

    override fun getItemCount(): Int {
        return if (null != arrayList) arrayList.size else 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        /*if(position==0)
        {
            arrayList[position].isSelected == true
        }*/

        if(arrayList[position].isSelected!!)
        {
            setSelected(holder)
        }else{
            setNotSelected(holder)
        }

        holder.listBinding.titleTxt.text = arrayList[position].name
        holder.listBinding.count.text = "("+arrayList[position].storyCount.toString()+")"
        if(arrayList[position].storyCount!! > 0)
        {
            holder.listBinding.parentPanel.setOnClickListener{
                if (mItemClickListener != null) {
                    mItemClickListener?.onItemListItemClickListenerDiscover(position)
                }
            }
        }else{
                holder.listBinding.parentPanel.background = activity.resources.getDrawable(R.drawable.grey_capsule_border)
                holder.listBinding.titleTxt.textColor = activity.resources.getColor(R.color.grey_live)
        }

        /*
        holder.listBinding.titleTxt.text = arrayList[position].title
        holder.listBinding.price.text =""+activity.resources.getString(R.string.dollar)+" "+ NumberFormat.getNumberInstance(Locale.US).format(arrayList[position].price!!.toDouble())
        holder.listBinding.distanceTxt.text = arrayList[position].propertySize+" "+activity.resources.getString(R.string.square_meter)
        holder.listBinding.propertyAddressTxt.text = arrayList[position].address
        holder.listBinding.ratingTxt.text = arrayList[position].rating
        if(arrayList[position].photo!=null&& arrayList[position].photo!!.isNotEmpty()){
            Glide.with(activity).load(arrayList[position].photo!![0]).error(R.drawable.ic_default_image).into( holder.listBinding.propertyImg)
        }


        if(arrayList[position].type.equals("1")){
            holder.listBinding.propertyType.text  = activity.resources.getString(R.string.rent)
        }else{
            holder.listBinding.propertyType.text  = activity.resources.getString(R.string.sale)
        }*/
    }

    private fun setSelected(holder: ListViewHolder) {
        holder.listBinding.parentPanel.background = activity.resources.getDrawable(R.drawable.black_capsule)
        holder.listBinding.titleTxt.textColor = activity.resources.getColor(R.color.white)
        holder.listBinding.count.textColor = activity.resources.getColor(R.color.white)
    }

    private fun setNotSelected(holder: ListViewHolder) {
        holder.listBinding.parentPanel.background = activity.resources.getDrawable(R.drawable.black_capsule_border)
        holder.listBinding.titleTxt.textColor = activity.resources.getColor(R.color.black)
        holder.listBinding.count.textColor = activity.resources.getColor(R.color.black)
    }

    private var mItemClickListener: onRecyclerViewItemClickListener? = null

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface onRecyclerViewItemClickListener {
        fun onItemListItemClickListenerDiscover(position: Int)
    }
}


