package com.mristudio.blooddonation.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mristudio.blooddonation.R;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;
import static com.mristudio.blooddonation.view.fragment.FindDonnerFragment.addressArray;
import static com.mristudio.blooddonation.view.fragment.FindDonnerFragment.addressofHopital;
import static com.mristudio.blooddonation.view.fragment.FindDonnerFragment.eTaddressOfHospital;

public class AutoLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    SearchView searchView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_location);

        searchView = findViewById(R.id.search);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        // supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(AutoLocationActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        Log.e(TAG, "address " + address.getAddressLine(0));
                        Log.e(TAG, "address " + address);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        FindDonnerActivity.eTaddressOfHospital.setText(location + "," + address);
                        FindDonnerActivity.addressofHopital = location;
                        FindDonnerActivity.addressArray = address;

                    } else {
                        Toasty.warning(AutoLocationActivity.this, "not found !", Toasty.LENGTH_SHORT).show();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        supportMapFragment.getMapAsync(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPriviousActivity();
            }
        });
    }

    private void backToPriviousActivity() {
        onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }


}