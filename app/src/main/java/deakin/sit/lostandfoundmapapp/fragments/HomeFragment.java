package deakin.sit.lostandfoundmapapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import deakin.sit.lostandfoundmapapp.CreateNewAdvertActivity;
import deakin.sit.lostandfoundmapapp.MainActivity;
import deakin.sit.lostandfoundmapapp.MapsActivity;
import deakin.sit.lostandfoundmapapp.R;

public class HomeFragment extends Fragment {
    Button createAdvertButton, showAllItemButton, showOnMapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        createAdvertButton = view.findViewById(R.id.createAdvertButton);
        showAllItemButton = view.findViewById(R.id.showAllItemButton);
        showOnMapButton = view.findViewById(R.id.showOnMapButton);

        createAdvertButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CreateNewAdvertActivity.class);
            startActivity(intent);
        });
        showAllItemButton.setOnClickListener(view1 -> {
            ((MainActivity) getActivity()).toALlLostAndFoundFragment();
        });
        showOnMapButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });
        return view;
    }
}