package com.example.lost_and_found;

import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.example.lost_and_found.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private AdvertDao advertDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup database
        AdvertDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AdvertDatabase.class, "advert_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        advertDao = db.advertDao();

        // Load the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<Advert> adverts = advertDao.getAllAdverts();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean hasValidMarkers = false;

        for (Advert advert : adverts) {
            double lat = advert.getLatitude();
            double lng = advert.getLongitude();
            Log.d("LAT_LNG", advert.getName() + " -> Lat: " + lat + ", Lng: " + lng);

            if (lat != 0.0 && lng != 0.0) {
                LatLng position = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(advert.getType() + ": " + advert.getName())
                        .snippet(advert.getDescription()));
                builder.include(position);
                hasValidMarkers = true;
            }
        }

        if (hasValidMarkers) {
            // Animate camera to show all markers with padding
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // 100px padding
        } else {
            LatLng defaultLocation = new LatLng(-37.810100451547555, 144.9630334161249);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5f));
        }
    }
}
