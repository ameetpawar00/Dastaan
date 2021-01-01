package com.itsupportwale.dastaan.utility

import java.util.*

interface Constants

const val TAB_PROP_MOST: Int = 1
const val TAB_PROP_TOP: Int = 2
const val TAB_PROP_LATEST: Int = 3
const val TAB_BAR_HOME: Int = 1
const val TAB_BAR_FAV: Int = 2
const val TAB_BAR_ADD: Int = 3
const val TAB_BAR_PROFILE: Int = 4
const val TAB_BAR_SETTING: Int = 5
const val SELECT_COUNTRY = 1
const val SELECT_REGION = 2
const val SELECT_CITY = 3
const val PARAM_COMING_FROM: String = "coming_from"
const val PERMISSION_REQUEST_LOCATION: Int = 123
const val PERMISSION_REQUEST_READ_WRITE = 125
const val STATUS_SUCCESS: String = "success"
const val STATUS_ERROR: String = "error"
const val LOGIN_CHECK: Int = 1
const val INIT_SEL_CHECK: Int = 2

const val REQUEST_SEARCH_LOCATION: Int = 1001

/*const val TIME_DATE_FORMAT_dd_MM_yyyy_with_TZ: String = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"
const val TIME_DATE_FORMAT_date: String = "dd"
const val TIME_DATE_FORMAT_DAY: String = "EEE"
const val TIME_DATE_FORMAT_MONTH: String = "MMM"
const val TIME_DATE_FORMAT_date_month: String = "dd MMM"
const val TIME_DATE_FORMAT_date_month_year: String = "dd MMM.yyyy"
const val TIME_DATE_FORMAT_year: String = "yyyy"
const val TIME_DATE_FORMAT_MONTH_yyyy: String = "MMM,yyyy"
const val TIME_FORMAT_yyyy_MM_dd: String  = "yyyy-MM-dd"
const val TIME_FORMAT_yyyy_MMMM_dd: String  = "yyyy-MMMM-dd"
const val TIME_FORMAT_MM: String  = "MM"
const val TIME_FORMAT_yyyy_MMMM = "yyyy, MMMM"
const val TIME_FORMAT_DD_MMMM_yyyy = "dd MMMM,yyyy "*/
const val TIME_FORMAT_YYYY_MM_DD_HH_MM_AA: String  = "yyyy-MM-dd hh:mmaa"
const val TIME_FORMAT_YYYY_MM_DD_HH_MM__AA: String  = "yyyy-MM-dd "
/*const val TIME_FORMAT_SLASH_YYYY_MM_DD: String  = "yyyy/MM/dd"
const val TIME_FORMAT_ONLY_TIME: String  = "hh:mm aa"
const val TIME_FORMAT_ONLY_TIME_HH: String  = "hh"
const val TIME_FORMAT_ONLY_TIME_MM: String  = "mm"
const val FORGOT_PASSWORD: String = "FORGOT_PASSWORD"
//Param
const val PARAM_MOBILE_NUMBER: String = "PARAM_MOBILE_NUMBER"

const val PARAM_USER_NAME: String = "PARAM_USER_NAME"*/

