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
import com.itsupportwale.dastaan.databinding.ActivityMyStoriesListBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_USER_ID
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import io.reactivex.disposables.CompositeDisposable

class MyStoriesList : BaseActivity(), View.OnClickListener, MyStoryAdapter.onRecyclerViewItemClickListener{
    lateinit var activityMyStoriesListBinding: ActivityMyStoriesListBinding
    private val storyDataArray: ArrayList<ResponseMyStory.Story> = ArrayList()
    lateinit var storyAdapter: MyStoryAdapter

    var pageIndex = 1
    var userId = 0

    val gson: Gson = Gson()

    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMyStoriesListBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_stories_list)
        userPreference = UserPreference.getInstance(this)

        val pullToRefresh: SwipeRefreshLayout = activityMyStoriesListBinding.pullToRefresh
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
        activityMyStoriesListBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityMyStoriesListBinding.topNavBar.notificationCount.visibility = View.GONE
        activityMyStoriesListBinding.topNavBar.menuIcon.visibility = View.GONE
        activityMyStoriesListBinding.topNavBar.backIcon.visibility = View.VISIBLE
        activityMyStoriesListBinding.topNavBar.backIcon.setOnClickListener(this)
        activityMyStoriesListBinding.topNavBar.navTitle.text = getString(R.string.myStories)
        activityMyStoriesListBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
        activityMyStoriesListBinding.topNavBar.notificationIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))

        setAdapters()
        getBundleData()

    }

    private fun getBundleData() {
        val extras = intent.extras
        if (null != extras) {
            userId = extras.getInt(PARAM_USER_ID)
            if(userId!=0)
            {
                callGetMyStoriesApi()
            }else{
                showSnackBar(activityMyStoriesListBinding.storyRecyclerView,"No User Data. Please Try Again!!!!")
            }
        }
    }


    private var linearLayoutManager: LinearLayoutManager? = null
    private fun setAdapters() {
        linearLayoutManager = LinearLayoutManager(this)
        activityMyStoriesListBinding.storyRecyclerView.setNestedScrollingEnabled(false)
        storyAdapter = MyStoryAdapter(this, storyDataArray)
        activityMyStoriesListBinding.storyRecyclerView.layoutManager = linearLayoutManager
        activityMyStoriesListBinding.storyRecyclerView.adapter = storyAdapter
        storyAdapter.setOnItemClickListener(this)
        storyAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        callGetMyStoriesApi()
    }

    private fun callGetMyStoriesApi() {

        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_MY_STORIES_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_PAGE, pageIndex)
        jsObj.addProperty(UrlManager.PARAM_WRITER_ID, userId)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(
            this,
            UrlManager.MAIN_URL,
            UrlManager.GET_MY_STORIES_METHOD_NAME,
            params,
            commonModel
        )
        Log.d("paramsparams", API.toBase64(jsObj.toString()))
    }


    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", result.toString())
        if(apiName.equals(UrlManager.GET_MY_STORIES_METHOD_NAME))
        {

                Log.d("modelmodel", result.toString())
                val model: ResponseMyStory = getGsonAsConvert().fromJson(
                    result,
                    ResponseMyStory::class.java
                )
                if (model.status!!) {

                    if(pageIndex == 1)
                    {
                        storyDataArray.clear()
                    }

                    storyDataArray.addAll(model.data!!.story!!)
                    storyAdapter.notifyDataSetChanged()

                    if(storyDataArray.size>0)
                    {
                        activityMyStoriesListBinding.noDataAvailable.visibility = View.GONE
                        activityMyStoriesListBinding.storyRecyclerView.visibility = View.VISIBLE
                    }
                }else{
                    showSnackBar(
                        activityMyStoriesListBinding.storyRecyclerView,
                        resources.getString(R.string.no_data_available)
                    )
                }
        }
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showSnackBar(
            activityMyStoriesListBinding.storyRecyclerView,
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



    override fun onItemListItemClickListener(position: Int) {
        val intent = Intent(this, StoryDetailsActivity::class.java)
        intent.putExtra(UrlManager.PARAM_STORY_ID,  storyDataArray[position].id)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}