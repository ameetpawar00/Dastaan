package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.NotificationDataAdapter
import com.itsupportwale.dastaan.beans.GetNotificationData
import com.itsupportwale.dastaan.databinding.ActivityNotificationBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_USER_ID
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.API
import com.itsupportwale.dastaan.utility.Constant
import com.itsupportwale.dastaan.utility.UserPreference

class NotificationActivity : BaseActivity(), View.OnClickListener, NotificationDataAdapter.onRecyclerViewItemClickListener{
    lateinit var activityNotificationBinding: ActivityNotificationBinding
    private val notificationDataArray: ArrayList<GetNotificationData.Datum> = ArrayList()
    lateinit var notificationDataAdapter : NotificationDataAdapter
    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNotificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        userPreference = UserPreference.getInstance(this)
        initView()
    }

    fun initView()
    {
        activityNotificationBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityNotificationBinding.topNavBar.notificationCount.visibility = View.GONE
        activityNotificationBinding.topNavBar.menuIcon.visibility = View.GONE
        activityNotificationBinding.topNavBar.backIcon.visibility = View.VISIBLE
        activityNotificationBinding.topNavBar.backIcon.setOnClickListener(this)
        activityNotificationBinding.topNavBar.navTitle.text = getString(R.string.notifications)
        activityNotificationBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
        activityNotificationBinding.topNavBar.notificationIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))

        notificationDataArray.clear()
        notificationDataAdapter = NotificationDataAdapter(this, notificationDataArray)
        activityNotificationBinding.thisRecyclerView.layoutManager = LinearLayoutManager(this)
        activityNotificationBinding.thisRecyclerView.adapter = notificationDataAdapter
        //notificationDataAdapter.setOnItemClickListener(this)
        notificationDataAdapter.notifyDataSetChanged()

        getNotificationData()
    }

    private fun getNotificationData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(METHOD_NAME, UrlManager.GET_NOTIFICATION_METHOD_NAME)
        jsObj.addProperty(PARAM_USER_ID, userPreference!!.user_id)

        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(applicationContext, UrlManager.MAIN_URL, UrlManager.GET_NOTIFICATION_METHOD_NAME, params, commonModel)
    }

    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", result.toString())
        if(apiName.equals(UrlManager.GET_NOTIFICATION_METHOD_NAME))
        {
            val model: GetNotificationData = getGsonAsConvert().fromJson(result, GetNotificationData::class.java)
            if (model.status!!)
            {
                notificationDataArray.addAll(model.data!!)
                notificationDataAdapter.notifyDataSetChanged()

                if(notificationDataArray.size>0)
                {
                    activityNotificationBinding.thisRecyclerView.visibility = View.VISIBLE
                    activityNotificationBinding.noDataAvailable.visibility = View.GONE
                }else{
                    activityNotificationBinding.thisRecyclerView.visibility = View.GONE
                    activityNotificationBinding.noDataAvailable.visibility = View.VISIBLE
                }

            }else{
                showSnackBar(
                        activityNotificationBinding.thisRecyclerView,
                        resources.getString(R.string.no_data_available)
                )
            }
        }
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showSnackBar(
                activityNotificationBinding.thisRecyclerView,
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
        val click_action: String = notificationDataArray[position].clickAction!!
        var notificationIntent: Intent? = null
        notificationIntent = Intent(this@NotificationActivity, MainActivity::class.java)
    /*
        if (click_action == "seeker_job_awarded") {
            notificationIntent = Intent(this@NotificationActivity, MainActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            notificationIntent.putExtra(Constant.COMING_FROM, "AppliedJobFragment")
        } else if (click_action == "provider_job_applied") {
            notificationIntent = Intent(this, JobProviderMainActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else if (click_action == "seeker_job_completed") {
            notificationIntent = Intent(this, MainActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            notificationIntent.putExtra(Constant.COMING_FROM, "AppliedJobFragment")
        } else if (click_action == "seeker_wallet_withdrawal") {
            notificationIntent = Intent(this, WalletActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else if (click_action == "provider_wallet_withdrawal") {
            notificationIntent = Intent(this, ProviderWalletActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }*/
        startActivity(notificationIntent)
    }
}