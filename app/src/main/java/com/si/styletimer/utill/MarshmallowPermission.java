package com.si.styletimer.utill;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MarshmallowPermission {

    public static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 222;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 333;
    public static final int REQUEST_CAMERA_PERMISSION = 444;
    public static final int REQUEST_LOCATION_PERMISSION = 555;
    public static final int REQUEST_READ_PHONE_STATE_PERMISSION = 666;
    public static final int REQUEST_READ_CONTACT_PERMISSION = 777;
    public static final int REQUEST_GET_ACCOUNTS_PERMISSION = 888;
    public static final int REQUEST_READ_SMS_PERMISSION = 999;
    public static final int REQUEST_RECEIVE_SMS_PERMISSION = 1122;
    public static final int REQUEST_CALL_PHONE_PERMISSION = 1133;
    public static final int REQUEST_GPS_PERMISSION = 1155;
    public static final int REQUEST_RECORD_PERMISSION = 111;

    private final Context mContext;
    Fragment fragment;

    public MarshmallowPermission(Context mContext, Fragment fragment) {
        this.mContext = mContext;
        this.fragment = fragment;
    }


    public MarshmallowPermission(Context mContext) {
        this.mContext = mContext;
    }




    @TargetApi(Build.VERSION_CODES.Q)
    public boolean check_read_external_storage_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean check_location_Permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check_camera_Permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                  /*  Toast.makeText(mContext, "toast else", Toast.LENGTH_SHORT).show();

                    final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("Permissions Required")
                            .setMessage("You have forcefully denied some of the required permissions " +
                                    "for this action. Please open settings, go to permissions and allow them.")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", mContext.getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();*/

                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check_write_Permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean check_record_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.RECORD_AUDIO)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check_read_phone_state_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_PHONE_STATE)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean check_read_contact_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check_get_account_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.GET_ACCOUNTS)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNTS_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNTS_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check_read_sms_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_SMS)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_SMS}, REQUEST_READ_SMS_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_SMS}, REQUEST_READ_SMS_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check_receive_sms_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.RECEIVE_SMS)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_RECEIVE_SMS_PERMISSION);

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_RECEIVE_SMS_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public boolean check__call_phone_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}


/****************************************Refereence*******************************************************/
   /* public  boolean check__call_phone_permission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (  ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }*/

