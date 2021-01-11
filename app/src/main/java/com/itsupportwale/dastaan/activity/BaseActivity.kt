package com.itsupportwale.dastaan.activity

import android.Manifest.permission
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.fragment.FragmentBaseListener
import com.itsupportwale.dastaan.servermanager.BooleanDeserializer
import com.itsupportwale.dastaan.servermanager.DoubleDeserializer
import com.itsupportwale.dastaan.servermanager.IntDeserializer

import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*

import java.util.*


open class BaseActivity : AppCompatActivity() {
    private var medialType: String? = ""
    private var fragmentBaseListener: FragmentBaseListener? = null
    fun setOnFragmentListener(fragmentBaseListener: FragmentBaseListener?) {
        this.fragmentBaseListener = fragmentBaseListener
    }

    protected fun checkLocationPermission() {
        if (isBelowMarshmallow) {
            locationPermissionAllow()
        } else {
            val result = ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_FINE_LOCATION)
            if (result == PackageManager.PERMISSION_GRANTED) {
                locationPermissionAllow()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
            }
        }

    }


    open fun checkReadWriteStoragePermission(medialTypePhoto: String) {
        this.medialType = medialTypePhoto
        if (isBelowMarshmallow) {
            readWriteStoragePermissionAllow(medialTypePhoto)
        } else {
            val result = ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
            val result2 = ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
            val result3 = ContextCompat.checkSelfPermission(applicationContext, permission.CAMERA)
            if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED) {

                if (fragmentBaseListener != null) {
                    fragmentBaseListener!!.onReadWriteStoragePermissionAllow(medialTypePhoto)
                } else {
                    readWriteStoragePermissionAllow(medialTypePhoto)
                }

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE, permission.CAMERA), PERMISSION_REQUEST_READ_WRITE)
            }
        }
    }



    protected val isBelowMarshmallow: Boolean
        protected get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true else false

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.size > 0) {
                    val AccessfineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (AccessfineLocation) {
                        locationPermissionAllow()
                    } else {
                        locationPermissionDeny()
                    }
                }
            }
            PERMISSION_REQUEST_READ_WRITE -> {
                if (grantResults.size > 0) {
                    val readStrorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val writeStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    val camera = grantResults[2] == PackageManager.PERMISSION_GRANTED

                    if (fragmentBaseListener != null) {
                        if (readStrorage && writeStorage && camera) {
                            fragmentBaseListener!!.onReadWriteStoragePermissionAllow(medialType.toString());
                        } else {
                            fragmentBaseListener!!.onReadWriteStoragePermissionDeny(medialType.toString());
                        }

                    } else {
                        if (readStrorage && writeStorage && camera) {
                            readWriteStoragePermissionAllow(medialType.toString());
                        } else {
                            readWriteStoragePermissionDeny(medialType.toString());
                        }
                    }

                }
            }
        }
    }

    protected open fun readWriteStoragePermissionAllow(medialTypePhoto: String) {}
    protected open fun readWriteStoragePermissionDeny(medialTypePhoto: String) {}
    protected open fun locationPermissionDeny() {}
    protected open fun locationPermissionAllow() {}

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    protected val currentLocation: Unit
        protected get() {
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            LocationServices.getFusedLocationProviderClient(this@BaseActivity).requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(this@BaseActivity).removeLocationUpdates(this)
                        if (locationResult != null && locationResult.locations.size > 0) {
                            val lastLocationIndex = locationResult.locations.size - 1
                            getCurrentLatLong(locationResult.locations[lastLocationIndex].latitude, locationResult.locations[lastLocationIndex].longitude)
                        }
                    }
                },
                Looper.getMainLooper()
            )
        }

