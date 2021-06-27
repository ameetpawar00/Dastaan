package com.itsupportwale.dastaan.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.ResponseGenreData
import com.itsupportwale.dastaan.beans.ResponseStoryAddedData
import com.itsupportwale.dastaan.databinding.ActivityAddStoryBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.IDENTITY_HIDE
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.IDENTITY_SHOW
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.API
import com.itsupportwale.dastaan.utility.UserPreference
import com.loopj.android.http.RequestParams
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileNotFoundException


class AddStoryActivity : BaseActivity(), View.OnClickListener {
    lateinit var activityAddStoryBinding: ActivityAddStoryBinding
    private var galleryImages = ArrayList<Image>()
    private var genreData = ArrayList<ResponseGenreData.Datum>()
    var isFeatured = false
    var capturedFile: File? = null
    var selectedGenre = 0
    private var userPreference: UserPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddStoryBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_add_story
        )
        userPreference = UserPreference.getInstance(getApplicationContext());
        initView()
    }

    private fun submitFormData() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        var identityStatus  = IDENTITY_HIDE
        when (activityAddStoryBinding.identity.getCheckedRadioButtonId()) {
            R.id.identityShow ->
            {
                identityStatus = IDENTITY_SHOW
            }
            R.id.identityHide ->
            {
                identityStatus = IDENTITY_HIDE
            }
        }

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.ADD_STORY_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_TITLE, activityAddStoryBinding.storyTitle.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_CONTENT, activityAddStoryBinding.storyContent.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_STORY_TAGS, activityAddStoryBinding.storyTags.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_GENRE, selectedGenre)
        jsObj.addProperty(UrlManager.PARAM_IDENTITY, identityStatus)
        jsObj.addProperty(UrlManager.PARAM_WRITER, userPreference!!.user_id)

        if(galleryImages.size>0)
        {
            jsObj.addProperty("photo_count", galleryImages.size)
        }
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))

        if(galleryImages.size>0)
        {
            try {
                for((index, pic) in galleryImages.withIndex())
                {
                    params.put("photo-" + index, File(pic.path))
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        apiCall(
            applicationContext,
            UrlManager.MAIN_URL,
            UrlManager.ADD_STORY_METHOD_NAME,
            params,
            commonModel
        )


    }


    private fun initView() {

        activityAddStoryBinding.storyGenre.setOnClickListener(this)
        activityAddStoryBinding.submitBtn.setOnClickListener(this)
        activityAddStoryBinding.uploadPhotoBtn.setOnClickListener(this)
        activityAddStoryBinding.topNavBar.backIcon.setOnClickListener(this)

        activityAddStoryBinding.topNavBar.menuIcon.visibility = View.GONE
        activityAddStoryBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityAddStoryBinding.topNavBar.notificationCount.visibility = View.GONE
        activityAddStoryBinding.topNavBar.backIcon.visibility = View.VISIBLE

        activityAddStoryBinding.topNavBar.navTitle.text = getString(R.string.add_story)
        activityAddStoryBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
        activityAddStoryBinding.topNavBar.notificationIcon.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )

        callGetGenreApi()
    }


    private fun callGetGenreApi() {

        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_GENRE_METHOD_NAME)
        params.put("data", API.toBase64(jsObj.toString()))
        apiCall(
            this,
            UrlManager.MAIN_URL,
            UrlManager.GET_GENRE_METHOD_NAME,
            params,
            commonModel
        )
        Log.d("paramsparams", API.toBase64(jsObj.toString()))
    }

    private fun checkValiadtion() {
        if (TextUtils.isEmpty(activityAddStoryBinding.storyTitle.text.toString().trim())) {
            showSnackBar(
                activityAddStoryBinding.storyTitle,
                resources.getString(R.string.story_title_error)
            )
        }else if (TextUtils.isEmpty(
                activityAddStoryBinding.storyTags.text.toString().trim()
            )) {
            showSnackBar(
                activityAddStoryBinding.storyTags,
                resources.getString(R.string.story_tags_tags_error)
            )
        }else if (TextUtils.isEmpty(
                activityAddStoryBinding.storyContent.text.toString().trim()
            )) {
            showSnackBar(
                activityAddStoryBinding.storyContent,
                resources.getString(R.string.story_content_error)
            )
        }else if (selectedGenre==0) {
            showSnackBar(
                activityAddStoryBinding.storyContent,
                resources.getString(R.string.story_genre_error)
            )
        }else if (galleryImages.size==0) {
            showSnackBar(
                activityAddStoryBinding.storyContent,
                resources.getString(R.string.story_images_error)
            )
        }else{
            submitFormData()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)

        showLog("HOME_METHOD_NAME-result", result.toString())

        if(apiName.equals(UrlManager.GET_GENRE_METHOD_NAME))
        {
            closeLoader()
            val model: ResponseGenreData = getGsonAsConvert().fromJson(result, ResponseGenreData::class.java)
            if (model.status!!) {

                genreData.addAll(model.data!!)
            } else {
                showSnackBar(activityAddStoryBinding.storyContent, model.message)
            }
        }else if(apiName.equals(UrlManager.ADD_STORY_METHOD_NAME))
        {
            closeLoader()
            val model: ResponseStoryAddedData = getGsonAsConvert().fromJson(result, ResponseStoryAddedData::class.java)
            if (model.status!!) {
                showSnackBar(activityAddStoryBinding.storyContent, model.message)
                finish()
            } else {
                showSnackBar(activityAddStoryBinding.storyContent, model.message)
            }
        }
    }
    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showSnackBar(activityAddStoryBinding.storyContent,message.toString())
        showLog("HOME_METHOD_NAME-result", message.toString())
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backIcon -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
            R.id.uploadPhotoBtn -> {
                chooseGalleryImage()
            }
            R.id.submitBtn -> {
                checkValiadtion()
            }
            R.id.storyGenre -> {
                initializeChildBottomBar()
            }
        }
    }

    fun chooseGalleryImage() {
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle(getString(R.string.folder))
            .setImageTitle(getString(R.string.tap_to_select))
            .setMaxSize(5)
            .setCameraOnly(false)
            .setShowCamera(false)
            .setMultipleMode(true)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            galleryImages = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)!!
            val uri = Uri.fromFile(File(galleryImages[0].path))
            Picasso.get().load(uri).into(activityAddStoryBinding.imageFeatured)
            isFeatured = true
        }
    }

    var bottomSheetDialog: BottomSheetDialog? = null
    fun initializeChildBottomBar() {
        bottomSheetDialog = BottomSheetDialog(this)
        val parentView = layoutInflater.inflate(R.layout.bottom_sheet_genre, null)
        bottomSheetDialog!!.setContentView(parentView)
        parentView.minimumHeight = 200
        (parentView.parent as View).setBackgroundColor(Color.TRANSPARENT)

        parentView.findViewById<View>(R.id.cancelImg)
            .setOnClickListener { bottomSheetDialog!!.cancel() }

        val hourButtonLayout =
            parentView.findViewById<RadioGroup>(R.id.hour_radio_group)  // This is the id of the RadioGroup we defined

        for (data in genreData) {
            val button = RadioButton(this)
            button.id = data.id!!
            button.text = data.name
            button.isChecked =  (data.id!! == selectedGenre) // Only select button with same index as currently selected number of hours
            button.setBackgroundResource(R.drawable.item_selector) // This is a custom button drawable, defined in XML
            hourButtonLayout.addView(button)
        }

        hourButtonLayout.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<View>(i)
            val index = radioGroup.indexOfChild(radioButton)
            activityAddStoryBinding.storyGenre.text = genreData[index].name
            selectedGenre = genreData[index].id!!
            bottomSheetDialog!!.dismiss()
        })
        bottomSheetDialog!!.show()
    }

}