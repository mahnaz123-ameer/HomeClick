package csedu.homeclick.androidhomeclick.navigator;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import csedu.homeclick.androidhomeclick.R;

public class ImageRecyclerViewAdapter extends  RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder>{
    private Context context;
    List<?> imageURL = new ArrayList<>();

    public ImageRecyclerViewAdapter() {

    }

    public ImageRecyclerViewAdapter(Context context, List<?> imageURL) {
        this.context = context;
        this.imageURL = imageURL;
    }



    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_space, parent, false);
        ImageRecyclerViewAdapter.ViewHolder holder = new ImageRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if(imageURL.get(position).getClass().equals(String.class)) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(context).load(imageURL.get(position)).apply(options).into(holder.imageView);
        } else {
            Glide.with(context).load(imageURL.get(position)).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return imageURL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.ad_photos);
        }
    }
}
