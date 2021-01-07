package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.GetUserProfileData
import com.itsupportwale.dastaan.beans.ResponseMyProfile
import com.itsupportwale.dastaan.databinding.ActivityEditUserProfileBinding
import com.itsupportwale.dastaan.databinding.ActivityRegisterBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.API
import com.itsupportwale.dastaan.utility.LOGIN_CHECK
import com.itsupportwale.dastaan.utility.UserPreference
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileNotFoundException
import java.util.ArrayList

class EditUserProfileActivity : BaseActivity(), View.OnClickListener {
    lateinit var activityEditUserProfileBinding: ActivityEditUserProfileBinding
    private var galleryImages = ArrayList<Image>()
    var isFeatured = false
    var device_token = ""
    var capturedFile: File? = null
    private var userPreference: UserPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditUserProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_user_profile)
        userPreference = UserPreference.getInstance(this);

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                device_token = task.result?.token.toString()
            })

        initView()
    }

    fun initView()
    {

        activityEditUserProfileBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityEditUserProfileBinding.topNavBar.notificationCount.visibility = View.GONE
        activityEditUserProfileBinding.topNavBar.menuIcon.visibility = View.GONE
        activityEditUserProfileBinding.topNavBar.backIcon.visibility = View.VISIBLE
        activityEditUserProfileBinding.topNavBar.backIcon.setOnClickListener(this)
        activityEditUserProfileBinding.topNavBar.navTitle.text = getString(R.string.edit_user_details)
        // activityPropertyDetailsBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
        //activityPropertyDetailsBinding.topNavBar.notificationIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))//
        activityEditUserProfileBinding.topNavBar.backIcon.setColorFilter(ContextCompat.getColor(this, R.color.white))


        activityEditUserProfileBinding.topNavBar.backIcon.setOnClickListener(this)
        activityEditUserProfileBinding.updateBtn.setOnClickListener(this)
        activityEditUserProfileBinding.uploadImage.setOnClickListener(this)

        getUserProfileData()

    }
    private fun getUserProfileData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.PROFILE_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", API.toBase64(jsObj.toString()))

        if(galleryImages.size>0)
        {
            try {
                params.put("photo", File(galleryImages[0].path))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        apiCall(this, UrlManager.MAIN_URL, UrlManager.PROFILE_METHOD_NAME, params, commonModel)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backIcon -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                finish()
            }
            R.id.updateBtn -> {
                checkValiadtion()
            }
            R.id.uploadImage -> {
                chooseGalleryImage()
            }
        }
    }

    private fun checkValiadtion() {

        if (TextUtils.isEmpty(activityEditUserProfileBinding.edtEmail.text.toString().trim())) {
            showSnackBar(activityEditUserProfileBinding.edtEmail, resources.getString(R.string.error_email))
        }else if (TextUtils.isEmpty(activityEditUserProfileBinding.edtName.text.toString().trim())) {
            showSnackBar(activityEditUserProfileBinding.edtName, resources.getString(R.string.error_name))
        }else{
            val params = RequestParams()
            showLoader(resources.getString(R.string.please_wait))
            var commonModel = CommonValueModel()

            val jsObj = Gson().toJsonTree(API()) as JsonObject
            jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.UPDATE_PROFILE_METHOD_NAME)
            jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
            jsObj.addProperty(UrlManager.PARAM_NAME, activityEditUserProfileBinding.edtName.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_EMAIL, activityEditUserProfileBinding.edtEmail.text.toString().trim())
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
            apiCall(applicationContext, UrlManager.MAIN_URL, UrlManager.UPDATE_PROFILE_METHOD_NAME, params, commonModel)
        }
    }

    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", result.toString())
        if(apiName.equals(UrlManager.UPDATE_PROFILE_METHOD_NAME))
        {
        val model: ResponseMyProfile = getGsonAsConvert().fromJson(result, ResponseMyProfile::class.java)
        if (model.status!!)
        {
            userPreference?.name = model.data!!.user!!.name
            userPreference?.email = model.data!!.user!!.email
            userPreference?.photo = model.data!!.user!!.photo
            userPreference?.device_token = device_token
            userPreference?.save(this)

            showSnackBar(
                activityEditUserProfileBinding.edtName,
                model.message.toString())

            finish()
        }else{
            showSnackBar(
                activityEditUserProfileBinding.edtName,
                model.message.toString()
            )
        }
        }else if(apiName.equals(UrlManager.PROFILE_METHOD_NAME))
        {
            val model: GetUserProfileData = getGsonAsConvert().fromJson(result, GetUserProfileData::class.java)
            if (model.status!!)
            {
                activityEditUserProfileBinding.edtName.setText(model.data!!.user!!.name)
                activityEditUserProfileBinding.edtEmail.setText(model.data!!.user!!.email)
                activityEditUserProfileBinding.edtPhoneNumber.setText(model.data!!.user!!.phone)

                Glide.with(this).asBitmap()
                    .load(model.data!!.user!!.photo)
                    .error(R.drawable.default_image_icon)
                    .into(activityEditUserProfileBinding.userImage)
            }else{
                showSnackBar(
                    activityEditUserProfileBinding.edtName,
                    model.message.toString()
                )
            }
        }

        closeLoader()
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", message.toString())
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
            Picasso.get().load(uri).into(activityEditUserProfileBinding.userImage)
            isFeatured = true
        }
    }

}