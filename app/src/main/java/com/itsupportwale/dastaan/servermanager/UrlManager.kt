package com.itsupportwale.dastaan.servermanager

interface UrlManager {
    companion object {
        const val AUTH = "Authorization"
        const val TOKEN = "token"
        const val ACCEPT = "Accept: application/json"
        const val CONTENT_TYPE = "Content-type: application/json"
        const val MAIN_URL = "https://dastaan.life/api/index.php"
        const val LOGIN_METHOD_NAME = "user_login"
        const val VERIFY_USER_METHOD_NAME = "verify_user"
        const val RESEND_OTP_METHOD_NAME = "resend_otp"
        const val REGISTER_USER_METHOD_NAME = "user_register"
        const val ADD_STORY_METHOD_NAME = "story_add"
        const val EDIT_STORY_METHOD_NAME = "story_update"
        const val GET_STORY_DETAILS_METHOD_NAME = "get_single_story"
        const val UPDATE_BOOKMARK_STATUS = "update_bookmark_status"
        const val GET_FAV = "user_bookmark_list"
        const val SET_STORY_RATING = "set_story_rating"
        const val UPDATE_FOLLOWING_STATUS = "update_following_status"
        const val GET_HOME_METHOD_NAME = "get_home"
        const val GET_MY_STORIES_METHOD_NAME = "get_my_stories"
        const val GET_MY_SUBSCRIPTION_METHOD_NAME = "get_my_subscriptions"
        const val GET_GENRE_METHOD_NAME = "get_genre"
        const val GET_PROPERTY_METHOD_LOCATION = "get_properties_location"
        const val GET_NOTIFICATION_METHOD_NAME = "get_notification"
        const val GET_COUNTRY_METHOD_NAME = "get_country"
        const val HOME_METHOD_NAME = "get_home_popular"

        const val PROFILE_METHOD_NAME = "get_profile"
        const val UPDATE_PROFILE_METHOD_NAME = "update_profile"
        const val FORGOT_PASSWORD_METHOD_NAME = "forgot_password"
        const val GET_LOCATION_METHOD_NAME = "get_location_list"

        const val REGISTER_METHOD_NAME = ""

        // PARAM NAME
        const val PARAM_PROPERTY_TYPE = "property_type"
        const val PARAM_BUDGET_RANGE = "budget_range"
        //const val PARAM_ = "title"
        const val PARAM_PHOTO = "photo"
        const val PARAM_WRITER = "writer"
        const val PARAM_STATUS = "status"
        const val PARAM_DESCRIPTION = "description"
        const val PARAM_YEAR_BUILD = "year_build"
        const val PARAM_PARKING = "parking"
        const val PARAM_BEDROOM = "bedroom"
        const val PARAM_BATHROOM = "bathroom"
        const val PARAM_KITCHEN = "kitchen"
        const val PARAM_PROPERTY_SIZE = "property_size"
        const val PARAM_FEATURES = "features"
        const val PARAM_TITLE = "title"
        const val IDENTITY_SHOW = "IDENTITY_SHOW"
        const val IDENTITY_HIDE = "IDENTITY_HIDE"
        const val PARAM_CONTENT = "content"
        const val PARAM_STORY_TAGS = "story_tags"
        const val PARAM_GENRE = "genre"
        const val PARAM_IDENTITY = "identity_status"
        const val PARAM_GENRE_OLD= "genre_old"
        const val PARAM_PRICE = "price"
        const val PARAM_STORY_CONTENT = "story_Content"
        const val PARAM_NAME = "name"
        const val PARAM_STORY_TITLE = "story_title"
        const val PARAM_TAGS = "tags"
        const val PARAM_PAGE = "page"
        const val PARAM_SEARCH = "search"
        const val PARAM_ORDER = "order"
        const val PARAM_USER_ID = "user_id"
        const val PARAM_WRITER_ID = "writer_id"
        const val PARAM_REVIEWER_ID = "reviewer_id"
        const val PARAM_STORY_ID = "story_id"
        const val PARAM_RATING = "rating"
        const val PARAM_REVIEW = "review"
        const val PARAM_EMAIL = "email"
        const val METHOD_NAME = "method_name"
        const val PARAM_PASSWORD = "password"
        const val PARAM_PHONE = "phone"
        const val PARAM_COUNTRY_CODE = "country_code"
        const val PARAM_DEVICE_TOKEN = "device_token"
        const val PARAM_TYPE_LATEST = "latest"
        const val PARAM_TYPE_MOST_POPULAR = "most_popular"
        const val PARAM_TYPE_TOP_RATED = "top_rated"

        const val PARAM_CITY_ID = "city_id"
    }
}
