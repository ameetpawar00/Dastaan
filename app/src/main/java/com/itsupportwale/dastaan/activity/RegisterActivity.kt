package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.ResponseGetLocationData
import com.itsupportwale.dastaan.beans.ResponseRegistrationData
import com.itsupportwale.dastaan.databinding.ActivityRegisterBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileNotFoundException
import java.util.ArrayList

class RegisterActivity : BaseActivity(), View.OnClickListener {
    lateinit var activityRegisterBinding: ActivityRegisterBinding
    private var galleryImages = ArrayList<Image>()
    var isFeatured = false
    var capturedFile: File? = null
    private var device_token = ""
    private var userPreference: UserPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)

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

    fun initView()
    {
        activityRegisterBinding.loginBtn.setOnClickListener(this)
        activityRegisterBinding.registerBtn.setOnClickListener(this)
        activityRegisterBinding.uploadImage.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.loginBtn -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            }
            R.id.registerBtn -> {
                checkValidation()
            }
            R.id.uploadImage -> {
                chooseGalleryImage()
            }
        }
    }

    private fun checkValidation() {
        if (TextUtils.isEmpty(activityRegisterBinding.edtName.text.toString().trim())) {
            showSnackBar(activityRegisterBinding.edtName, resources.getString(R.string.error_name))
        }else if (TextUtils.isEmpty(activityRegisterBinding.edtEmail.text.toString().trim())) {
            showSnackBar(activityRegisterBinding.edtEmail, resources.getString(R.string.error_email))
        }else if (TextUtils.isEmpty(activityRegisterBinding.edtPhoneNumber.text.toString().trim())) {
            showSnackBar(activityRegisterBinding.edtPhoneNumber, resources.getString(R.string.error_phone_number))
        }else if (TextUtils.isEmpty(activityRegisterBinding.edtPassword.text.toString().trim())) {
            showSnackBar(activityRegisterBinding.edtPassword, resources.getString(R.string.error_password))
        }else if (TextUtils.isEmpty(activityRegisterBinding.edtConfirmPassword.text.toString().trim())) {
            showSnackBar(activityRegisterBinding.edtConfirmPassword, resources.getString(R.string.error_confirm_password))
        }else if (!activityRegisterBinding.edtConfirmPassword.text.toString().trim().equals(activityRegisterBinding.edtPassword.text.toString().trim())) {
            showSnackBar(activityRegisterBinding.edtConfirmPassword, resources.getString(R.string.error_password_not_match))
        }else{
            submitRegistrationData()
        }
    }

    private fun submitRegistrationData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.REGISTER_USER_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_NAME, activityRegisterBinding.edtName.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_EMAIL, activityRegisterBinding.edtEmail.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_PASSWORD, activityRegisterBinding.edtPassword.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_PHONE, activityRegisterBinding.edtPhoneNumber.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_COUNTRY_CODE, "+91")
        jsObj.addProperty(UrlManager.PARAM_DEVICE_TOKEN, device_token)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        if(galleryImages.size>0)
        {
            try {
                params.put("photo", File(galleryImages[0].path))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        apiCall(applicationContext, UrlManager.MAIN_URL, UrlManager.REGISTER_USER_METHOD_NAME, params, commonModel)
    }

    override fun onSuccess(result: String?, methodName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, methodName, commonModel)
        closeLoader()
        when (methodName) {
            UrlManager.REGISTER_USER_METHOD_NAME -> {
                Log.d("resultresult", result!!)
                val responseRegistrationData: ResponseRegistrationData = getGsonAsConvert().fromJson(result, ResponseRegistrationData::class.java)
                if(responseRegistrationData.status == Constant.RESPONSE_SUCCESS)
                {
                    showSnackBar(activityRegisterBinding.edtConfirmPassword,"User Added Successfully.")

                    userPreference?.user_id = responseRegistrationData!!.data!!.userId
                    userPreference?.name = responseRegistrationData!!.data!!.name
                    userPreference?.phone = responseRegistrationData.data!!.phone
                    userPreference?.countryCode = responseRegistrationData.data!!.countryCode
                    userPreference?.email = responseRegistrationData.data!!.email
                    userPreference?.photo = responseRegistrationData.data!!.photo?.toString()
                    userPreference?.rating = responseRegistrationData.data!!.rating
                    userPreference?.followers = responseRegistrationData.data!!.followers
                    userPreference?.device_token = device_token

                    userPreference!!.loginStatus = LOGIN_CHECK
                    userPreference?.save(this)
                    gotoNextActivity()
                    //showSnackBar(activityRegisterBinding.edtConfirmPassword,responseRegistrationData.message)
                }else{
                    showSnackBar(activityRegisterBinding.edtConfirmPassword,responseRegistrationData.message)
                }
            }
        }
    }

    private fun gotoNextActivity() {
        val intent = Intent(applicationContext , MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        finish()
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showSnackBar(activityRegisterBinding.edtConfirmPassword,message.toString())
    }

    fun chooseGalleryImage() {
        ImagePicker.with(this)
                .setFolderMode(true)
                .setFolderTitle(getString(R.string.folder))
                .setImageTitle(getString(R.string.tap_to_select))
                .setMaxSize(1)
                .setCameraOnly(false)
                .setShowCamera(false)
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            galleryImages = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)!!
            val uri = Uri.fromFile(File(galleryImages[0].path))
            Picasso.get().load(uri).into(activityRegisterBinding.userImage)
            isFeatured = true
        }
    }

}