/*    protected open fun getCurrentLatLong(latitude: Double, longitude: Double) {
    }

    protected open fun onLocationPermissionSuccess() {
    }*/
    protected open fun onLocationPermissionFailure() {
    }


    fun promptLocationDailog(activity: Activity/*,userPreference:UserPreference*/)
    {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(activity) //Home is name of the activity

        builder.setMessage(resources.getString(R.string.location_message))
        builder.setPositiveButton("yes",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                onDialogYes()

            })

        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                onDialogNo()
            })

        val alert: AlertDialog = builder.create()
        alert.show()

    }


    protected open fun onDialogYes() {}
    protected open fun onDialogNo() {}



    protected fun callCurrentLatLong() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(this@BaseActivity).requestLocationUpdates(
            locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@BaseActivity)
                        .removeLocationUpdates(
                            this
                        )
                    if (locationResult != null && locationResult.locations.size > 0) {
                        val lastLocationIndex = locationResult.locations.size - 1
                        getCurrentLatLong(
                            locationResult.locations[lastLocationIndex].latitude,
                            locationResult.locations[lastLocationIndex].longitude
                        )
                    }
                }
            },
            Looper.getMainLooper()
        )
    }


    protected open fun getCurrentLatLong(latitude: Double, longitude: Double) {
    }

    protected open fun onLocationPermissionSuccess() {
    }


    fun enableLocationSettings(activity: Activity): Boolean {
        var gps_enabled = false
        var network_enabled = false
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            gps_enabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
        }
        try {
            network_enabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
        }
        return if (!gps_enabled && !network_enabled) {

            val googleApiClient = GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .build()
            googleApiClient.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result =
                LocationServices.SettingsApi.checkLocationSettings(
                    googleApiClient,
                    builder.build()
                )
            result.setResultCallback { result ->
                val status = result.status
                val state = result.locationSettingsStates
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        onLocationPermissionSuccess()
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        status.startResolutionForResult(
                            activity, 123
                        )

                    } catch (e: IntentSender.SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }

            false
        } else {
            onLocationPermissionSuccess()
            true

        }
    }

    private var customProgress: CustomProgress? = null
    fun showLoader(vararg loadingMessage: String?): CustomProgress? {
        runOnUiThread {
            var message: String? = ""
            if (loadingMessage != null && loadingMessage.size > 0) {
                message = loadingMessage[0]
            }
            if (customProgress == null) customProgress = CustomProgress(this@BaseActivity)
            closeLoader()
            customProgress = customProgress!!.show(this@BaseActivity, message, false, false, null)
        }
        return customProgress
    }

    override fun onStop() {
        if (customProgress != null && customProgress!!.isShowing()) customProgress!!.dismiss()
        super.onStop()
    }

    fun closeLoader() {
        if (customProgress != null && customProgress!!.isShowing()) customProgress!!.dismiss()
    }

  /*  fun getApiCall(context: Context?, pageName: String, requestObject: GetRequestModel, commonModel: CommonValueModel) {
        val jUser: UserPreference = UserPreference.getInstance(context!!)!!
        try {
            val jUser: UserPreference = UserPreference.getInstance(context!!)!!
            val apiInterface = getClient(context!!)!!.create(APIInterface::class.java)
            var call: Observable<ResponseBody?>? = null
            Log.d("JSON", " GETREQUEST" + gsonAsConvert.toJson(requestObject))
            when (pageName) {
                UrlManager.GET_MY_CAR_LIST -> call = apiInterface.getMyCArList(jUser.accessToken)
                UrlManager.GET_CAR_LIST_DETAILS -> call = apiInterface.getCarListByHourORLIVE(
                    jUser.accessToken,
                    requestObject.auctionType
                )
                UrlManager.GET_VEHICLES_DETAILS -> call = apiInterface.getVehicleDetails(
                    jUser.accessToken,
                    requestObject.carId,
                    requestObject.bidType
                )
                UrlManager.GET_MY_WATCHLIST_CAR_LIST -> call =
                    apiInterface.getWatchlistCars(jUser.accessToken)
                UrlManager.GET_WINNING_CAR_LIST -> call =
                    apiInterface.getWinningCarList(jUser.accessToken)
                UrlManager.GET_REFERRED_CAR_LIST -> call =
                    apiInterface.getRefferedCarList(jUser.accessToken)
            }
            if (call != null) {
                if (checkInternetConnection()) {
                    apiCall(call, pageName, commonModel)
                } else {
                    closeLoader()
                    showSnackBar(
                        findViewById(R.id.content),
                        resources.getString(R.string.check_internet)
                    )
                }
            }
        } catch (e: Exception) {
            closeLoader()
            showLongToast(e.message)
        }
    }

    fun postApiCall(
        context: Context?,
        pageName: String,
        requestObject: PostRequestModel,
        commonModel: CommonValueModel?
    ) {
        val jUser: UserPreference = UserPreference.getInstance(context!!)!!
        try {
            showLog("JSON", " POSTREQUEST" + gsonAsConvert.toJson(requestObject))
            val apiInterface = getClient(context!!)!!.create(APIInterface::class.java)
            var call: Observable<ResponseBody?>? = null
            when (pageName) {
                UrlManager.GET_All_CAR_LIST -> call = apiInterface.getAllCarList(jUser.accessToken)
                UrlManager.GET_CAR_LIST_ALL -> call = apiInterface.getAllCArList(jUser.accessToken)
                UrlManager.LOGIN -> call = apiInterface.loginApi(requestObject)
                UrlManager.FORGOT_PASSWORD -> call = apiInterface.forgotPasswordApi(requestObject)
                UrlManager.RESET_PASSWORD -> call = apiInterface.resetPassword(requestObject)
                UrlManager.CAR_ADD -> call = apiInterface.carAddApi(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.SAVE_CAR_WATCHLIST -> call = apiInterface.carAddWatchListApi(
                    jUser.accessToken,
                    requestObject.carId
                )
                UrlManager.LIVE_APPRAISAL_COUNTER -> call = apiInterface.carLiveAppraisalCounterApi(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.LIVE_APPRAISAL_ACCEPT -> call = apiInterface.carLiveAppraisalAcceptApi(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.CAR_REFERRAL_COUNTER -> call = apiInterface.carReferralCounterApi(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.CAR_REFERRAL_ACCEPT -> call = apiInterface.carReferralAcceptApi(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.CAR_REFERRAL_CANCEL -> call = apiInterface.carReferralCancelApi(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.CAR_AUTO_BID_ADD -> call = apiInterface.carSetAutoBid(
                    jUser.accessToken,
                    requestObject
                )
                UrlManager.CAR_AUTO_BID_REMOVE -> call = apiInterface.carRemoveAutoBid(
                    requestObject.autoId,
                    jUser.accessToken
                )
                UrlManager.CONTACT_US -> call = apiInterface.carContactUs(requestObject)
                UrlManager.GET_CAR_LIST_HOURLY -> call = apiInterface.getHourlyCarList(
                    jUser.accessToken,
                    requestObject
                )
                else -> {
                    closeLoader()
                    showLongToast("API post case not handled")
                }
            }
            if (call != null) {
                if (checkInternetConnection()) {
                    apiCall(call, pageName, commonModel!!)
                } else {
                    closeLoader()
                    Toast.makeText(
                        context,
                        resources.getString(R.string.check_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            closeLoader()
            showLongToast(e.message)
        }
    }
*/
    protected fun showSnackBar(view: View, message: String?) {
        val snackbar: Snackbar
        snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT)
        val snackBarView = snackbar.view
        snackBarView.background = view.context.resources.getDrawable(R.drawable.text_shaded)
        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

    fun checkInternetConnection(): Boolean {
        val manager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val ni = manager.activeNetworkInfo
        return ni != null && ni.state == NetworkInfo.State.CONNECTED
    }

    protected fun showLongToast(message: String?) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        if (toast != null) {
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
    protected fun showSlowToast(message: String?) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        if (toast != null) {
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.show()
        }
    }

  /*  var disposable: Disposable? = null

    @SuppressLint("CheckResult")
    private fun apiCall(
        call: Observable<ResponseBody?>,
        apiName: String,
        commonModel: CommonValueModel
    ) {

        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object :
            Observer<ResponseBody?> {
            override fun onSubscribe(d: Disposable) {
                disposable = d;
            }

            override fun onError(t: Throwable) {
                if (fragmentBaseListener != null) {
                    fragmentBaseListener!!.onFragmentApiFailure("Something went wrong, please try again.", apiName)
                } else {
                    this@BaseActivity.onFailure("Something went wrong, please try again.", apiName, commonModel)
                }
            }

            override fun onComplete() {
                showLog("onComplete", "onCompleted")
            }


            override fun onNext(response: ResponseBody) {
                try {
                    val result: String = response.string()
                    showLog("@@@@@@@@Response ", "apiName" + result)
                    if (fragmentBaseListener != null) {
                        fragmentBaseListener!!.onFragmentApiSuccess(
                            result, apiName, commonModel)
                    } else {
                        onSuccess(result, apiName, commonModel)
                    }
                } catch (e: IOException) {
                    showLog("onError", e.toString())
                }
            }
        })

    }*/


    fun showLog(title: String?, message: String) {
        Log.e(title, "" + message)
    }

  /*  protected open fun onSuccess(
        result: String?,
        apiName: String?,
        disposable: Disposable?,
        commonModel: CommonValueModel?
    ) {}
    protected open fun onFailure(
        message: String?,
        apiName: String?,
        disposable: Disposable?,
        commonModel: CommonValueModel?
    ) {}
*/

    protected fun callVerificationApi(otp: String?) {}

    fun getKmFromLatLong(lat1: Double, lng1: Double, lat2: Float, lng2: Float): Float {
        val loc1 = Location("")
        loc1.setLatitude(lat1.toDouble())
        loc1.setLongitude(lng1.toDouble())
        val loc2 = Location("")
        loc2.setLatitude(lat2.toDouble())
        loc2.setLongitude(lng2.toDouble())
        val distanceInMeters: Float = loc1.distanceTo(loc2)
        return distanceInMeters / 1000
    }

    open fun apiCall( context :Context?,apiName: String, methodName: String, params: RequestParams,commonModel: CommonValueModel?) {

        val client = AsyncHttpClient()

        client.post(apiName, params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                super.onStart()

            }

            override fun onSuccess(statusCode: Int, headers: Array<out cz.msebera.android.httpclient.Header>?, responseBody: ByteArray?) {
                val result = String(responseBody!!)
                if (statusCode == 200 || statusCode == 201) {

                    if (fragmentBaseListener != null) {
                        fragmentBaseListener!!.onFragmentApiSuccess(result, methodName,  commonModel)
                    } else {
                        onSuccess(result, methodName,  commonModel)
                    }

                } else {
                    val serverStatusCode=  ServerStatusCode()
                    if (fragmentBaseListener != null) {
                        fragmentBaseListener!!.onFragmentApiFailure(serverStatusCode.getStatusCodeMessage(statusCode), methodName)
                    } else {
                        this@BaseActivity.onFailure(serverStatusCode.getStatusCodeMessage(statusCode), methodName, commonModel)
                    }
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out cz.msebera.android.httpclient.Header>?, responseBody: ByteArray?, error: Throwable?) {
                val serverStatusCode=  ServerStatusCode()
                if (fragmentBaseListener != null) {
                    fragmentBaseListener!!.onFragmentApiFailure(serverStatusCode.getStatusCodeMessage(statusCode), methodName)
                } else {
                    this@BaseActivity.onFailure(serverStatusCode.getStatusCodeMessage(statusCode), methodName, commonModel)
                }
            }


        })
    }

    protected open fun onSuccess(result: String?, methodName: String?, commonModel: CommonValueModel?) {}
    protected open fun onFailure(message: String?, methodName: String?, commonModel: CommonValueModel?) {}

    public  fun toBase64(input: String): String? {
        val encodeValue = Base64.encode(input.toByteArray(), Base64.DEFAULT)
        return String(encodeValue)
    }

    fun getGsonAsConvert(): Gson {
        return GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Int::class.java, IntDeserializer())
                .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
                .registerTypeAdapter(Double::class.java, DoubleDeserializer())
                .create()
    }


}








