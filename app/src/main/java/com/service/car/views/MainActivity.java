package com.service.car.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 123;
    private EditText et_location;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean internetEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews(this);
        findViewById(R.id.iv_selectlocation).setOnClickListener(this);
        findViewById(R.id.btn_service_request).setOnClickListener(this);

        if (!network_enabled && !gps_enabled) {
            showDialogAlert(this, Constants.GPS);
        } else if (!internetEnabled) {
            showDialogAlert(this, Constants.INTERNET);
        }else if (network_enabled && gps_enabled) {

            if (Build.VERSION.SDK_INT > 28 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {
                // permission granted
                getUserLocation(this);
            }
        }else {
            finish();
        }

    }


    private void initViews(Context context) {
        et_location = findViewById(R.id.et_location);
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
                                startActivity(callIntent);
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


    private void getUserLocation(Context context) {
        System.out.println("-->>> in method");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    et_location.setText(getAddress(location.getLatitude(), location.getLongitude()));
                } else {
                    Toast.makeText(MainActivity.this, "Select Location from Map", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        et_location.setText(getAddress(location.getLatitude(), location.getLongitude()));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Location permissions granted, starting location");
                getUserLocation(getApplicationContext());

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
            Toast.makeText(this, "Address=>" + add, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_service_request:
                Toast.makeText(MainActivity.this, "Thankyou, Your Request id is 12324 \n Our team will serve you better in short time", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.iv_selectlocation:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
        }
    }
}
