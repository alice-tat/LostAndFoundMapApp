package deakin.sit.lostandfoundmapapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import deakin.sit.lostandfoundmapapp.database.DatabaseHelper;
import deakin.sit.lostandfoundmapapp.database.PostAdapter;
import deakin.sit.lostandfoundmapapp.database.PostDataModel;
import deakin.sit.lostandfoundmapapp.R;

public class AllLostAndFoundFragment extends Fragment {
    RecyclerView recyclerView;
    PostAdapter postAdapter;

    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_lost_and_found, container, false);

        dbHelper = new DatabaseHelper(getContext());

        List<PostDataModel> postDataModelList = dbHelper.getAllPosts();

        postAdapter = new PostAdapter(postDataModelList, this);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);

        return view;
    }
}