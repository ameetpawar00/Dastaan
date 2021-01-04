package com.itsupportwale.dastaan.activity

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.GetStoryDetailsModel
import com.itsupportwale.dastaan.beans.ResponseGenreData
import com.itsupportwale.dastaan.beans.ResponseStoryDetailsData
import com.itsupportwale.dastaan.beans.ResponseUpdateStoryData
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.squareup.picasso.Picasso
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.io.File
import java.io.FileNotFoundException
import java.lang.StringBuilder
import kotlin.collections.ArrayList
import com.itsupportwale.dastaan.databinding.ActivityEditStoryBinding



class EditStoryActivity : BaseActivity(), View.OnClickListener {
    lateinit var activityEditStoryBinding: ActivityEditStoryBinding
    private var galleryImages = ArrayList<Image>()
    var isFeatured = false
    var capturedFile: File? = null
    var selectedGenre = 0
    var previousGenre = 0
    var storyId: Int? = 0
    private var genreData = ArrayList<ResponseGenreData.Datum>()

    private var userPreference: UserPreference? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditStoryBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_edit_story
        )
        userPreference = UserPreference.getInstance(getApplicationContext());
        getBundleData()
        initView()
    }


    private fun getBundleData() {
        val extras = intent.extras
        if (null != extras) {
            storyId = extras.getInt(UrlManager.PARAM_STORY_ID)
            getStoryDetails()
        }

    }

    private fun initView() {




        activityEditStoryBinding.storyGenre.setOnClickListener(this)
        activityEditStoryBinding.updateBtn.setOnClickListener(this)
        activityEditStoryBinding.uploadPhotoBtn.setOnClickListener(this)
        activityEditStoryBinding.topNavBar.backIcon.setOnClickListener(this)

        activityEditStoryBinding.topNavBar.menuIcon.visibility = View.GONE
        activityEditStoryBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityEditStoryBinding.topNavBar.notificationCount.visibility = View.GONE
        activityEditStoryBinding.topNavBar.backIcon.visibility = View.VISIBLE

        activityEditStoryBinding.topNavBar.navTitle.text = getString(R.string.edit_story)
        activityEditStoryBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
        activityEditStoryBinding.topNavBar.notificationIcon.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )

        getStoryDetails()
    }


    private fun getStoryDetails() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.GET_STORY_DETAILS_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_USER_ID,  userPreference!!.user_id)
        jsObj.addProperty(UrlManager.PARAM_STORY_ID,  storyId)

        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(applicationContext, UrlManager.MAIN_URL, UrlManager.GET_STORY_DETAILS_METHOD_NAME, params, commonModel)
    }


    private fun checkValiadtion() {
        /*else if (TextUtils.isEmpty(activityEditStoryBinding.countryLinLay.text.toString().trim())) {
            showSnackBar(activityEditStoryBinding.countryLinLay, resources.getString(R.string.error_country))
        }else if (TextUtils.isEmpty(activityEditStoryBinding.regionLinLay.text.toString().trim())) {
            showSnackBar(activityEditStoryBinding.regionLinLay, resources.getString(R.string.error_region))
        }else if (TextUtils.isEmpty(activityEditStoryBinding.cityLinLay.text.toString().trim())) {
            showSnackBar(activityEditStoryBinding.cityLinLay, resources.getString(R.string.error_city))
        }*/
        if (TextUtils.isEmpty(activityEditStoryBinding.storyTitle.text.toString().trim())) {
            showSnackBar(activityEditStoryBinding.storyTitle, resources.getString(R.string.story_title_error))
        }else if (TextUtils.isEmpty(activityEditStoryBinding.storyTags.text.toString().trim())) {
            showSnackBar(activityEditStoryBinding.storyTags, resources.getString(R.string.story_tags_tags_error))
        }else if (TextUtils.isEmpty(activityEditStoryBinding.storyContent.text.toString().trim())) {
            showSnackBar(
                activityEditStoryBinding.storyContent,
                resources.getString(R.string.story_content_error)
            )
        }else if (selectedGenre==0) {
            showSnackBar(
                activityEditStoryBinding.storyContent,
                resources.getString(R.string.story_genre_error)
            )
        }else{


            val params = RequestParams()
            showLoader(resources.getString(R.string.please_wait))
            var commonModel = CommonValueModel()

            val jsObj = Gson().toJsonTree(API()) as JsonObject
            jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.EDIT_STORY_METHOD_NAME)

            jsObj.addProperty(UrlManager.PARAM_TITLE, activityEditStoryBinding.storyTitle.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_STORY_TAGS, activityEditStoryBinding.storyTags.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_CONTENT, activityEditStoryBinding.storyContent.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_GENRE, selectedGenre)
            jsObj.addProperty(UrlManager.PARAM_GENRE_OLD, previousGenre)
            jsObj.addProperty(UrlManager.PARAM_WRITER, userPreference!!.user_id)
            jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)

            if(galleryImages.size>0)
            {
                jsObj.addProperty("photo_count",  galleryImages.size)
            }
            showLog("HOME_METHOD_NAME-param", jsObj.toString())
            params.put("data", toBase64(jsObj.toString()))
            if(galleryImages.size>0)
            {
                try {
                    for((index,pic) in galleryImages.withIndex())
                    {
                        params.put("photo-"+index, File(pic.path))
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
            apiCall(applicationContext, UrlManager.MAIN_URL, UrlManager.EDIT_STORY_METHOD_NAME, params, commonModel)
        }
    }

    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)
        closeLoader()

        if(apiName.equals(UrlManager.GET_GENRE_METHOD_NAME))
        {
            closeLoader()
            val model: ResponseGenreData = getGsonAsConvert().fromJson(result, ResponseGenreData::class.java)
            if (model.status!!) {

                genreData.addAll(model.data!!)
            } else {
                showSnackBar(activityEditStoryBinding.storyContent, model.message)
            }
        }else if(apiName.equals(UrlManager.EDIT_STORY_METHOD_NAME)) {
            showLog("HOME_METHOD_NAME-result", result.toString())
            val model: ResponseUpdateStoryData = getGsonAsConvert().fromJson(result, ResponseUpdateStoryData::class.java)
            if (model.status!! && model.message != null) {
                showSnackBar(activityEditStoryBinding.storyContent, model.message)
                finish()
            }else{
                showSnackBar(activityEditStoryBinding.storyContent, model.message)
            }
        }else if(apiName.equals(UrlManager.GET_STORY_DETAILS_METHOD_NAME)) {
            showLog("HOME_METHOD_NAME-result", result.toString())
            val model: ResponseStoryDetailsData = getGsonAsConvert().fromJson(result, ResponseStoryDetailsData::class.java)
            if (model.status!! && model.data != null) {
                activityEditStoryBinding.storyTitle.setText(model.data!!.title)
                activityEditStoryBinding.storyContent.setText(model.data!!.content)
                activityEditStoryBinding.storyGenre.setText(model.data!!.genre)
                activityEditStoryBinding.storyTags.setText(model.data!!.storyTags)
                selectedGenre = model.data!!.genreId!!
                previousGenre = model.data!!.genreId!!
                callGetGenreApi()
                if(model.data!!.photo!=null && model.data!!.photo!!.isNotEmpty()){
                    Glide.with(this)
                        .load(model.data!!.photo!![0])
                        .error(R.drawable.ic_default_image)
                        .into(activityEditStoryBinding.imageFeatured)
                }
            }
        }
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", message.toString())
        showSnackBar(activityEditStoryBinding.imageFeatured, message.toString())
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
            R.id.storyGenre -> {
                initializeChildBottomBar()
            }
            R.id.updateBtn -> {
                checkValiadtion()
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
            Picasso.get().load(uri).into(activityEditStoryBinding.imageFeatured)
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
            activityEditStoryBinding.storyGenre.text = genreData[index].name
            selectedGenre = genreData[index].id!!
            bottomSheetDialog!!.dismiss()
        })
        bottomSheetDialog!!.show()
    }
}