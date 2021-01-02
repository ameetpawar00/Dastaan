package com.itsupportwale.dastaan.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import com.itsupportwale.dastaan.activity.StoryDetailsActivity
import com.itsupportwale.dastaan.adapters.FavouritesDataAdapter
import com.itsupportwale.dastaan.beans.ResponseBookmarkData
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

    private val favouritesDataArray: ArrayList<ResponseBookmarkData.Datum> = ArrayList()
    private val copyFavouritesDataArray: ArrayList<ResponseBookmarkData.Datum> = ArrayList()
    lateinit var favouritesDataAdapter : FavouritesDataAdapter
    lateinit var fragmentFavouritesBinding : FragmentFavouritesBinding
    val gson: Gson = Gson()


    var name: String = ""
    var typeGbl: String = ""
    private var isLoading: Boolean = false
    lateinit var userPreference: UserPreference

    lateinit var handler: Handler
    var keyWord = ""
    var myRunnable = object : Runnable{
        override fun run() {
            favouritesDataArray.clear()
            if(!TextUtils.isEmpty(keyWord))
            {
                for(cate in copyFavouritesDataArray)
                {
                    try{
                        if(cate.content?.toLowerCase()?.contains(keyWord.toLowerCase())!! || cate.title?.toLowerCase()?.contains(keyWord.toLowerCase())!!)
                        {
                            favouritesDataArray.add(cate)
                        }
                    }
                    catch (e: Exception)
                    {
                    }
                }
            }
            else
            {
                favouritesDataArray.addAll(copyFavouritesDataArray)

            }
            favouritesDataAdapter.notifyDataSetChanged()
        }
    }



    companion object {
        fun newInstance(): FavouritesFragment {
            return FavouritesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentFavouritesBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_favourites, container, false)
        val view: View = fragmentFavouritesBinding.getRoot()
        setOnFragmentListener(this);
        userPreference = UserPreference.getInstance(activity!!)!!
        initView(view)
        return view
    }
    private fun initView(view: View) {
        favouritesDataAdapter = FavouritesDataAdapter(requireActivity(),favouritesDataArray)
        fragmentFavouritesBinding.thisRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        fragmentFavouritesBinding.thisRecyclerView.adapter = favouritesDataAdapter
        favouritesDataAdapter.setOnItemClickListener(this)
        favouritesDataAdapter.notifyDataSetChanged()



        handler = Handler(Looper.getMainLooper())

        fragmentFavouritesBinding!!.searchEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyWord =  fragmentFavouritesBinding!!.searchEdt.text.toString()
                handler.removeCallbacks(myRunnable)
                handler.postDelayed(myRunnable,500)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        getFavData()
    }

    private fun getFavData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_FAV)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(activity, UrlManager.MAIN_URL, UrlManager.GET_FAV, params, commonModel)
    }

    override fun onClick(p0: View?) {

    }

    override fun onFragmentApiSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        showLog("HOME_METHOD_NAME-resylt", result.toString())

        val model: ResponseBookmarkData = getGsonAsConvert().fromJson(result, ResponseBookmarkData::class.java)
        if (model.status!!)
        {
            favouritesDataArray.clear()
            copyFavouritesDataArray.clear()
            copyFavouritesDataArray.addAll(model.data!!)
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
        }
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
        val intent = Intent(requireActivity(), StoryDetailsActivity::class.java)
        intent.putExtra(UrlManager.PARAM_STORY_ID,  favouritesDataArray[position].id)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }


}