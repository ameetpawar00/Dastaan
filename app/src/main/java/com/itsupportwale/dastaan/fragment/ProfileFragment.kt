package com.itsupportwale.dastaan.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.activity.MyStoriesList
import com.itsupportwale.dastaan.activity.StoryDetailsActivity
import com.itsupportwale.dastaan.adapters.MyStoriesAdapter
import com.itsupportwale.dastaan.beans.GetUserProfileData
import com.itsupportwale.dastaan.databinding.FragmentProfileBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.API
import com.itsupportwale.dastaan.utility.UserPreference

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : BaseFragment(), FragmentBaseListener, View.OnClickListener, MyStoriesAdapter.onRecyclerViewItemClickListener {

    private val myStoriesDataArray: ArrayList<GetUserProfileData.MyStories> = ArrayList()
    lateinit var myStoriesAdapter : MyStoriesAdapter
    lateinit var fragmentProfileBinding : FragmentProfileBinding
    val gson: Gson = Gson()
    var userId = 0
    private var userPreference: UserPreference? = null
    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentProfileBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_profile, container, false)
        val view: View = fragmentProfileBinding.getRoot()
        setOnFragmentListener(this);
        userPreference = UserPreference.getInstance(requireActivity());
        initView(view)
        return view
    }
    private fun initView(view: View) {
        fragmentProfileBinding.viewAll.setOnClickListener(this)
        myStoriesAdapter = MyStoriesAdapter(requireActivity(),myStoriesDataArray)
        fragmentProfileBinding.thisRecyclerView.layoutManager = LinearLayoutManager(requireActivity() , LinearLayoutManager.HORIZONTAL ,false)
        fragmentProfileBinding.thisRecyclerView.adapter = myStoriesAdapter
        myStoriesAdapter.setOnItemClickListener(this)
        myStoriesAdapter.notifyDataSetChanged()

        val pullToRefresh: SwipeRefreshLayout = fragmentProfileBinding.pullToRefresh
        pullToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                initView(view)
                pullToRefresh.setRefreshing(false)
            }
        })

        userId = userPreference?.user_id!!
        getProfileData()
    }

    private fun getProfileData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.PROFILE_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userId)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(activity, UrlManager.MAIN_URL, UrlManager.PROFILE_METHOD_NAME, params, commonModel)
    }

    override fun onClick(p0: View?) {
        when (view?.id) {
            R.id.viewAll -> {
                val intent = Intent(requireActivity(), MyStoriesList::class.java)
                intent.putExtra(UrlManager.PARAM_USER_ID,  userId)
                requireActivity().startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
        }
    }

    override fun onFragmentApiSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        showLog("HOME_METHOD_NAME-resylt", result.toString())

        val model: GetUserProfileData = getGsonAsConvert().fromJson(result, GetUserProfileData::class.java)
        if (model.status!!)
        {
            fragmentProfileBinding.userName.text = model.data!!.user!!.name
            fragmentProfileBinding.userEmail.text = model.data!!.user!!.email
            fragmentProfileBinding.followersCount.text = model.data!!.user!!.followerCount.toString()
            fragmentProfileBinding.storyCount.text = "("+model.data!!.myStories!!.size.toString()+")"

            Glide.with(requireActivity()).asBitmap()
                .load(model.data!!.user!!.photo)
                .error(R.drawable.default_image_icon)
                .into(fragmentProfileBinding.roundedimage)

            myStoriesDataArray.clear()
            myStoriesDataArray.addAll(model.data!!.myStories!!)
            myStoriesAdapter.notifyDataSetChanged()

            if(myStoriesDataArray.size>0)
            {

                fragmentProfileBinding.viewAll.visibility = View.VISIBLE
                fragmentProfileBinding.thisRecyclerView.visibility = View.VISIBLE
                fragmentProfileBinding.noDataAvailable.visibility = View.GONE
            }else{
                fragmentProfileBinding.thisRecyclerView.visibility = View.GONE
                fragmentProfileBinding.noDataAvailable.visibility = View.VISIBLE
            }
        }else{
            showSnackBar(
                fragmentProfileBinding.thisRecyclerView,
                model.error?.toString()
            )
        }
        closeLoader()
    }

    override fun onFragmentApiFailure(message: String?, apiName: String?) {
        showSnackBar(
            fragmentProfileBinding.thisRecyclerView,
            resources.getString(R.string.no_data_available)
        )
        closeLoader()
    }

    override fun onReadWriteStoragePermissionAllow(medialTypes: String?) {

    }

    override fun onReadWriteStoragePermissionDeny(medialTypes: String?) {
        TODO("Not yet implemented")
    }


    override fun onItemListItemClickListener(position: Int) {
         val intent = Intent(requireActivity(), StoryDetailsActivity::class.java)
         intent.putExtra(UrlManager.PARAM_STORY_ID,  myStoriesDataArray[position].id)
         startActivity(intent)
         requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}