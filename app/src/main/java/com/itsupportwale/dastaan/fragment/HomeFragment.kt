package com.itsupportwale.dastaan.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.*
import com.itsupportwale.dastaan.beans.GetPropertyListModel
import com.itsupportwale.dastaan.beans.ResponseHomeData
import com.itsupportwale.dastaan.beans.ResponseUpdateBookmarkData
import com.itsupportwale.dastaan.databinding.FragmentHomeBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.itsupportwale.dastaan.utility.API.toBase64
import com.loopj.android.http.RequestParams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.math.BigInteger
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment(), FragmentBaseListener, View.OnClickListener, StoryAdapter.onRecyclerViewItemClickListener, MySubscriptionAdapter.onRecyclerViewItemClickListener, DiscoverAdapter.onRecyclerViewItemClickListener{

    lateinit var fragmentHomeBinding : FragmentHomeBinding
    private val mySubscriptionDataArray: ArrayList<ResponseHomeData.MySubscription> = ArrayList()
    lateinit var mySubscriptionAdapter: MySubscriptionAdapter

    private val discoverDataArray: ArrayList<ResponseHomeData.Genre> = ArrayList()
    lateinit var discoverAdapter: DiscoverAdapter

    private val storyDataArray: ArrayList<ResponseHomeData.Story> = ArrayList()
    lateinit var storyAdapter: StoryAdapter

    private var userPreference: UserPreference? = null
    var searchValue: String= ""
    var pageIndex = 1
    var orderBy = 1
    var genreSelected = 1
    private val disposable = CompositeDisposable()

    val gson: Gson = Gson()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
    lateinit var handler: Handler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_home, container, false)
        val view: View = fragmentHomeBinding.getRoot()
        setOnFragmentListener(this);

        userPreference = UserPreference.getInstance(requireActivity());

        val pullToRefresh: SwipeRefreshLayout = fragmentHomeBinding.pullToRefresh
        pullToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                initView(view)
                pullToRefresh.setRefreshing(false)
            }
        })

        initView(view)

        return view
    }
    private fun initView(view: View) {
        fragmentHomeBinding.sortBYBtn.setOnClickListener(this)

        disposable.add(
            RxTextView.textChangeEvents(fragmentHomeBinding.searchEdt).skipInitialValue()
                .debounce(
                    300,
                    TimeUnit.MILLISECONDS
                ).distinctUntilChanged().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                    searchData()
                )!!
        )
        handler = Handler(Looper.getMainLooper())

        setAdapters()

        callGetHomeApi()
    }


    private fun searchData(): DisposableObserver<TextViewTextChangeEvent?>? {
        return object : DisposableObserver<TextViewTextChangeEvent?>() {
            override fun onNext(textViewTextChangeEvent: TextViewTextChangeEvent) {
                searchValue = textViewTextChangeEvent.text().toString()
                callGetHomeApi()

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    private fun callGetHomeApi() {

        if(searchValue!="")
        {
            fragmentHomeBinding.mySubscription.visibility = View.GONE
        }else{
            fragmentHomeBinding.mySubscription.visibility = View.VISIBLE
        }

        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_HOME_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_PAGE, pageIndex)
        jsObj.addProperty(UrlManager.PARAM_SEARCH, searchValue)
        jsObj.addProperty(UrlManager.PARAM_ORDER, orderBy)
        jsObj.addProperty(UrlManager.PARAM_GENRE, genreSelected)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(
            requireActivity(),
            UrlManager.MAIN_URL,
            UrlManager.GET_HOME_METHOD_NAME,
            params,
            commonModel
        )
        Log.d("paramsparams", toBase64(jsObj.toString()))
    }

    override fun onFragmentApiSuccess(result: String?, methodName: String?, commonModel: CommonValueModel?) {
        closeLoader()
        when (methodName) {
            UrlManager.UPDATE_BOOKMARK_STATUS -> {
                Log.d("modelmodel", result.toString())

                val model: ResponseUpdateBookmarkData = getGsonAsConvert().fromJson(
                    result,
                    ResponseUpdateBookmarkData::class.java
                )
                if (model.status!!) {
                    showSnackBar(fragmentHomeBinding.searchEdt, "Bookmark Updated Successfully")
                }else{
                    showSnackBar(fragmentHomeBinding.searchEdt, model.message)
                }

            }
            UrlManager.GET_HOME_METHOD_NAME -> {
                Log.d("modelmodel", result.toString())
                val model: ResponseHomeData = getGsonAsConvert().fromJson(
                    result,
                    ResponseHomeData::class.java
                )
                if (model.status!!) {

                    if(pageIndex == 1)
                    {
                        storyDataArray.clear()
                    }

                    storyDataArray.addAll(model.data!!.story!!)
                    storyAdapter.notifyDataSetChanged()

                    mySubscriptionDataArray.clear()
                    discoverDataArray.clear()
                    var discoverAll = ResponseHomeData.Genre()
                    discoverAll.isSelected = true
                    discoverAll.id = 0
                    discoverAll.name = "All"
                    discoverAll.storyCount = storyDataArray.size
                    discoverDataArray.add(discoverAll)
                    discoverDataArray.addAll(model.data!!.genre!!)
                    discoverAdapter.notifyDataSetChanged()


                    mySubscriptionDataArray.addAll(model.data!!.mySubscription!!)
                    mySubscriptionAdapter.notifyDataSetChanged()


                    if(storyDataArray.size>0)
                    {
                        fragmentHomeBinding.noDataAvailableStory.visibility = View.GONE
                        fragmentHomeBinding.storyRecyclerView.visibility = View.VISIBLE
                    }

                    if(mySubscriptionDataArray.size>0)
                    {
                        fragmentHomeBinding.mySubscription.visibility = View.VISIBLE
                        fragmentHomeBinding.mySubscriptionRecyclerView.visibility = View.VISIBLE
                    }else{
                        fragmentHomeBinding.noDataAvailableSub.visibility = View.GONE
                        fragmentHomeBinding.mySubscription.visibility = View.GONE
                    }

                } else {
                    showSnackBar(fragmentHomeBinding.searchEdt, model.message)
                }
            }
        }
    }

    override fun onFragmentApiFailure(message: String?, apiName: String?) {
        closeLoader()
        showSnackBar(fragmentHomeBinding.searchEdt, message.toString())
    }

    override fun onReadWriteStoragePermissionAllow(medialTypes: String?) {

    }

    override fun onReadWriteStoragePermissionDeny(medialTypes: String?) {

    }


    private var linearLayoutManager: LinearLayoutManager? = null
    private fun setAdapters() {
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        fragmentHomeBinding.mySubscriptionRecyclerView.setNestedScrollingEnabled(false)
        mySubscriptionAdapter = MySubscriptionAdapter(requireActivity(), mySubscriptionDataArray)
        fragmentHomeBinding.mySubscriptionRecyclerView.layoutManager = linearLayoutManager
        fragmentHomeBinding.mySubscriptionRecyclerView.adapter = mySubscriptionAdapter
        mySubscriptionAdapter.setOnItemClickListener(this)
        mySubscriptionAdapter.notifyDataSetChanged()

        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        fragmentHomeBinding.discoverRecyclerView.setNestedScrollingEnabled(false)
        discoverAdapter = DiscoverAdapter(requireActivity(), discoverDataArray)
        fragmentHomeBinding.discoverRecyclerView.layoutManager = linearLayoutManager
        fragmentHomeBinding.discoverRecyclerView.adapter = discoverAdapter
        discoverAdapter.setOnItemClickListener(this)
        discoverAdapter.notifyDataSetChanged()

        linearLayoutManager = LinearLayoutManager(requireContext())
        fragmentHomeBinding.storyRecyclerView.setNestedScrollingEnabled(false)
        storyAdapter = StoryAdapter(requireActivity(), storyDataArray)
        fragmentHomeBinding.storyRecyclerView.layoutManager = linearLayoutManager
        fragmentHomeBinding.storyRecyclerView.adapter = storyAdapter
        storyAdapter.setOnItemClickListener(this)
        storyAdapter.notifyDataSetChanged()

    }

    override fun onItemListItemClickListener(position: Int, tabType: Int) {
        if(tabType== CLICK_FROM_PARENT)
        {

        }else if(tabType== CLICK_FROM_FAV)
        {
            setBookmarkStatus(storyDataArray[position].id!!)
            storyDataArray[position].isFavourite = false
            storyAdapter.notifyDataSetChanged()
        }else if(tabType== CLICK_FROM_NOT_FAV)
        {
            setBookmarkStatus(storyDataArray[position].id!!)
            storyDataArray[position].isFavourite = true
            storyAdapter.notifyDataSetChanged()
        }
        /*val intent = Intent(requireActivity(), PropertyDetailsActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)*/
    }

    private fun setBookmarkStatus(storyId: BigInteger) {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.UPDATE_BOOKMARK_STATUS)
        jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(
            requireActivity(),
            UrlManager.MAIN_URL,
            UrlManager.UPDATE_BOOKMARK_STATUS,
            params,
            commonModel
        )
    }

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.sortBYBtn -> {
                initializeChildBottomBar()
            }
        }
    }

    override fun onItemListItemClickListenerDiscover(position: Int) {

        for (data in discoverDataArray)
        {
            data.isSelected = false
        }
        discoverDataArray[position].isSelected = true
        discoverAdapter.notifyDataSetChanged()
    }

    override fun onItemListItemClickListenerSubscription(position: Int) {
        TODO("Not yet implemented")
    }

    var bottomSheetDialog: BottomSheetDialog? = null
    @SuppressLint("SetTextI18n")
    fun initializeChildBottomBar() {
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        val parentView = layoutInflater.inflate(R.layout.bottom_sheet_sorting, null)
        bottomSheetDialog!!.setContentView(parentView)
        parentView.minimumHeight = 200
        (parentView.parent as View).setBackgroundColor(Color.TRANSPARENT)

        parentView.findViewById<View>(R.id.cancelImg)
            .setOnClickListener { bottomSheetDialog!!.cancel() }


        parentView.findViewById<View>(R.id.thisRecent).setOnClickListener {
            orderBy = 1
            fragmentHomeBinding.sortBYBtn.text = requireActivity().getString(R.string.recent_title)
            callGetHomeApi()
            bottomSheetDialog!!.cancel()
        }

        parentView.findViewById<View>(R.id.thisMostPopular).setOnClickListener {
            orderBy = 2
            callGetHomeApi()
            fragmentHomeBinding.sortBYBtn.text = requireActivity().getString(R.string.popular_title)
            bottomSheetDialog!!.cancel()
        }

        parentView.findViewById<View>(R.id.thisHighlyRated).setOnClickListener {
            orderBy = 3
            fragmentHomeBinding.sortBYBtn.text = requireActivity().getString(R.string.high_rated_title)
            callGetHomeApi()
            bottomSheetDialog!!.cancel()
        }

        bottomSheetDialog!!.show()
    }
}