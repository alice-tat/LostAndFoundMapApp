package deakin.sit.lostandfoundmapapp.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import deakin.sit.lostandfoundmapapp.MainActivity;
import deakin.sit.lostandfoundmapapp.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<PostDataModel> postDataModelList;
    Fragment fragment;

    public PostAdapter(List<PostDataModel> postDataModelList, Fragment fragment) {
        this.postDataModelList = postDataModelList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostDataModel post = postDataModelList.get(position);

        holder.postInfoText.setText("[" + post.getType().toUpperCase() + "]: " + post.getName());
        holder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("PostID", post.getId());
            ((MainActivity) fragment.getActivity()).toPostDetailFragment(bundle);
        });
    }

    @Override
    public int getItemCount() {
        return postDataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView postInfoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postInfoText = itemView.findViewById(R.id.postInfoText);
        }
    }
}
