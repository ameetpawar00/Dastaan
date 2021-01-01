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
import com.itsupportwale.dastaan.adapters.FavouritesDataAdapter
import com.itsupportwale.dastaan.databinding.FragmentFavouritesBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.API
import com.itsupportwale.dastaan.utility.UserPreference

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : BaseFragment(), FragmentBaseListener, View.OnClickListener, FavouritesDataAdapter.onRecyclerViewItemClickListener {

    private val favouritesDataArray: ArrayList<String> = ArrayList()
    lateinit var favouritesDataAdapter : FavouritesDataAdapter
    lateinit var fragmentFavouritesBinding : FragmentFavouritesBinding
    val gson: Gson = Gson()
    private var userPreference: UserPreference? = null
    private var pageIndex = 1
    companion object {
        fun newInstance(): FavouritesFragment {
            return FavouritesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentFavouritesBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_favourites, container, false)
        val view: View = fragmentFavouritesBinding.getRoot()
        setOnFragmentListener(this);
        userPreference = UserPreference.getInstance(activity!!)
        initView(view)
        return view
    }
    private fun initView(view: View) {

        favouritesDataArray.add("a")
        favouritesDataArray.add("b")
        favouritesDataArray.add("c")
        favouritesDataArray.add("d")
        favouritesDataArray.add("e")
        favouritesDataArray.add("f")
        favouritesDataArray.add("g")
        favouritesDataArray.add("h")

        favouritesDataAdapter = FavouritesDataAdapter(requireActivity(),favouritesDataArray)
        fragmentFavouritesBinding.thisRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        fragmentFavouritesBinding.thisRecyclerView.adapter = favouritesDataAdapter
        favouritesDataAdapter.setOnItemClickListener(this)
        favouritesDataAdapter.notifyDataSetChanged()

        getFavData()
    }

    private fun getFavData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_FAV)
        jsObj.addProperty(UrlManager.PARAM_PAGE, pageIndex)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(activity, UrlManager.MAIN_URL, UrlManager.GET_FAV, params, commonModel)
    }

    override fun onClick(p0: View?) {
    }


    override fun onFragmentApiSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        showLog("HOME_METHOD_NAME-resylt", result.toString())

/*
        val model: GetFavData = getGsonAsConvert().fromJson(result, GetFavData::class.java)
        if (model.status!!)
        {
            favouritesDataArray.clear()
            favouritesDataArray.addAll(model.data!!)

            favouritesDataAdapter.notifyDataSetChanged()

            if(favouritesDataArray.size>0)
            {
                fragmentFavouritesBinding.thisRecyclerView.visibility = View.VISIBLE
                fragmentFavouritesBinding.noDataAvailable.visibility = View.GONE
            }else{
                fragmentFavouritesBinding.thisRecyclerView.visibility = View.GONE
                fragmentFavouritesBinding.noDataAvailable.visibility = View.VISIBLE
            }

        }else{
            showSnackBar(
                    fragmentFavouritesBinding.thisRecyclerView,
                    resources.getString(R.string.no_data_available)
            )
        }*/
        closeLoader()
    }

    override fun onFragmentApiFailure(message: String?, apiName: String?) {
        showSnackBar(
                fragmentFavouritesBinding.thisRecyclerView,
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
      /*  val intent = Intent(requireActivity(), PropertyDetailsActivity::class.java)
        intent.putExtra(UrlManager.PARAM_PROPERTY_ID,  favouritesDataArray[position].id)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)*/
    }


}