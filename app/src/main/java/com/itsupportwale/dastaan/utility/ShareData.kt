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
import java.io.FileOutputStream

class ShareData(
    private val bitmap: Bitmap,
    private val context: Context,
    private val activity: Activity
) {
    fun shareScreenshots(storyId: String) {
        val fileName = "share.png"
        val dir = File(context.cacheDir, "images")
        dir.mkdirs()
        val file = File(dir, fileName)
        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val uri = FileProvider.getUriForFile(
            context,
            "com.itsupportwale.dastaan.fileprovider", file
        )
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        intent.setDataAndType(uri, context.contentResolver.getType(uri))
        if(!storyId.equals("")){
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share your thoughts, stories, and views/ अपने विचार, कहानियां और विचार साझा करें on \n Dastaan.life https://dastaan.life/story/"+storyId)
            intent.putExtra(Intent.EXTRA_TEXT, "Share your thoughts, stories, and views/ अपने विचार, कहानियां और विचार साझा करें on \n Dastaan.life https://dastaan.life/story/"+storyId)
        }else{
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share your thoughts, stories, and views/ अपने विचार, कहानियां और विचार साझा करें on \n Dastaan.life https://dastaan.life")
            intent.putExtra(Intent.EXTRA_TEXT, "Share your thoughts, stories, and views/ अपने विचार, कहानियां और विचार साझा करें on \n Dastaan.life https://dastaan.life")

        }

        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            activity.startActivity(Intent.createChooser(intent, "Share Image"))
        } catch (e: ActivityNotFoundException) {
            //Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

}
