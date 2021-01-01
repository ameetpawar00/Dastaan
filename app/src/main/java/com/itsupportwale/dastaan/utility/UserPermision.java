package com.itsupportwale.dastaan.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.itsupportwale.dastaan.R;
import com.itsupportwale.dastaan.interfaces.AppPermissionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Gehlot on 7/7/2018.
 */

public class UserPermision {
    static String[] notPermittedPermissionList;

    public static boolean checkPermission(final Activity context, final String[] exteranalStorage, final int
            exteranalStorageRequestCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        ArrayList<String> notPermittedPermissionArrayList;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            boolean shouldShowRequestPermissionRationale = false;
            boolean isAnyRequestNotPermitted = false;
            notPermittedPermissionArrayList = new ArrayList<>();
            for (String permission : exteranalStorage) {
                if (isPermissionGranted(context, permission) == false) {
                    isAnyRequestNotPermitted = true;
                    notPermittedPermissionArrayList.add(permission);

                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    shouldShowRequestPermissionRationale = true;
                }
            }

            notPermittedPermissionList = new String[notPermittedPermissionArrayList.size()];
            int i = 0;
            for (String notPermittedPermission : notPermittedPermissionArrayList) {
                notPermittedPermissionList[i] = notPermittedPermission;
                i++;
            }


            if (isAnyRequestNotPermitted) {
                if (shouldShowRequestPermissionRationale) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(context.getString(R.string.permission_necessary));
                    alertBuilder.setMessage(context.getString(R.string.tit_permission_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, notPermittedPermissionList,
                                    exteranalStorageRequestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(context, notPermittedPermissionList,
                            exteranalStorageRequestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPermission(final Activity context,
                                          final ArrayList<String> exteranalStorage, final int
                                                  exteranalStorageRequestCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        ArrayList<String> notPermittedPermissionArrayList;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            boolean shouldShowRequestPermissionRationale = false;
            boolean isAnyRequestNotPermitted = false;
            notPermittedPermissionArrayList = new ArrayList<>();
            for (String permission : exteranalStorage) {
                if (isPermissionGranted(context, permission) == false) {
                    isAnyRequestNotPermitted = true;
                    notPermittedPermissionArrayList.add(permission);

                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    shouldShowRequestPermissionRationale = true;
                }
            }

            notPermittedPermissionList = new String[notPermittedPermissionArrayList.size()];
            int i = 0;
            for (String notPermittedPermission : notPermittedPermissionArrayList) {
                notPermittedPermissionList[i] = notPermittedPermission;
                i++;
            }


            if (isAnyRequestNotPermitted) {
                if (shouldShowRequestPermissionRationale) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(context.getString(R.string.permission_necessary));
                    alertBuilder.setMessage(context.getString(R.string.tit_permission_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, notPermittedPermissionList,
                                    exteranalStorageRequestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(context, notPermittedPermissionList,
                            exteranalStorageRequestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkPermission(final Activity context,
                                          final String exteranalStorage,
                                          final int exteranalStorageRequestCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        ArrayList<String> notPermittedPermissionArrayList;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            boolean shouldShowRequestPermissionRationale = false;
            boolean isAnyRequestNotPermitted = false;
            notPermittedPermissionArrayList = new ArrayList<>();


            if (isPermissionGranted(context, exteranalStorage) == false) {
                isAnyRequestNotPermitted = true;
                notPermittedPermissionArrayList.add(exteranalStorage);

            }


            notPermittedPermissionList = new String[notPermittedPermissionArrayList.size()];
            int i = 0;
            for (String notPermittedPermission : notPermittedPermissionArrayList) {
                notPermittedPermissionList[i] = notPermittedPermission;
                i++;
            }


            if (isAnyRequestNotPermitted) {
                if (shouldShowRequestPermissionRationale) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(context.getString(R.string.permission_necessary));
                    alertBuilder.setMessage(context.getString(R.string.tit_permission_necessary));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, notPermittedPermissionList,
                                    exteranalStorageRequestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(context, notPermittedPermissionList,
                            exteranalStorageRequestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean isPermissionGranted(Activity activity, String exteranalStorage) {
        return ContextCompat.checkSelfPermission(activity, exteranalStorage) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean isPermissionGranted(Context activity, String exteranalStorage) {
        return ContextCompat.checkSelfPermission(activity, exteranalStorage) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermission(final Activity context,
                                          final ArrayList<String> exteranalStorage) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            for (String permission : exteranalStorage) {
                if (!isPermissionGranted(context, permission)) {
                    return false;

                }

            }

            return true;


        } else {
            return true;
        }
    }

    public static boolean checkPermission(final Context context,
                                          final ArrayList<String> exteranalStorage) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            for (String permission : exteranalStorage) {
                if (isPermissionGranted(context, permission) == false) {
                    return false;

                }

            }

            return true;


        } else {
            return true;
        }
    }

    public static void requestForCheckedAndPermission(final Activity context, final ArrayList<String> exteranalStorage, final
    AppPermissionListener permissionListener) {

        int currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion < Build.VERSION_CODES.M || checkPermission(context , exteranalStorage)) {

            permissionListener.OnAllPermissionsGranted(true);

            return;
        }
        Dexter.withActivity(context)
                .withPermissions(exteranalStorage)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        permissionListener.OnAllPermissionsGranted(report.areAllPermissionsGranted());

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                 final PermissionToken token) {




                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(context.getString(R.string.permission_necessary));
                        alertBuilder.setMessage(context.getString(R.string.tit_permission_necessary));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                token.continuePermissionRequest();

                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();









                    }
                }).check();

                /*withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken
                    token) {
                        token.continuePermissionRequest();
                    }
                }).check();*/
    }

    public static void openActivityForGpsON(final Activity activity ,final int requestCode)
    {
        LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setTitle(activity.getString(R.string.gps_network_not_enabled));
            dialog.setMessage(activity.getResources().getString(R.string.open_location_settings));
            dialog.setPositiveButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(myIntent,requestCode);
                    //get gps
                }
            });
            dialog.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    paramDialogInterface.dismiss();
                }
            });
            dialog.show();
        }
    }

    public static boolean canGetLocationFromInterNet(Activity activity)
    {

        LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}


        return network_enabled ;
    }


    public static boolean canGetLocationFromInterGps(Activity activity)
    {

        LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Log.e("exception ", ex.getMessage());
            return gps_enabled ;
        }

        return gps_enabled ;
    }

    public static boolean canGetLocation(Activity activity)
    {
        return canGetLocationFromInterNet(activity);
    }
}
