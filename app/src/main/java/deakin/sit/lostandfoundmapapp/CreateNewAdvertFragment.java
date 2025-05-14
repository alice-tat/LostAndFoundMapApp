package deakin.sit.lostandfoundmapapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class CreateNewAdvertFragment extends Fragment implements OnMapReadyCallback {
    RadioGroup radioGroup;
    EditText inputName, inputPhone, inputDescription, inputDate, inputLocation;
    Button saveButton, getCurrentLocationButton;

    DatabaseHelper dbHelper;

    String selectedType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_advert, container, false);

        dbHelper = new DatabaseHelper(getContext());

        radioGroup = view.findViewById(R.id.radioGroup);
        inputName = view.findViewById(R.id.inputName);
        inputPhone = view.findViewById(R.id.inputPhone);
        inputDescription = view.findViewById(R.id.inputDescription);
        inputDate = view.findViewById(R.id.inputDate);
        inputLocation = view.findViewById(R.id.inputLocation);

        saveButton = view.findViewById(R.id.saveButton);
        getCurrentLocationButton = view.findViewById(R.id.getCurrentLocationButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) view.findViewById(checkedId);
                selectedType = radioButton.getText().toString().toUpperCase();
            }
        });

        saveButton.setOnClickListener(view1 -> {

            String name = inputName.getText().toString();
            String phone = inputPhone.getText().toString();
            String description = inputDescription.getText().toString();
            String date = inputDate.getText().toString();
            String location = inputLocation.getText().toString();

            if (selectedType.isEmpty() || name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                Toast.makeText(getContext(), "Input fields can not be empty", Toast.LENGTH_SHORT);
            } else {
                // Add advert
                dbHelper.addPost(new PostDataModel(selectedType, name, phone, description, date, location));
                inputName.setText("");
                inputPhone.setText("");
                inputDescription.setText("");
                inputDate.setText("");
                inputLocation.setText("");
                ((MainActivity) getActivity()).toHomeFragment();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }
}