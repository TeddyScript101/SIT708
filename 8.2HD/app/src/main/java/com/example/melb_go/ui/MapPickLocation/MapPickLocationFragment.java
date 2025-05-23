package com.example.melb_go.ui.MapPickLocation;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.melb_go.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapPickLocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng selectedLatLng;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_pick_location, container, false);

        btnConfirm = view.findViewById(R.id.btnConfirmLocation);
        btnConfirm.setEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapPickLocationFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnConfirm.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                Bundle result = new Bundle();
                result.putDouble("selectedLat", selectedLatLng.latitude);
                result.putDouble("selectedLng", selectedLatLng.longitude);
                getParentFragmentManager().setFragmentResult("locationSelected", result);

                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultLoc = new LatLng(-37.8136, 144.9631);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 10));

        mMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            mMap.clear();


            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            String addressText = "Selected Location";
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    addressText = address.getAddressLine(0); // Full address
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Selected Location")
                            .snippet(addressText))
                    .showInfoWindow();

            btnConfirm.setEnabled(true);
        });

    }
}

