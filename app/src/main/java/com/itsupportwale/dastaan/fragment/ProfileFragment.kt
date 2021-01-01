package com.itsupportwale.dastaan.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.RecentlyViewedAdapter
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
class ProfileFragment : BaseFragment(), FragmentBaseListener, View.OnClickListener, RecentlyViewedAdapter.onRecyclerViewItemClickListener {

    private val recentlyViewedDataArray: ArrayList<String> = ArrayList()
    lateinit var recentlyViewedAdapter : RecentlyViewedAdapter
    lateinit var fragmentProfileBinding : FragmentProfileBinding
    val gson: Gson = Gson()
    private var userPreference: UserPreference? = null
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
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
        recentlyViewedDataArray.add("a")
        recentlyViewedDataArray.add("b")
        recentlyViewedDataArray.add("c")
        recentlyViewedDataArray.add("d")
        recentlyViewedDataArray.add("e")
        recentlyViewedAdapter = RecentlyViewedAdapter(requireActivity(),recentlyViewedDataArray)
        fragmentProfileBinding.thisRecyclerView.layoutManager = LinearLayoutManager(requireActivity() , LinearLayoutManager.HORIZONTAL ,false)
        fragmentProfileBinding.thisRecyclerView.adapter = recentlyViewedAdapter
        recentlyViewedAdapter.setOnItemClickListener(this)
        recentlyViewedAdapter.notifyDataSetChanged()

        getProfileData()
    }

    private fun getProfileData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.PROFILE_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference?.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(activity, UrlManager.MAIN_URL, UrlManager.PROFILE_METHOD_NAME, params, commonModel)
    }

    override fun onClick(p0: View?) {
    }

    override fun onFragmentApiSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        showLog("HOME_METHOD_NAME-resylt", result.toString())

       /* val model: GetUserProfileData = getGsonAsConvert().fromJson(result, GetUserProfileData::class.java)
        if (model.status!!)
        {
            fragmentProfileBinding.userName.text = model.data!!.user!!.name
            fragmentProfileBinding.userEmail.text = model.data!!.user!!.email
            fragmentProfileBinding.propertiesCount.text = model.data!!.myProperties!!.size.toString()

            if(model.data!!.myProperties!!.size>0)
            {
                fragmentProfileBinding.propertTitle.text = "Properties"
            }else{
                fragmentProfileBinding.propertTitle.text = "Property"
            }

            Glide.with(requireActivity()).asBitmap()
                    .load(model.data!!.user!!.photo)
                    .error(R.drawable.default_image_icon)
                    .into(fragmentProfileBinding.roundedimage)


            recentlyViewedDataArray.addAll(model.data!!.myProperties!!)
            recentlyViewedAdapter.notifyDataSetChanged()

            if(recentlyViewedDataArray.size>0)
            {
                fragmentProfileBinding.thisRecyclerView.visibility = View.VISIBLE
                fragmentProfileBinding.noDataAvailable.visibility = View.GONE
            }else{
                fragmentProfileBinding.thisRecyclerView.visibility = View.GONE
                fragmentProfileBinding.noDataAvailable.visibility = View.VISIBLE
            }


        }else{
            showSnackBar(
                    fragmentProfileBinding.thisRecyclerView,
                    resources.getString(R.string.no_data_available)
            )
        }*/
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
       /* val intent = Intent(requireActivity(), PropertyDetailsActivity::class.java)
        intent.putExtra(UrlManager.PARAM_PROPERTY_ID,  recentlyViewedDataArray[position].id)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)*/
    }
}