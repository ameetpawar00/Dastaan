package com.itsupportwale.dastaan.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by administrator on 28/7/17.
 */

public class DeviceInfo
{
    public static int getDeviceWidth(Activity activity)
    {
        Display display =activity. getWindowManager().getDefaultDisplay();
       int  screenWidth = display.getWidth();
        return screenWidth ;
    }


    public static int getDeviceWidth(Context context)
    {
        Activity activity = (Activity) context ;

        Display display =activity.getWindowManager().getDefaultDisplay();
        int  screenWidth = display.getWidth();
        return screenWidth ;
    }

    public static int getDeviceHeight(Activity activity)
    {
        Display display =activity. getWindowManager().getDefaultDisplay();
        int  screenHeight = display.getHeight();
        return screenHeight ;
    }


    public static int getDensity(Activity activity)
    {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);

        return densityDpi ;

    }


    public static int getImageRatioWidth(Activity activity, int per , Drawable resource ){


        int width = resource.getIntrinsicWidth();
        int height = resource.getIntrinsicHeight();

        int width1 = (getDeviceWidth(activity)*per)/100;

        double ratio = width / width1 ;

        double height1 = height / ratio ;





        return width1;

    }

    public static Drawable resizeDrawable(Activity activity, int per ,Drawable image) {

        int width = image.getIntrinsicWidth();
        int height = image.getIntrinsicHeight();

        int width1 = (getDeviceWidth(activity)*per)/100;

        double ratio = width / width1 ;

        double height1 = height / ratio ;
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width1, (int) height1, false);
        return new BitmapDrawable(activity.getResources(), bitmapResized);
    }


    public static Drawable resizeDrawable(Context activity, int per ,Drawable image) {

        int width = image.getIntrinsicWidth();
        int height = image.getIntrinsicHeight();

        int width1 = (getDeviceWidth(activity)*per)/100;

        double ratio = width / width1 ;

        double height1 = height / ratio ;
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width1, (int) height1, false);
        return new BitmapDrawable(activity.getResources(), bitmapResized);
    }


    public static int getImageRatioHeight(Activity activity, int per ,Drawable resource ){


        int width = resource.getIntrinsicWidth();
        int height = resource.getIntrinsicHeight();

        int width1 = (getDeviceWidth(activity)*per)/100;

        double ratio = width / width1 ;

        int height1 = (int)(height / ratio) ;




        return height1;

    }

}
