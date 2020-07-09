package com.service.car.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.service.car.R;
import com.service.car.utils.Constants;
import com.service.car.utils.NetworkUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LocationManager locationManager;
    private TelephonyManager telephonyManager;
    private static final int REQUEST_LOCATION = 123;
    private TextView tv_location;
    private EditText etPhoneNumber1;
    private EditText etPhoneNumber2;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private boolean internetEnabled = false;
    private String phoneNumber1;
    private String phoneNumber2;
    private List<SubscriptionInfo> subscription;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews(this);
        findViewById(R.id.iv_selectlocation).setOnClickListener(this);
        findViewById(R.id.btn_service_request).setOnClickListener(this);

        if (!internetEnabled) {
            showDialogAlert(this, Constants.INTERNET);
        } else if (!network_enabled && !gps_enabled) {
            showDialogAlert(this, Constants.GPS);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            Log.v(TAG," -->> phine "+ mPhoneNumber);

            Log.v(TAG, "All Permissions Granted");
            getUserDetails(this);

        } else {
            if (Build.VERSION.SDK_INT > 28) {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.READ_PHONE_NUMBERS,
                                Manifest.permission.READ_PHONE_STATE},
                        REQUEST_LOCATION);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.READ_PHONE_NUMBERS,
                                Manifest.permission.READ_PHONE_STATE},
                        REQUEST_LOCATION);
            }
        }
    }

    private void initViews(Context context) {
        tv_location = findViewById(R.id.et_location);
        etPhoneNumber1 = findViewById(R.id.et_primarynumber);
        etPhoneNumber2 = findViewById(R.id.et_secondarynumber);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        internetEnabled = NetworkUtil.getInstance().isNetworkConnected(context);
    }

    private void showDialogAlert(Context context, final int type) {
        String dialogTitle = "";
        String dialogMessage = "";
        String positiveTitle;
        String cancelTitle;
        if (type == 1) {
            dialogTitle = "NO INTERNET";
            dialogMessage = "This App needs Network";
        } else if (type == 2) {
            dialogTitle = "NO GPS";
            dialogMessage = "This App needs GPS";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Turn it on from Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callIntent = null;
                                if (type == 1) {
                                    callIntent = new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS);
                                } else if (type == 2) {
                                    callIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                }
                                startActivityForResult(callIntent, Constants.PERMISSIONSREQUEST);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void getUserDetails(Context context) {
        Log.v(TAG," -->> size "+subscription.toString());

        for (int i = 0; i < subscription.size(); i++) {
            SubscriptionInfo info = subscription.get(i);
            Log.d(TAG, "number " + info.getNumber());
            Log.d(TAG, "network name : " + info.getCarrierName());
            Log.d(TAG, "country iso " + info.getCountryIso());
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    tv_location.setText(getAddress(location.getLatitude(), location.getLongitude()));
                } else {
                    Toast.makeText(MainActivity.this, "Select Location from Map", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        tv_location.setText(getAddress(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Location permissions granted, starting location");
                getUserDetails(getApplicationContext());

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                System.out.println("Location permissions Denied");
            }
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_service_request:
                Toast.makeText(MainActivity.this, "Thankyou, Your Request id is 12324 \n Our team will serve you better in short time", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.iv_selectlocation:
                startActivityForResult(new Intent(MainActivity.this, MapsActivity.class), Constants.LATLNGREQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.LATLNGREQUEST) {
            double latValue = data.getDoubleExtra("lat", 0.0);
            double lngValue = data.getDoubleExtra("lng", 0.0);
            tv_location.setText(getAddress(latValue, lngValue));
        } else if (requestCode == Constants.PERMISSIONSREQUEST) {
            initViews(getApplicationContext());
        }
    }
}
