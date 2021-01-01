package com.itsupportwale.dastaan.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.activity.LoginActivity
import com.itsupportwale.dastaan.activity.SplashScreenActivity
import com.itsupportwale.dastaan.databinding.FragmentSettingsBinding
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.UserPreference

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : BaseFragment(), FragmentBaseListener, View.OnClickListener {


    lateinit var fragmentSettingsBinding : FragmentSettingsBinding
    val gson: Gson = Gson()

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingsBinding = DataBindingUtil.inflate(
            inflater!!,
            R.layout.fragment_settings,
            container,
            false
        )
        val view: View = fragmentSettingsBinding.getRoot()
        setOnFragmentListener(this)
        initView(view)
        return view
    }
    private fun initView(view: View) {

        fragmentSettingsBinding.logoutBtn.setOnClickListener(this)
      //  val postRequestModel = PostRequestModel()
     //   showLoader(requireActivity().resources.getString(R.string.please_wait))
    //    var commonModel = CommonValueModel()
    //    postApiCall(requireActivity(), UrlManager.GET_CAR_LIST_ALL, postRequestModel, commonModel)
    }

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.logoutBtn -> {
                LogoutBtn()
            }
        }
    }


    open fun LogoutBtn() {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.menu_logout))
            .setMessage(getString(R.string.logout_msg))
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                var userPreference: UserPreference? = UserPreference.getInstance(requireActivity());
                userPreference!!.loginStatus = 0
                val intent: Intent = Intent(
                    requireActivity(),
                    SplashScreenActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            .setNegativeButton(
                android.R.string.no
            ) { dialog, which ->
                // do nothing
            } //  .setIcon(R.drawable.ic_logout)
            .show()
    }

    override fun onFragmentApiSuccess(
        result: String?,
        apiName: String?,
        commonModel: CommonValueModel?
    ) {

    }

    override fun onFragmentApiFailure(message: String?, apiName: String?) {

    }

    override fun onReadWriteStoragePermissionAllow(medialTypes: String?) {

    }

    override fun onReadWriteStoragePermissionDeny(medialTypes: String?) {

    }
}