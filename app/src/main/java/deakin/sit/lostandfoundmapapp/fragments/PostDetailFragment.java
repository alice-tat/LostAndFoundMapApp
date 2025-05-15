package deakin.sit.lostandfoundmapapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import deakin.sit.lostandfoundmapapp.database.DatabaseHelper;
import deakin.sit.lostandfoundmapapp.database.PostDataModel;
import deakin.sit.lostandfoundmapapp.R;

public class PostDetailFragment extends Fragment {
    DatabaseHelper dbHelper;

    TextView typeText, nameText, contactText, dateText, locationText, descriptionText;
    Button removeButton;

    PostDataModel post;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        dbHelper = new DatabaseHelper(getContext());

        typeText = view.findViewById(R.id.typeText);
        nameText = view.findViewById(R.id.nameText);
        contactText = view.findViewById(R.id.contactText);
        dateText = view.findViewById(R.id.dateText);
        locationText = view.findViewById(R.id.locationText);
        descriptionText = view.findViewById(R.id.descriptionText);

        removeButton = view.findViewById(R.id.removeButton);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int postId = bundle.getInt("PostID");
            post = dbHelper.getPost(postId);

            typeText.setText(post.getType());
            nameText.setText(post.getName());
            contactText.setText(post.getPhone());
            dateText.setText(post.getDate());
            locationText.setText(post.getLocation());
            descriptionText.setText(post.getDescription());
        }

        removeButton.setOnClickListener(view1 -> {
            if (post != null) {
                dbHelper.deletePost(post.getId());
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }
}