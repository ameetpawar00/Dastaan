package com.itsupportwale.dastaan.utility

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import com.itsupportwale.dastaan.R
import java.io.File

class ShareData(
    private val rootView: View?,
    private val activity: Activity,
    private val context: Context?
) {
    fun ShareImageData(deeplink: String) {
        var linkAppMain =
            "http://play.google.com/store/apps/details?id=" + context!!.packageName
        if (deeplink != "null") {
            linkAppMain = deeplink
            if (rootView != null && context != null) { //&& !context.isFinishing()
                rootView.isDrawingCacheEnabled = true
                val bitmap = Bitmap.createBitmap(rootView.drawingCache)
                if (bitmap != null) {
                    val mediaStorageDir =
                        File(activity.externalCacheDir.toString() + "Image.png")
                    /* FileOutputStream outputStream = new FileOutputStream(String.valueOf(mediaStorageDir));
                     bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                     outputStream.close();*/
                    val imageUri = FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        mediaStorageDir
                    )
                    val waIntent = Intent(Intent.ACTION_SEND)
                    waIntent.type = "image/*"
                    waIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    waIntent.putExtra(Intent.EXTRA_TITLE, "Validate Your News")
                    waIntent.putExtra(Intent.EXTRA_SUBJECT, "Is It Fake News")
                    waIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Check Your News Is Real or Fake : Install Now $linkAppMain"
                    )
                    activity.startActivity(Intent.createChooser(waIntent, "Share with"))
                }
            }
        } else {
            if (rootView != null && context != null) {
                try {
                    try {
                        val imageUri: Uri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            imageUri = Uri.parse(
                                MediaStore.Images.Media.insertImage(
                                    context.contentResolver,
                                    BitmapFactory.decodeResource(
                                        context.resources,
                                        R.drawable.download
                                    ), null, null
                                )
                            )
                        } else {
                            rootView.isDrawingCacheEnabled = true
                            val bitmap = Bitmap.createBitmap(rootView.drawingCache)
                            val mediaStorageDir = File(
                                activity.externalCacheDir.toString() + "Image.png"
                            )
                            imageUri = FileProvider.getUriForFile(
                                context,
                                context.packageName + ".provider",
                                mediaStorageDir
                            )
                        }
                        val share = Intent(Intent.ACTION_SEND)
                        share.type = "image/*"
                        share.putExtra(Intent.EXTRA_STREAM, imageUri)
                        share.putExtra(Intent.EXTRA_TITLE, "Is It Fake News")
                        share.putExtra(Intent.EXTRA_SUBJECT, "Is It Fake News")
                        share.putExtra(
                            Intent.EXTRA_TEXT,
                            "Check Is It Fake News... Please Do share and provide feedback Install Now $linkAppMain"
                        ) //todo add link for app
                        activity.startActivity(Intent.createChooser(share, "Share Using"))
                    } catch (e: NullPointerException) {
                    }
                    // Launch the Google+ share dialog with attribution to your app.
                } catch (ex: ActivityNotFoundException) {
                    //   Toast.makeText(mActivity, mActivity.getString(R.string.share_google_plus_not_installed), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
