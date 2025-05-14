package deakin.sit.lostandfoundmapapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;

import deakin.sit.lostandfoundmapapp.databinding.ActivityCreateNewAdvertBinding;

public class CreateNewAdvertActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseHelper dbHelper;
    String selectedType = "";

    private GoogleMap mMap;
    private ActivityCreateNewAdvertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateNewAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        });

        binding.saveButton.setOnClickListener(view -> {
            String name = binding.inputName.getText().toString();
            String phone = binding.inputPhone.getText().toString();
            String description = binding.inputDescription.getText().toString();
            String date = binding.inputDate.getText().toString();
            String location = binding.inputLocation.getText().toString();

            if (selectedType.isEmpty() || name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Input fields can not be empty", Toast.LENGTH_SHORT);
            } else {
                // Add advert
                dbHelper.addPost(new PostDataModel(selectedType, name, phone, description, date, location));
                binding.inputName.setText("");
                binding.inputPhone.setText("");
                binding.inputDescription.setText("");
                binding.inputDate.setText("");
                binding.inputLocation.setText("");
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
}