package dev.amin.tagadapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.amin.tagadapter.databinding.RowTagBinding
import kotlinx.android.synthetic.main.row_tag.view.*
import kotlin.properties.Delegates

class TagAdapter(
    activity: Activity,
    private var tagList: ArrayList<Tag>,
    private var commingFrom: Int
) : RecyclerView.Adapter<TagAdapter.ListViewHolder>() {

    var activity: Activity
    init {
        this.activity = activity
    }
    /***
     * Measuring Helper which is used to measure each row of the recyclerView
     */
    private val measureHelper = MeasureHelper(this, tagList.size)

    /***
     * Attached RecyclerView to the Adapter
     */
    private var recyclerView: RecyclerView? = null

    /***
     * First step is to get the width of the recyclerView then
     * Proceed to measuring the holders.
     *
     * Is RecyclerView done measuring.
     */
    private var ready = false

    /***
     * Determines when the measuring of all the cells is done.
     * If the newVal is true the adapter should be updated.
     */
    var measuringDone by Delegates.observable(false) { _, _, newVal ->
        if (newVal)
            update()
    }

    /***
     * Called to update the adapter with the new LayoutManager.
     */
    private fun update() {

        recyclerView ?: return

        recyclerView?.apply {

            visibility = View.VISIBLE

            // Get the list of sorted items from measureHelper
            tagList = measureHelper.getItems()

            // Get the list of sorted spans from measureHelper
            layoutManager = MultipleSpanGridLayoutManager(context, MeasureHelper.SPAN_COUNT, measureHelper.getSpans())
        }
    }

    /***
     * When recyclerView is attached measure the width and calculate the baseCell on measureHelper.
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView.apply {

            visibility = View.INVISIBLE

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    // Prevent the multiple calls
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Measure the BaseCell on MeasureHelper.
                    measureHelper.measureBaseCell(recyclerView.width)

                    ready = true

                    // NotifyDataSet because itemCount value needs to update.
                    notifyDataSetChanged()
                }
            })
        }
    }


    class ListViewHolder(listBinding: RowTagBinding) : RecyclerView.ViewHolder(listBinding.root)
    {
        var listBinding: RowTagBinding
        init {
            this.listBinding = listBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val listBinding = DataBindingUtil.inflate<RowTagBinding>(LayoutInflater.from(parent.context), R.layout.row_tag ,parent ,false  )
        return ListViewHolder(
            listBinding
        )
    }


    override fun getItemCount() = if (ready) tagList.size else 0

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val tag = tagList[position]

        // Determine if the MeasureHelper is done measuring if not holder should be measured.
        val shouldMeasure = measureHelper.shouldMeasure()

       // holder.setData(activity,tag, shouldMeasure)


        if(tag.isSelected!!){
            highlightView(activity,holder.listBinding)
        }else{
            unhighlightView(activity,holder.listBinding)
        }

        holder.listBinding.rowTitle.text = tag.title
        holder.listBinding.rowTitle.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT

        if (!shouldMeasure)
            holder.listBinding.rowTitle.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

        if (shouldMeasure)
            measureHelper.measure(holder, tag)

        holder.listBinding.rowTitle.setOnClickListener{
            if (mItemClickListener != null) {
                mItemClickListener?.onItemClickListener( position,tagList,commingFrom)
            }
        }

    }

    fun highlightView(activity:Activity,itemView: RowTagBinding) {
        itemView.rowTitle.setTextColor(activity.resources.getColor(R.color.colorWhite))
        itemView.rowTitle.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.title_cornered_normal))
    }

    fun unhighlightView(activity:Activity,itemView: RowTagBinding) {
        itemView.rowTitle.setTextColor(activity.resources.getColor(R.color.colorPrimary))
        itemView.rowTitle.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.title_cornered_border))

    }

    private var mItemClickListener: onRecyclerViewItemClickListener? = null

    fun setOnItemClickListener(mItemClickListener: onRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface onRecyclerViewItemClickListener {
        fun onItemClickListener( position: Int,tagList: ArrayList<Tag>,commingFrom:Int)
    }
    /*class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(activity:Activity,tag: Tag, shouldMeasure: Boolean) {

            if(tag.isSelected!!){
                highlightView(activity,itemView)
            }else{
                unhighlightView(activity,itemView)
            }

            itemView.rowTitle.apply {

                text = tag.title
                *//* set the height to normal, because in the measureHelper in order to fit
                    as much holders as possible we shrink the view to height of 1 *//*
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            }

            *//* if the measuring is done set the width to fill the whole cell to avoid unwanted
                empty spaces between the cells *//*
            if (!shouldMeasure)
                itemView.rowTitle.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT


            itemView.rowTitle.setOnClickListener{
                if(tag.isSelected!!)
                {
                    tag.isSelected = false
                }else{
                    tag.isSelected = true
                }

                //notifyDataSetChanged()
            }

        }

         fun highlightView(activity:Activity,itemView: View) {
            itemView.rowTitle.setTextColor(activity.resources.getColor(R.color.colorWhite))

        }

         fun unhighlightView(activity:Activity,itemView: View) {
            itemView.rowTitle.setTextColor(activity.resources.getColor(R.color.colorPrimary))

        }



    }*/


}