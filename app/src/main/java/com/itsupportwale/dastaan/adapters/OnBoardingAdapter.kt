package com.itsupportwale.dastaan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.OnBoardingModel


internal class OnBoardingAdapter(mContext: Context, items: ArrayList<OnBoardingModel>) : PagerAdapter() {
    private val mContext: Context
    var onBoardItems: ArrayList<OnBoardingModel> = ArrayList()
    override fun getCount(): Int {
        return onBoardItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            LayoutInflater.from(mContext).inflate(R.layout.onboarding_item, container, false)
        val item: OnBoardingModel = onBoardItems[position]
        val imageView: ImageView = itemView.findViewById(R.id.ivOnboard) as ImageView
        imageView.setImageResource(item.imageID)
        /*val imageIconView: ImageView = itemView.findViewById(R.id.ivOnboardIcon) as ImageView
        imageIconView.setImageResource(item.imageIconID)*/
        val tv_title = itemView.findViewById(R.id.tvHeader) as TextView
        tv_title.setText(item.title)
        val tv_content = itemView.findViewById(R.id.tvDesc) as TextView
        tv_content.setText(item.description)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    init {
        this.mContext = mContext
        onBoardItems = items
    }
}