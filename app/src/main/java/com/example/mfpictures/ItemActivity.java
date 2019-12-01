package com.example.mfpictures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import java.util.ArrayList;

import static com.example.mfpictures.DisplayPhotosActivity.CATEGORY_KEY;

/*
ItemActivity deals with displaying the categories of pictures that have been chosen
using a recycleView to arrange them as a list and by implementing the ItemClickAdapter.OnItemClickListener
interface giving the ability for the items in the list to be clickable in order to jump to their designated
Activities that displays the content that a certain category has to offer
 */
public class ItemActivity extends AppCompatActivity implements ItemClickAdapter.OnItemClickListener {

    //The classes used to create the recycleView, the Adapter class which makes it possible
    //for the content of the list to be sent to the recyclerView and then displayed on the screen
    private RecyclerView recyclerView;
    private ItemClickAdapter clickAdapter;
    private ArrayList<ItemUrl> itemList;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //We have taken the content from res folders and assigned them in to different variables
        itemList = new ArrayList<>();
        String[] categoryList = getResources().getStringArray(R.array.category_keys);
        String[] pictureNames = getResources().getStringArray(R.array.picture_names);
        TypedArray drawables = getResources().obtainTypedArray(R.array.drawables);

        //Using the for loop we can iterate through each of the content provided
        //and then added to the arrayList by calling the constructor of ItemUrl class
        //which actually works as a custom adapter Class
        for (int i = 0; i < categoryList.length; i++) {

            itemList.add(new ItemUrl(categoryList[i], drawables.getResourceId(i, 0), pictureNames[i]));

        }

        //Using the ItemClickAdapter class that is used to create
        // the adapter for the recyclerView and the recyclerView it self,
        //we call it by giving it the context and the arrayList  as parameters of the constructor
        clickAdapter = new ItemClickAdapter(ItemActivity.this, itemList);

        //And then ItemClickAdapter is called by the recyclerView
        recyclerView.setAdapter(clickAdapter);

    }

    /*
    OnItemClick is an overridden method that its been created to handle the click events
    by giving a parameter to cary the position of each item in the recyclerView.
    In this case is used to jump from this activity to DisplayPhotosActivity
    Bundle is used to store the categories and send them to the next activity
     */
    @Override
    public void onItemClick(int position) {

        intent = new Intent(ItemActivity.this, DisplayPhotosActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(
                CATEGORY_KEY,
                itemList.get(position).getCategory());

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
