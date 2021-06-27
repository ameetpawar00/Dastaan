package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.*
import com.itsupportwale.dastaan.beans.GetNotificationData
import com.itsupportwale.dastaan.beans.ResponseHomeData
import com.itsupportwale.dastaan.beans.ResponseMyStory
import com.itsupportwale.dastaan.beans.ResponseMySubscription
import com.itsupportwale.dastaan.databinding.ActivityMySubscriptionBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_USER_ID
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import io.reactivex.disposables.CompositeDisposable

class MySubscriptionActivity : BaseActivity(), View.OnClickListener, MySubscriptionsAdapter.onRecyclerViewItemClickListener{
    lateinit var activityMySubscriptionBinding: ActivityMySubscriptionBinding
    private val mySubscriptionDataArray: ArrayList<ResponseMySubscription.Story> = ArrayList()
    lateinit var storyAdapter: MySubscriptionsAdapter

    var pageIndex = 1


    val gson: Gson = Gson()

    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMySubscriptionBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_subscription)
        userPreference = UserPreference.getInstance(this)

        val pullToRefresh: SwipeRefreshLayout = activityMySubscriptionBinding.pullToRefresh
        pullToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                initView()
                pullToRefresh.setRefreshing(false)
            }
        })

        initView()
    }

    fun initView()
    {
        activityMySubscriptionBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityMySubscriptionBinding.topNavBar.notificationCount.visibility = View.GONE
        activityMySubscriptionBinding.topNavBar.menuIcon.visibility = View.GONE
        activityMySubscriptionBinding.topNavBar.backIcon.visibility = View.VISIBLE
        activityMySubscriptionBinding.topNavBar.backIcon.setOnClickListener(this)
        activityMySubscriptionBinding.topNavBar.navTitle.text = getString(R.string.mySubscriptions)
        activityMySubscriptionBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
        activityMySubscriptionBinding.topNavBar.notificationIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))

        setAdapters()
        callGetMySubscriptionsApi()

    }




    private var linearLayoutManager: LinearLayoutManager? = null
    private fun setAdapters() {
        linearLayoutManager = LinearLayoutManager(this)
        activityMySubscriptionBinding.storyRecyclerView.setNestedScrollingEnabled(false)
        storyAdapter = MySubscriptionsAdapter(this, mySubscriptionDataArray)
        activityMySubscriptionBinding.storyRecyclerView.layoutManager = linearLayoutManager
        activityMySubscriptionBinding.storyRecyclerView.adapter = storyAdapter
        storyAdapter.setOnItemClickListener(this)
        storyAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        callGetMySubscriptionsApi()
    }

    private fun callGetMySubscriptionsApi() {

        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_MY_SUBSCRIPTION_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_PAGE, pageIndex)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(
            this,
            UrlManager.MAIN_URL,
            UrlManager.GET_MY_SUBSCRIPTION_METHOD_NAME,
            params,
            commonModel
        )
        Log.d("paramsparams", API.toBase64(jsObj.toString()))
    }


    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", result.toString())
        if(apiName.equals(UrlManager.GET_MY_SUBSCRIPTION_METHOD_NAME))
        {
            Log.d("modelmodel", result.toString())
            val model: ResponseMySubscription = getGsonAsConvert().fromJson(
                result,
                ResponseMySubscription::class.java
            )
            if (model.status!!) {

                if(pageIndex == 1)
                {
                    mySubscriptionDataArray.clear()
                }

                mySubscriptionDataArray.addAll(model.data!!.story!!)
                storyAdapter.notifyDataSetChanged()

                if(mySubscriptionDataArray.size>0)
                {
                    activityMySubscriptionBinding.noDataAvailable.visibility = View.GONE
                    activityMySubscriptionBinding.storyRecyclerView.visibility = View.VISIBLE
                }
            }else{
                showSnackBar(
                    activityMySubscriptionBinding.storyRecyclerView,
                    resources.getString(R.string.no_data_available)
                )
            }
        }
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showSnackBar(
            activityMySubscriptionBinding.storyRecyclerView,
            resources.getString(R.string.no_data_available)
        )
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backIcon -> {
                finish()
            }
        }
    }

    override fun onItemListItemClickListener(position: Int, tabType: Int) {
        if(tabType== CLICK_FROM_PARENT)
        {
            val intent = Intent(this, StoryDetailsActivity::class.java)
            intent.putExtra(UrlManager.PARAM_STORY_ID,  mySubscriptionDataArray[position].id)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }else if(tabType== CLICK_FROM_FAV)
        {
            setBookmarkStatus(mySubscriptionDataArray[position].id!!)
            mySubscriptionDataArray[position].isFavourite = false
            storyAdapter.notifyDataSetChanged()
        }else if(tabType== CLICK_FROM_NOT_FAV)
        {
            setBookmarkStatus(mySubscriptionDataArray[position].id!!)
            mySubscriptionDataArray[position].isFavourite = true
            storyAdapter.notifyDataSetChanged()
        }
    }

    private fun setBookmarkStatus(storyId: Int) {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.UPDATE_BOOKMARK_STATUS)
        jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(
            this,
            UrlManager.MAIN_URL,
            UrlManager.UPDATE_BOOKMARK_STATUS,
            params,
            commonModel
        )
    }
}