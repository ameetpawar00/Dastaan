package com.itsupportwale.dastaan.utility

import com.itsupportwale.dastaan.BuildConfig
import java.io.Serializable


object Constant : Serializable {
    private const val serialVersionUID = 1L

    const val INDIVIDUAL = "individual"
    const val COMPANY = "company"
    const val JOB_TYPE_HOURLY = "Hourly"
    const val JOB_TYPE_FULL = "Full Time"
    const val JOB_TYPE_HALF = "Part Time"
    const val MALE = "Male"
    const val FEMALE = "Female"
    const val ARRAY_NAME = "JOBS_APP"
    const val CREDIT = "credit"
    const val DEBIT = "debit"
    const val CATEGORY_NAME = "category_name"
    const val CATEGORY_CID = "cid"
    const val CATEGORY_IMAGE = "category_image"
    const val CITY_ID = "c_id"
    const val CITY_NAME = "city_name"
    const val JOB_STATUS = "job_status"
    const val ALL_JOBS = "all_jobs"
    const val ACTIVE_JOBS = "active_jobs"
    const val COMPLETED_JOBS = "completed_jobs"
    const val FAILED_JOBS = "failed_jobs"

    /*
                           job_salary
                           "all_jobs": "2",
                                    "active_jobs": "1",
                                    "completed_jobs": "0",
                                    "failed_jobs": "0",*/
    const val COMING_FROM = "coming_from"
    const val JOB_ID = "id"
    const val JOB_NAME = "job_name"
    const val JOB_DESIGNATION = "job_designation"
    const val JOB_DESC = "job_desc"
    const val JOB_SALARY = "job_salary"
    const val JOB_COMPANY_NAME = "job_company_name"
    const val JOB_SITE = "job_company_website"
    const val JOB_PHONE_NO = "job_phone_number"
    const val JOB_COUNTRY_CODE = "job_country_code"
    const val JOB_MAIL = "job_mail"
    const val JOB_VACANCY = "job_vacancy"
    const val JOB_ADDRESS = "job_address"
    const val JOB_QUALIFICATION = "job_qualification"
    const val JOB_SKILL = "job_skill"
    const val JOB_IMAGE = "job_image"
    const val JOB_DATE = "job_date"
    const val JOB_APPLY = "job_apply_total"
    const val JOB_ALREADY_SAVED = "already_saved"
    const val JOB_APPLIED_DATE = "apply_date"
    const val JOB_APPLIED_SEEN = "seen"
    const val JOB_TYPE = "job_type"
    const val JOB_WORK_DAY = "job_work_day"
    const val JOB_WORK_TIME = "job_work_time"
    const val JOB_EXP = "job_experince"
    const val JOB_FAVOURITE = "is_favourite"
    const val IS_APPLIED = "is_applied"
    const val APP_NAME = "app_name"
    const val APP_IMAGE = "app_logo"
    const val APP_VERSION = "app_version"
    const val APP_AUTHOR = "app_author"
    const val APP_CONTACT = "app_contact"
    const val APP_EMAIL = "app_email"
    const val APP_WEBSITE = "app_website"
    const val APP_DESC = "app_description"
    const val APP_PRIVACY_POLICY = "app_privacy_policy"
    const val USER_NAME = "name"
    const val OTP = "otp"
    const val ACCOUNT_NUMBER = "account_number"
    const val IFSC_CODE = "ifsc_code"
    const val ACCOUNT_HOLDER_NAME = "account_holder_name"
    const val LINKED_MOBILE = "linked_mobile"
    const val CURRENT_WALLET_AMOUNT = "current_wallet_amount"
    const val CREDIT_REMAINING = "credits_remaining"
    const val SUBSCRIPTION_PLAN_ID = "subscription_plan_id"
    const val WORKING_TYPE = "working_type"
    const val JOB_START_TIME = "job_start_time"
    const val JOB_END_TIME = "job_end_time"
    const val USER_LAST_SEEN = "userLastSeen"
    const val USER_CHAT_STATUS = "userChatStatus"
    const val USER__NAME = "user_name"
    const val USER_ID = "user_id"
    const val USER_EMAIL = "email"
    const val USER_PHONE = "phone"
    const val USER_COUNTRY_CODE = "country_code"
    const val USER_CITY = "city"
    const val USER_ADDRESS = "address"
    const val USER_TYPE = "user_type"
    const val USER_IMAGE = "user_image"
    const val USER_RESUME = "user_resume"
    const val USER_TOTAL_APPLIED_JOB = "total_apply_job"
    const val USER_TOTAL_SAVED_JOB = "total_saved_job"
    const val USER_CURRENT_COMPANY = "current_company_name"
    const val USER_EXPERIENCE = "experiences"
    const val USER_SKILLS = "skills"
    const val USER_GENDER = "gender"
    const val USER_DOB = "date_of_birth"
    const val SEEN = "seen"
    const val USER_AVAILABILITY = "user_availability"
    var GET_SUCCESS_MSG = 0
    const val MSG = "msg"
    const val RESPONSE_SUCCESS = true
    const val SUCCESS = "success"
    const val STATUS = "status"
    var AD_COUNT = 0
    var AD_COUNT_SHOW = 0
    var isBanner = false
    var isInterstitial = false
    var isAdMobBanner = true
    var isAdMobInterstitial = true
    var bannerId: String? = ""
    var interstitialId: String? = ""
    var adMobPublisherId: String? = ""
    var isAppUpdate = false
    var isAppUpdateCancel = false
    var appUpdateVersion: String? = ""
    var appUpdateUrl: String? = ""
    var appUpdateDesc: String? = ""

