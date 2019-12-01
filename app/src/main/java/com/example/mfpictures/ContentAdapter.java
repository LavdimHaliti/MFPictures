package com.example.mfpictures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
This class is used to create the adapter the recyclerView and the OnItemClickListener interface
and to this class is created only for the DisplayPhotosActivity class
and procedure is basically the same as with ItemClickAdapter class
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ItemViewHolder>{


    private ArrayList<ContentUrl> contentList;
    private Context context;


    private ContentAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ContentAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public ContentAdapter(Context context, ArrayList<ContentUrl> contentList){
        this.context = context;
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public ContentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_content, parent, false);
        return new ContentAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        ContentUrl currentItem = contentList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String userName = currentItem.getUserName();
        int likes = currentItem.getLikes();

        Picasso.get()
                .load(imageUrl)
                .noFade()
                .fit()
                .centerInside()
                .into(holder.contentImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        MaterialImageLoading.animate(holder.contentImageView).setDuration(3000).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
        holder.userName.setText(userName);
        holder.likes.setText("" + likes);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView contentImageView;
        public TextView userName;
        public TextView likes;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            contentImageView= itemView.findViewById(R.id.image_view_content);
            userName = itemView.findViewById(R.id.text_view_user);
            likes = itemView.findViewById(R.id.text_view_likes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener != null){

                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
