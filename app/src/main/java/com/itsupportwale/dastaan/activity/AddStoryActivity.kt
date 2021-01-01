package com.itsupportwale.dastaan.activity

import android.annotation.SuppressLint
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
import com.itsupportwale.dastaan.beans.ResponseStoryAddedData
import com.itsupportwale.dastaan.databinding.ActivityAddStoryBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.squareup.picasso.Picasso
import dev.amin.tagadapter.Tag
import dev.amin.tagadapter.TagAdapter
import java.io.File
import java.io.FileNotFoundException
import kotlin.collections.ArrayList

class AddStoryActivity : BaseActivity(), View.OnClickListener,TagAdapter.onRecyclerViewItemClickListener {
    lateinit var activityAddStoryBinding: ActivityAddStoryBinding
    private var galleryImages = ArrayList<Image>()
    var isFeatured = false
    var capturedFile: File? = null
    lateinit var featureListAdapter: TagAdapter
    private val featureList: ArrayList<Tag> = ArrayList()
    private var featureListCurrent : ArrayList<Tag> = ArrayList()
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
        jsObj.addProperty(UrlManager.METHOD_NAME, UrlManager.ADD_STORY_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_TITLE, activityAddStoryBinding.storyTitle.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_CONTENT, activityAddStoryBinding.storyContent.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_STORY_TAGS, activityAddStoryBinding.storyTags.text.toString().trim())
        jsObj.addProperty(UrlManager.PARAM_GENRE, 1)
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

        var featuresList = resources.getStringArray(R.array.features_list)
        featureList.clear()
        for (i in 0..featuresList.size-1) {
            var tag = Tag()
            tag.title= featuresList[i]
            featureList.add(tag)
        }

        featureListAdapter = TagAdapter(this, featureList, 0)
        activityAddStoryBinding.thisListRv.layoutManager = LinearLayoutManager(this)
        activityAddStoryBinding.thisListRv.adapter = featureListAdapter
        featureListAdapter.setOnItemClickListener(this)
        featureListAdapter.notifyDataSetChanged()
    }

    private fun checkValiadtion() {
        /*else if (TextUtils.isEmpty(activityAddPropertyBinding.countryLinLay.text.toString().trim())) {
            showSnackBar(activityAddPropertyBinding.countryLinLay, resources.getString(R.string.error_country))
        }else if (TextUtils.isEmpty(activityAddPropertyBinding.regionLinLay.text.toString().trim())) {
            showSnackBar(activityAddPropertyBinding.regionLinLay, resources.getString(R.string.error_region))
        }else if (TextUtils.isEmpty(activityAddPropertyBinding.cityLinLay.text.toString().trim())) {
            showSnackBar(activityAddPropertyBinding.cityLinLay, resources.getString(R.string.error_city))
        }*/
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
        }else{
            submitFormData()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)

            showLog("HOME_METHOD_NAME-result", result.toString())

           if(apiName.equals(UrlManager.ADD_STORY_METHOD_NAME))
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