    ///
    const val SENDER_ID = "SENDER_ID"
    const val SENDER_NAME = "SENDER_NAME"
    const val SENDER_IMAGE = "SENDER_IMAGE"
    const val RECEIVER_ID = "RECEIVER_ID"
    const val RECEIVER_NAME = "RECEIVER_NAME"
    const val RECEIVER_IMAGE = "RECEIVER_IMAGE"
    const val NODE_IMAGE = "uploadImage"
    const val NODE_TEXT = "text"
    const val NODE_TIMESTAMP = "timestamp"
    const val NODE_RECEIVER_ID = "receiverid"
    const val NODE_RECEIVER_NAME = "receivername"
    const val NODE_RECEIVER_PHOTO = "receiverphoto"
    const val NODE_SENDER_ID = "senderid"
    const val NODE_SENDER_NAME = "sendername"
    const val NODE_SENDER_PHOTO = "senderphoto"
    const val NODE_IS_READ = "isread"

    //User nodes
    const val NODE_USER_ID = "id"
    const val NODE_NAME = "name"
    const val NODE_PHOTO = "photo"
    const val LOG_TAG = "fchat"
    const val MESSAGE_CHILD = "messages"
    const val PREF_MY_ID = "myid"
    const val PREF_MY_NAME = "myname"
    const val PREF_MY_DP = "mydp"
    const val USERS_CHILD = "users"

    //Fragment tags
    const val TAG_CHAT_HISTORY = "Chat History"
    const val ID = "id"
    const val NAME = "name"
    const val PHOTO = "photo"
    const val SELECTCOUNT = "SELECTCOUNT"
    const val FILETYPE = "FILETYPE"
    const val FILEPATH = "FILEPATH"
    const val INTENT_PICTURE = "pic"
    const val CAMERA = "CAMERA"
    const val GALLERY = "GALLERY"
    const val REQUEST_POST_DOCUMENT_CALLBACK = 2070
    const val REQUEST_FILE_GALLERY_CALLBACK = 2072
    const val PLAN_ID = "plan_id"
    var grey = 1
    var pink = 2
    var white = 3
    var TIME_FORMAT_yyyy_MM_dd = "yyyy-MM-dd"
    var TIME_FORMAT_yyyy_MMMM_DD = "yyyy-MMMM-dd"
    var TIME_FORMAT_yyyy_MMMM = "yyyy, MMMM"

    const val OPERATION_CAPTURE_PHOTO = 1
    const val OPERATION_CHOOSE_PHOTO = 2

    const val REQUEST_CAMERA_CODE = 6600


    const val PROPERTY_HOME = 1
    const val PROPERTY_PRICE = 2




    const val TAB_MOST_POPULAR = "most_popular"
    const val TAB_LATEST = "latest"
    const val TAB_TOP_RATED = "top_rated"


}
