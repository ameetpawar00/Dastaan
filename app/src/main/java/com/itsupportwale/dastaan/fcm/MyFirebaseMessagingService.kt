package com.itsupportwale.dastaan.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.activity.MainActivity
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var bitmap: Bitmap? = null

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("p0",p0)
    }
    override fun  onMessageReceived(remoteMessage: RemoteMessage) {
        //  super.onMessageReceived(remoteMessage);



        if (remoteMessage == null) return

        var remoteMessage = remoteMessage.notification!!
        val message = remoteMessage.body
        val title = remoteMessage.title


        var click_action = ""
        if (remoteMessage.clickAction != null) {
            click_action = remoteMessage.clickAction!!
        }

        val powerManager =
                applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        var wakeLock: PowerManager.WakeLock? = null
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "SW::MyWakelockTag"
            )
        }

        wakeLock?.acquire(5000)


        notifyMe(message.toString(), title.toString(), click_action)
    }


    private fun notifyMe(message: String, title: String, click_action: String) {
        //
        var mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*Handle push for oreo*/
        val CHANNEL_ID = "my_channel_01" // The id of the channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence =
                    getString(R.string.app_name) // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mNotificationManager.createNotificationChannel(mChannel)
        }
        var notificationIntent = Intent()

        if (click_action != null && click_action.trim().length > 0) {
            if (click_action.equals("bid_status_outbid_live")) {

                notificationIntent = Intent(this, MainActivity::class.java)
                //notificationIntent.putExtra(PARAM_COMING_FROM, TAB_BAR_LIVE_AUCTION)

            } else if (click_action.equals("bid_status_outbid_hourly")) {

                notificationIntent = Intent(this, MainActivity::class.java)
                //notificationIntent.putExtra(PARAM_COMING_FROM, TAB_BAR_HOURLY_AUCTION)

            } else if (click_action.equals("live_appraisals_new_car_added")) {

                notificationIntent = Intent(this, MainActivity::class.java)
                //notificationIntent.putExtra(PARAM_COMING_FROM, TAB_BAR_LIVE_APPRAISAL)

            } else {
                notificationIntent = Intent(this, MainActivity::class.java)
            }
        } else {
            notificationIntent = Intent(this, MainActivity::class.java)
        }


        notificationIntent.putExtra("click_action", "click_action")
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        //  notificationIntent.putExtra("msg", "" + message);
        val pendingNotificationIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var mBuilder: NotificationCompat.Builder? = null
        try {
            mBuilder = NotificationCompat.Builder(this).setSmallIcon(R.drawable.dastaan).setContentTitle(title).setStyle(NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message)))
                    .setContentText(Html.fromHtml(message))
                    .setLights(Color.RED, 3000, 3000)
                    //.setDeleteIntent(createOnDismissedIntent(this, getUniqueRandomValue()))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
            /*Handle push for oreo*/if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setChannelId(CHANNEL_ID)
            }

            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            mBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        } catch (e: Exception) {
        }
        mBuilder!!.setContentIntent(pendingNotificationIntent)
        //
        mNotificationManager.notify(getUniqueRandomValue(), mBuilder.build())


        val intent = Intent("notification_cast")
        intent.putExtra("key", "new_notification")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        sendMessage();
    }

    private fun sendMessage() {
        Log.d("sender", "Broadcasting message")
        val intent = Intent("custom-event-name")
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    fun getUniqueRandomValue(): Int {
        return ((Date().time / 1000L % Int.MAX_VALUE).toInt())
    }
}