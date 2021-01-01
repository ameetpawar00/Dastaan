package com.itsupportwale.dastaan.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R

import com.itsupportwale.dastaan.activity.BaseActivity
import com.itsupportwale.dastaan.servermanager.BooleanDeserializer
import com.itsupportwale.dastaan.servermanager.DoubleDeserializer
import com.itsupportwale.dastaan.servermanager.IntDeserializer
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.servermanager.request.GetRequestModel
import com.itsupportwale.dastaan.servermanager.request.PostRequestModel


open class BaseFragment : Fragment() {
    protected var active = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   String className = this.getClass().getSimpleName();
        //  Utils.sendScreenName(getActivity(), className);
    }

    protected fun apiCall(context: Context?, apiUrl: String?, methodName: String?, params: RequestParams, commonModel: CommonValueModel?) {
        if (activity != null) {
            (activity as BaseActivity?)!!.apiCall(context, apiUrl!!, methodName!!,params, commonModel!!)
        }
    }


    protected fun setOnFragmentListener(fragmentBaseListener: FragmentBaseListener?) {
        if (activity != null) {
            (activity as BaseActivity?)?.setOnFragmentListener(fragmentBaseListener)
        }
    }

    protected fun showLoader(title: String?) {
        if (activity != null) {
            (activity as BaseActivity?)!!.showLoader(title)
        }
    }

    protected fun closeLoader() {
        if (activity != null) {
            (activity as BaseActivity?)!!.closeLoader()
        }
    }

    protected fun showLog(title: String?, message: String?) {
        if (activity != null) {
            (activity as BaseActivity?)!!.showLog(title, message!!)
        }
    }
    protected fun checkReadWriteStoragePermission(medialType: String) {
        if (activity != null) {
            (activity as BaseActivity?)!!.checkReadWriteStoragePermission(medialType);
        }
    }


    override fun onResume() {
        super.onResume()
        // getActivity().registerReceiver(brTab, new IntentFilter(Constants.BROADCAST_SILENT_PUSH));
    }

    fun checkInternetConnection(appContext: Context): Boolean {
        val manager = (appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val ni = manager.activeNetworkInfo
        return ni != null && ni.state == NetworkInfo.State.CONNECTED
    }

    protected fun showSnackBar(view: View, message: String?) {
        val snackbar: Snackbar
        snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT)
        val snackBarView = snackbar.view
        snackBarView.background = view.context.resources.getDrawable(R.drawable.text_shaded)
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }
    fun getGsonAsConvert(): Gson {
        return GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Int::class.java, IntDeserializer())
                .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
                .registerTypeAdapter(Double::class.java, DoubleDeserializer())
                .create()
    }
    open fun Any.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
}