const val PARAM_SEARCH_TERM: String = "PARAM_SEARCH_TERM"
const val PARAM_CITY_ID: String = "city_id"
const val PARAM_ADDRESS: String = "PARAM_ADDRESS"
/*const val PARAM_MAP_ID: String = "PARAM_MAP_ID"

const val PARAM_CURRENT_LOCATION: String = "PARAM_CURRENT_LOCATION"

const val TYPE_CURRENT_LOCATION: String = "TYPE_CURRENT_LOCATION"
const val TYPE_HOME_LOCATION: String = "TYPE_HOME_LOCATION"

const val PARAM_LATITUDE: String = "PARAM_LATITUDE"
const val PARAM_LONGITUDE: String = "PARAM_LONGITUDE"
const val PARAM_SPORT_ID: String = "PARAM_SPORT_ID"
const val PARAM_DATE: String = "PARAM_DATE"
const val PARAM_VENUE_ID: String = "PARAM_VENUE_ID"
const val PARAM_SPORT_NAME: String = "PARAM_SPORT_NAME"
const val PARAM_CURRENCY: String = "PARAM_CURRENCY"
const val PARAM_PAYMENT_COUNT: String = "PARAM_PAYMENT_COUNT"
const val PARAM_VENUE_NAME: String = "PARAM_VENUE_NAME"
const val PARAM_ID: String = "id"
const val PARAM_AUCTION_ID: String = "PARAM_AUCTION_ID"
const val PARAM_TIMING: String = "PARAM_TIMING"
const val PARAM_FROM_TIME: String = "PARAM_FROM_TIME"
const val PARAM_TO_TIME: String = "PARAM_TO_TIME"
const val PARAM_FEE_PER_HOUR: String = "PARAM_FEE_PER_HOUR"
const val PARAM_MINIMUM_VALUE: String = "PARAM_MINIMUM_VALUE"
const val BID_EVENT_NEW: String = "newbid"
const val BID_EVENT_NEW_HOURLY: String = "hourly_newbid"
const val BID_EVENT_JOIN_LIVE: String = "join_live"
const val BID_EVENT_LEAVE_LIVE: String = "leave_live"
const val BID_EVENT_JOIN_APPRAISAL: String = "join_appraisal"
const val BID_EVENT_LEAVE_APPRAISAL: String = "leave_appraisal"
const val BID_EVENT_APPRAISAL_BID: String = "appraisal_bid"
const val BID_EVENT_APPRAISAL_ACCEPTED: String = "appraisal_accepted"
const val BID_EVENT_JOIN_HOURLY: String = "join_hourly"
const val BID_EVENT_LEAVE_HOURLY: String = "leave_hourly"

const val BID_EVENT_JOIN_REFERRAL: String = "join_referral"
const val BID_EVENT_LEAVE_REFERRAL: String = "leave_referral"
const val BID_EVENT_REFERRAL_BID: String = "referral_bid"
const val BID_EVENT_REFERRAL_ACCEPTED: String = "referral_accepted"

const val PARAM_IMAGES_DAMAGE: String = "PARAM_IMAGES_DAMAGE"
const val PARAM_IMAGES_BACK: String = "PARAM_IMAGES_BACK"
const val PARAM_IMAGES_FRONT: String = "PARAM_IMAGES_FRONT"
const val PARAM_IMAGES_INTERIOR: String = "PARAM_IMAGES_INTERIOR"
const val PARAM_IMAGES_LEFT: String = "PARAM_IMAGES_LEFT"
const val PARAM_IMAGES_RIGHT: String = "PARAM_IMAGES_RIGHT"
const val CAR_IMAGES: Int = 1
const val DAMAGE_IMAGES: Int = 2


const val SORT_TYPE_ENDING: String = "ending"
const val SORT_TYPE_LATEST: String = "latest"

const val SIGN_UP_TAB_DEALER_USER: String = "SIGN_UP_TAB_DEALER_USER"
const val SIGN_UP_TAB_PRIVATE_USER: String = "SIGN_UP_TAB_PRIVATE_USER"



//Our constants
const val OPERATION_CAPTURE_PHOTO = 1
const val OPERATION_CHOOSE_PHOTO = 2

const val REQUEST_CAMERA_CODE = 6600

const val BID_STATUS_NO_BID = 0
const val BID_STATUS_ON = 1
const val BID_STATUS_OUTBID = 2
const val BID_STATUS_BONUS_START = 3
const val BID_STATUS_BONUS_TIME = 4
const val BID_STATUS_SOLD = 5
const val BID_STATUS_RESERVED = 6

const val COMING_FROM_VEHICLE_DETAILS = 1
const val COMING_FROM_SELLCAR = 2




const val  REQUEST_FILE_GALLERY_CALLBACK = 2072
const val  REQUEST_FILE_GALLERY_CALLBACK_FRONT = 2081
const val  REQUEST_FILE_GALLERY_CALLBACK_BACK = 2082
const val  REQUEST_FILE_GALLERY_CALLBACK_LEFT = 2083
const val  REQUEST_FILE_GALLERY_CALLBACK_RIGHT = 2084
const val  REQUEST_FILE_GALLERY_CALLBACK_INTERIOR = 2085
const val  REQUEST_FILE_GALLERY_CALLBACK_DAMAGES = 2086

const val PERMISSION_REQUEST_READ_WRITE_FRONT = 2081
const val PERMISSION_REQUEST_READ_WRITE_BACK = 2082
const val PERMISSION_REQUEST_READ_WRITE_LEFT = 2083
const val PERMISSION_REQUEST_READ_WRITE_RIGHT = 2084
const val PERMISSION_REQUEST_READ_WRITE_INTERIOR = 2085
const val PERMISSION_REQUEST_READ_WRITE_DAMAGES = 2086
const val  REQUEST_CAMERA_CODE_VIDEO = 2020
const val LIVE_INDEX_LAN1 = 0
const val LIVE_INDEX_LAN2 = 1
const val LIVE_INDEX_LAN3 = 2
const val LIVE_INDEX_LAN4 = 3


const val FILEPATH = "FILEPATH"

const val SELECT_*/