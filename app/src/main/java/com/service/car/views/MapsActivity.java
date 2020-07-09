package com.service.car.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.service.car.R;
import com.service.car.utils.Constants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker locationMarker;
    private Location selectedLocation;

    private static final int LOCATION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    selectedLocation = location;
                    Toast.makeText(MapsActivity.this, currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                } else {
                    Toast.makeText(MapsActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Try to obtain the map from the SupportMapFragment.
        mMap.setMyLocationEnabled(true);
        if (currentLocation != null) {
            selectedLocation = currentLocation;
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Vehicle is Here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            //Adding the created the marker on the map
            locationMarker = mMap.addMarker(markerOptions);
            locationMarker.showInfoWindow();
            //Map Zoom to location
            CameraUpdate center= CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Log.e("ERROR ","LOCATION PERMISSION MISSING");
                }
                break;
            }
        }


    @Override
    public void onBackPressed() {
        Log.v(TAG,"back pressed");
        Intent intent=new Intent();
        intent.putExtra("lat",selectedLocation.getLatitude());
        intent.putExtra("lng",selectedLocation.getLongitude());
        setResult(Constants.LATLNGREQUEST,intent);
        super.onBackPressed();
    }
}
