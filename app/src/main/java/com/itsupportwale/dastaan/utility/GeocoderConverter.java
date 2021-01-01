package com.itsupportwale.dastaan.utility;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocoderConverter
{
    public static void   getLocationFromAddress(Activity activity , String strAddress , Double latitideObj , Double longitudeObj){
        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress,5);
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            longitudeObj = location.getLongitude()  ;
            latitideObj = location.getLatitude()  ;
        }
        catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
