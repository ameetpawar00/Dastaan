package com.itsupportwale.dastaan.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.squareup.picasso.Picasso
import dev.amin.tagadapter.Tag
import dev.amin.tagadapter.TagAdapter
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.io.File
import java.io.FileNotFoundException
import java.lang.StringBuilder
import kotlin.collections.ArrayList
import com.itsupportwale.dastaan.databinding.ActivityEditStoryBinding


class EditStoryActivity : BaseActivity(), View.OnClickListener, TagAdapter.onRecyclerViewItemClickListener {
    lateinit var activityEditStoryBinding: ActivityEditStoryBinding
    private var galleryImages = ArrayList<Image>()
    var isFeatured = false
    var capturedFile: File? = null

    var storyId: String? = ""
    lateinit var featureListAdapter: TagAdapter
    private val featureList: ArrayList<Tag> = ArrayList()
    private var featureListCurrent : ArrayList<Tag> = ArrayList()

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
            storyId = extras.getString(UrlManager.PARAM_STORY_ID, "")
            getStoryDetails()
        }

    }

    private fun initView() {

        var featuresList = resources.getStringArray(R.array.features_list)
        featureList.clear()
        for (i in 0..featuresList.size-1) {
            var tag = Tag()
            tag.title= featuresList[i]
            featureList.add(tag)
        }

        featureListAdapter = TagAdapter(this, featureList, 0)
        activityEditStoryBinding.thisListRv.layoutManager = LinearLayoutManager(this)
        activityEditStoryBinding.thisListRv.adapter = featureListAdapter
        featureListAdapter.setOnItemClickListener(this)
        featureListAdapter.notifyDataSetChanged()

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
        }else{

            var stringBuilder = StringBuilder()
            var prefix = ""
            for(featureData in featureListCurrent)
            {
                if(featureData.isSelected!!)
                {
                    stringBuilder.append(prefix)
                    prefix = ","
                    stringBuilder.append(featureData.title)
                }
            }


            val params = RequestParams()
            showLoader(resources.getString(R.string.please_wait))
            var commonModel = CommonValueModel()

            val jsObj = Gson().toJsonTree(API()) as JsonObject
            jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.EDIT_STORY_METHOD_NAME)

            jsObj.addProperty(UrlManager.PARAM_STORY_TITLE, activityEditStoryBinding.storyTitle.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_TAGS, activityEditStoryBinding.storyTags.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_STORY_CONTENT, activityEditStoryBinding.storyContent.text.toString().trim())
            jsObj.addProperty(UrlManager.PARAM_FEATURES, stringBuilder.toString())
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

       /* if(apiName.equals(UrlManager.EDIT_STORY_METHOD_NAME)) {
            val model: UpdateStoryData = getGsonAsConvert().fromJson(result, UpdateStoryData::class.java)
            if (model.status!! && model.message != null) {
                showSnackBar(activityEditStoryBinding.cityLinLay, model.message)
            }
        }else if(apiName.equals(UrlManager.GET_STORY_DETAILS_METHOD_NAME)) {
            showLog("HOME_METHOD_NAME-result", result.toString())
            val model: GetStoryDetailsModel = getGsonAsConvert().fromJson(result, GetStoryDetailsModel::class.java)
            if (model.status!! && model.data != null) {

                activityEditStoryBinding.edtStoryName.setText(model.data!!.title)
                activityEditStoryBinding.edtLandmark.setText(model.data!!.landmark)
                defaultStoryType = model.data!!.type!!
                if(model.data!!.type==1){
                    activityEditStoryBinding.edtStoryType.setText(resources.getString(R.string.rent))
                }else{
                    activityEditStoryBinding.edtStoryType.setText(resources.getString(R.string.sale))
                }
                activityEditStoryBinding.edtStoryPrice.setText(model.data!!.price)
                activityEditStoryBinding.edtAreaSizeSqft.setText(model.data!!.propertySize)
                activityEditStoryBinding.edtKitchen.setText(model.data!!.kitchen)
                activityEditStoryBinding.edtBathrooms.setText(model.data!!.bathroom)
                activityEditStoryBinding.edtBedrooms.setText(model.data!!.bedroom)
                activityEditStoryBinding.edtParkings.setText(model.data!!.parking)
                activityEditStoryBinding.edtYearOfBuilt.setText(model.data!!.yearBuild)
                activityEditStoryBinding.edtDescription.setText(model.data!!.description)
                activityEditStoryBinding.countryLinLay.setText(model.data!!.countryName)
                activityEditStoryBinding.regionLinLay.setText(model.data!!.regionName)
                activityEditStoryBinding.cityLinLay.setText(model.data!!.cityName)
                activityEditStoryBinding.etLocation.setText(model.data!!.address)
                gblLatitude = parseDouble(model.data!!.latitude!!)
                gblLongitude = parseDouble(model.data!!.longitude!!)

                var thisSelectedOne = model.data!!.features!!.split(",")
                var stringBuilder = StringBuilder()
                var prefix = ""
                for((index, value) in featureList.withIndex()  )
                {
                    if(thisSelectedOne.contains(value.title) )
                    {
                        featureList[index].isSelected = true
                    }
                }
                featureListAdapter.notifyDataSetChanged()

                //activityEditStoryBinding.cityLinLay.setText(model.data!!.features)

                countryId = model.data!!.country
                regionId = model.data!!.state
                cityId = model.data!!.city

            }
        }*/
    }

    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", message.toString())
        showSnackBar(activityEditStoryBinding.imageFeatured, message.toString())
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

    override fun onItemClickListener(position: Int, thisTagList: ArrayList<Tag>, commingFrom: Int) {
        if(thisTagList[position].isSelected!!)
        {
            thisTagList[position].isSelected = false
        }else{
            thisTagList[position].isSelected = true
        }

        featureListAdapter.notifyDataSetChanged()
        featureListCurrent.clear()
        featureListCurrent.addAll(thisTagList)
    }
}