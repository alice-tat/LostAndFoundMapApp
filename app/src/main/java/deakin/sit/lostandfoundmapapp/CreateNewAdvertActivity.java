package deakin.sit.lostandfoundmapapp;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import deakin.sit.lostandfoundmapapp.database.DatabaseHelper;
import deakin.sit.lostandfoundmapapp.database.PostDataModel;
import deakin.sit.lostandfoundmapapp.databinding.ActivityCreateNewAdvertBinding;

public class CreateNewAdvertActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseHelper dbHelper;
    String selectedType = "";
    LatLng selectedLatLng = null;

    private GoogleMap mMap;
    private ActivityCreateNewAdvertBinding binding;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateNewAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Register launcher
        ActivityResultLauncher<Intent> startAutoComplete = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        LatLng latLng = place.getLatLng();
                        if(latLng != null){
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(place.getName())
                                    .snippet(place.getAddress()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            Toast.makeText(this, "Selected: " + place.getName(), Toast.LENGTH_SHORT).show();
                            binding.inputLocation.setText(place.getName());
                            selectedLatLng = latLng;
                        }
                    }
                }
        );

        Places.initialize(getApplicationContext(),  BuildConfig.MAPS_API_KEY);
        binding.inputLocation.setOnClickListener(v -> {
//            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            Place.Field[] fields = new Place.Field[]{Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS};
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(fields))
                    .build(this);
            startAutoComplete.launch(intent);
        });

        // Set up other components
        dbHelper = new DatabaseHelper(this);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                selectedType = radioButton.getText().toString().toUpperCase();
            }
        });

        binding.getCurrentLocationButton.setOnClickListener(view -> {
            getCurrentLocation();
        });

        binding.saveButton.setOnClickListener(view -> {
            String name = binding.inputName.getText().toString();
            String phone = binding.inputPhone.getText().toString();
            String description = binding.inputDescription.getText().toString();
            String date = binding.inputDate.getText().toString();
            String location = binding.inputLocation.getText().toString();

            if (selectedType.isEmpty() || name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty() || selectedLatLng==null) {
                Toast.makeText(this, "Input fields can not be empty", Toast.LENGTH_SHORT);
            } else {
                // Add advert
                dbHelper.addPost(new PostDataModel(selectedType, name, phone, description, date, location, selectedLatLng.latitude, selectedLatLng.longitude));
                binding.inputName.setText("");
                binding.inputPhone.setText("");
                binding.inputDescription.setText("");
                binding.inputDate.setText("");
                binding.inputLocation.setText("");
                selectedLatLng = null;
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Ask for permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("LOCATION", "Lat: " + latitude + ", Lng: " + longitude);

                        // Get the name
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if (addresses != null && !addresses.isEmpty()) {
                            String placeName = addresses.get(0).getAddressLine(0);
                            binding.inputLocation.setText(placeName);
                        }

                        selectedLatLng = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 10));
                    }
                });
    }
}