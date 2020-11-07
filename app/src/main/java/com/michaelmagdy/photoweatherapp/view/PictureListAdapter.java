package com.michaelmagdy.photoweatherapp.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.michaelmagdy.photoweatherapp.R;
import com.michaelmagdy.photoweatherapp.model.localdatabase.Picture;

import java.util.Arrays;

public class PictureListAdapter extends ListAdapter<Picture, PictureListAdapter.PictureViewHolder> {


    protected PictureListAdapter() {
        super(diffCallback);
    }

    public static final DiffUtil.ItemCallback<Picture> diffCallback =
            new DiffUtil.ItemCallback<Picture>() {
                @Override
                public boolean areItemsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Picture oldItem, @NonNull Picture newItem) {
                    return Arrays.equals(oldItem.getImage(), newItem.getImage());
                }
            };

    public static Bitmap convertByteArrayToBitmap(byte[] image) {

        Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0, image.length);
        return bitmap;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new PictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {

        holder.imageView.setImageBitmap(convertByteArrayToBitmap(getItem(position).getImage()));
    }

    class PictureViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_list_item);
        }
    }
}
