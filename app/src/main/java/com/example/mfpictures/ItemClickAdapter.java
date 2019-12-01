package com.example.mfpictures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

/*
This class is used to create the adapter the recyclerView and the OnItemClickListener interface
and to this class is created only for the ItemActivity class
 */
public class ItemClickAdapter extends RecyclerView.Adapter<ItemClickAdapter.ItemViewHolder> {

    private ArrayList<ItemUrl> itemList;
    private Context context;
    private OnItemClickListener listener;

    /*
    OnItemClickListener is the interface that handles the clicks
    and has a method called onItemClick with a parameter which will
    be used to assign the current position of an item in the recyclerView
     */
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    //The constructor that is used to take the context and the arrayList
    public ItemClickAdapter(Context context, ArrayList<ItemUrl> itemList){
        this.context = context;
        this.itemList = itemList;
        this.listener = (OnItemClickListener) context;
    }

    /*
    This method is used to create the viewHolder by taking the content from the layout res folder
    and assign it to the View
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_url, parent, false);
        return new ItemViewHolder(view);
    }

    /*
    This method is used to bind the views to the recyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

        ItemUrl currentItem = itemList.get(position);

        String pictureName = currentItem.getItemPictureName();
        int itemPicture = currentItem.getItemPicture();

        holder.pictureContent.setImageResource(itemPicture);
        holder.pictureNameText.setText(pictureName);
    }

    /*
    This method keeps count of items in the list
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /*
    In this class the views are created and assigned and used by the onBindViewHolder method
    We also use the View class to sort of activate the click listener and makes it possible for the items
    in the recyclerView to be clickable by getting the adapter position and taking that position and put it
    in the onItemClick interface method
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView pictureNameText;
        public ImageView pictureContent;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            pictureNameText = itemView.findViewById(R.id.text_view_picture_name);
            pictureContent =  itemView.findViewById(R.id.image_view_picture);

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
