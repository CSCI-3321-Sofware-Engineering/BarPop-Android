package com.aymanbagabas.barpop;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFusedLocationClient = ((MainActivity)getActivity()).getmFusedLocationClient();

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Permissions check
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            ((MainActivity)getActivity()).startLocationUpdates();

                            
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                            Log.d("success" + location.getLatitude(), " 1");
                        }
                    }
                });
        // Add sample locations
        prototype(mMap);
    }

    private void prototype(GoogleMap map) {

        ArrayList<String> placesNames = new ArrayList<>();
        placesNames.add("Congress Street Social Club");
        placesNames.add("Club Elan");
        placesNames.add("Wet Willie's");
        placesNames.add("Club 51 Degrees");
        placesNames.add("El-Rocko Lounge");
        ArrayList<LatLng> placesLocations = new ArrayList<>();
        placesLocations.add(new LatLng(32.080859, -81.096605));
        placesLocations.add(new LatLng(32.082420, -81.094509));
        placesLocations.add(new LatLng(32.080966, -81.094771));
        placesLocations.add(new LatLng(32.079899, -81.093446));
        placesLocations.add(new LatLng(32.078666, -81.093394));

        for (int i = 0; i < placesNames.size(); i++) {
            int population = (int)(Math.random() * 100);
            BitmapDescriptor color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            if (population < 30)
                color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            else if (population <= 60)
                color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

            map.addMarker(new MarkerOptions()
                    .position(placesLocations.get(i))
                    .title(placesNames.get(i))
                    .snippet(String.format("Population: %d", population))
                    .icon(color)
            );
        }
    }
}
// public class MapsMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
//    // Include the OnCreate() method here too, as described above.
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    // Retrieve the content view that renders the map.
//    setContentView(R.layout.activity_maps);
//    // Get the SupportMapFragment and request notification
//    // when the map is ready to be used.
//    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//            .findFragmentById(R.id.googleMap);
//    mapFragment.getMapAsync(this);
//}
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // Add a marker in Sydney, Australia,
//        // and move the map's camera to the same location.
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//}