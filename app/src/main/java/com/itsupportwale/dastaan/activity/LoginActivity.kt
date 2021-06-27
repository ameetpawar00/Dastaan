package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.ResponseForgotPasswordData
import com.itsupportwale.dastaan.beans.ResponseLoginData
import com.itsupportwale.dastaan.beans.ResponseRegistrationData
import com.itsupportwale.dastaan.databinding.ActivityLoginBinding
import com.itsupportwale.dastaan.databinding.BottomSheetForgotPasswordBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.FORGOT_PASSWORD_METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.LOGIN_METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.MAIN_URL
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_DEVICE_TOKEN
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_EMAIL
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_PASSWORD
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity(), View.OnClickListener {
    lateinit var activityLoginBinding: ActivityLoginBinding
    private var device_token = ""
    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    device_token = task.result?.token.toString()
                })
        userPreference = UserPreference.getInstance(getApplicationContext());

        initView()
    }

    fun initView() {
        activityLoginBinding.continueBtn.setOnClickListener(this)
        activityLoginBinding.loginBtn.setOnClickListener(this)
        activityLoginBinding.facebookBtn.setOnClickListener(this)
        activityLoginBinding.googleBtn.setOnClickListener(this)
        activityLoginBinding.registerBtn.setOnClickListener(this)
        activityLoginBinding.forgotPasswordBtn.setOnClickListener(this)

        userPreference!!.loginStatus = FIRST_CHECK
        userPreference?.save(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginBtn -> {
                checkValidation()
            }
            R.id.continueBtn -> {
                gotoNextActivity()
            }
            R.id.forgotPasswordBtn -> {
                initializeForgotPasswordBottomBar()
            }
            R.id.registerBtn -> {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            }
            R.id.googleBtn -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            }
            R.id.facebookBtn -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            }
        }
    }

    private fun checkValidation() {
        if (TextUtils.isEmpty(activityLoginBinding.edtEmail.text.toString().trim())) {
            showSnackBar(activityLoginBinding.edtEmail, resources.getString(R.string.error_email))
        }else if (TextUtils.isEmpty(activityLoginBinding.edtPassword.text.toString().trim())) {
            showSnackBar(activityLoginBinding.edtPassword, resources.getString(R.string.error_password))
        }else{
            submitLoginData()
        }
    }

    private fun submitLoginData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(METHOD_NAME, LOGIN_METHOD_NAME)
        jsObj.addProperty(PARAM_EMAIL, activityLoginBinding.edtEmail.text.toString().trim())
        jsObj.addProperty(PARAM_PASSWORD, activityLoginBinding.edtPassword.text.toString().trim())
        jsObj.addProperty(PARAM_DEVICE_TOKEN, device_token)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(applicationContext, MAIN_URL, LOGIN_METHOD_NAME, params,commonModel)
    }

    override fun onSuccess(result: String?, methodName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, methodName, commonModel)
        closeLoader()
        when (methodName) {
            LOGIN_METHOD_NAME -> {
                val responseLoginData: ResponseLoginData = getGsonAsConvert().fromJson(result, ResponseLoginData::class.java)
                if(responseLoginData.status == Constant.RESPONSE_SUCCESS)
                {
                    showSnackBar(activityLoginBinding.edtPassword,"User Login Successful.")

                    userPreference?.user_id = responseLoginData!!.data!!.user!!.id
                    userPreference?.name = responseLoginData!!.data!!.user!!.name
                    userPreference?.phone = responseLoginData.data!!.user!!.phone
                    userPreference?.countryCode = responseLoginData.data!!.user!!.countryCode
                    userPreference?.email = responseLoginData.data!!.user!!.email
                    userPreference?.photo = responseLoginData.data!!.user!!.photo
                    userPreference?.followers = responseLoginData.data!!.user!!.followers
                    userPreference?.rating = responseLoginData.data!!.user!!.rating
                    userPreference?.device_token = device_token

                    userPreference!!.loginStatus = LOGIN_CHECK
                    userPreference?.save(this)
                    gotoNextActivity()
                    //showSnackBar(activityRegisterBinding.edtConfirmPassword,responseRegistrationData.message)
                }else{
                    showSnackBar(activityLoginBinding.edtPassword,responseLoginData.message)
                }
            }
            FORGOT_PASSWORD_METHOD_NAME->{
                val responseForgotPasswordData: ResponseForgotPasswordData = getGsonAsConvert().fromJson(result, ResponseForgotPasswordData::class.java)
                if(responseForgotPasswordData.status == Constant.RESPONSE_SUCCESS)
                {
                    showSnackBar(activityLoginBinding.edtPassword, responseForgotPasswordData.message)
                }else{
                    showSnackBar(activityLoginBinding.edtPassword,responseForgotPasswordData.message)
                }

            }
        }
    }


    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showSnackBar(activityLoginBinding.edtPassword, message.toString())
    }

    fun gotoNextActivity() {
        val intent = Intent(applicationContext , MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        finish()
    }

    lateinit var forgotPassworBottomSheetDailog: BottomSheetDialog
    var thisEmailId = ""
    fun initializeForgotPasswordBottomBar()
    {
        val myDrawerView = layoutInflater.inflate(R.layout.bottom_sheet_forgot_password, null)
        val binding = BottomSheetForgotPasswordBinding.inflate(layoutInflater, myDrawerView as ViewGroup, false)
        forgotPassworBottomSheetDailog = BottomSheetDialog(this)
        forgotPassworBottomSheetDailog.setContentView(binding.layoutBottomSheet)
        forgotPassworBottomSheetDailog.window?.setBackgroundDrawableResource(R.drawable.custom_tab_unselector_transparent)
        (binding.layoutBottomSheet.parent as View).setBackgroundResource(R.drawable.custom_tab_unselector_transparent)

        binding.submitBtn.setOnClickListener {
            forgotPassworBottomSheetDailog.dismiss()
            if (TextUtils.isEmpty(binding.edtEmail.text.trim().toString())) {
                showLongToast(resources.getString(R.string.error_email))
            }else{
                thisEmailId = binding.edtEmail.text.trim().toString()
                val params = RequestParams()
                showLoader(resources.getString(R.string.please_wait))
                var commonModel = CommonValueModel()
                val jsObj = Gson().toJsonTree(API()) as JsonObject
                jsObj.addProperty(METHOD_NAME, FORGOT_PASSWORD_METHOD_NAME)
                jsObj.addProperty(PARAM_EMAIL, thisEmailId)
                showLog("HOME_METHOD_NAME-param", jsObj.toString())
                params.put("data", toBase64(jsObj.toString()))
                apiCall(applicationContext, MAIN_URL, FORGOT_PASSWORD_METHOD_NAME, params,commonModel)
            }

        }
        binding.cancelTxt.setOnClickListener {
            forgotPassworBottomSheetDailog.dismiss()
        }
        forgotPassworBottomSheetDailog.show()
    }

}