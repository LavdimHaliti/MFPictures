package com.example.mfpictures;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mfpictures.DetailActivity.EXTRA_COMMENTS;
import static com.example.mfpictures.DetailActivity.EXTRA_DOWNLOADS;
import static com.example.mfpictures.DetailActivity.EXTRA_FAVORITES;
import static com.example.mfpictures.DetailActivity.EXTRA_LIKES;
import static com.example.mfpictures.DetailActivity.EXTRA_URL;
import static com.example.mfpictures.DetailActivity.EXTRA_USERNAME;
import static com.example.mfpictures.DetailActivity.EXTRA_VIEWS;

/*
This is an Activity that is mainly to fetch data from the internet using a certain JSON API
using Volley library
 */
public class DisplayPhotosActivity extends AppCompatActivity implements ContentAdapter.OnItemClickListener {


    public static final String CATEGORY_KEY = "categories";
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private ArrayList<ContentUrl> contentList;

    //RequestQueue used with Volley library in this case
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        try {
            recyclerView = findViewById(R.id.recycler_view_content);

            contentList = new ArrayList<>();

            //Volley is called in the onCreate method that creates a queue for the data
            //that will be fetched
            requestQueue = Volley.newRequestQueue(this);

            fetchPictures();
        } catch (NullPointerException ignore) {

        }
    }

    /*
    The method gets the categories from the List created in the ItemActivity class and using bundle
    are sent to this Activity via Intent.
    The category variable is then added to the url that it will make possible for the pictures to be fetched
     */
    public void fetchPictures() {

        String category = getIntent().getExtras().getString(CATEGORY_KEY);
        String url = "https://pixabay.com/api/?key=13872413-b163144865001bd5c3112feb9&q="
                + category
                + "&image_type=photo&pretty=true";

        //The JsonObjectRequest is being created which we give a GET request method, the url to fetch the data from
        //and two methods are over ridden, onResponse and onErrorResponse
        //where the main role is played in the onResponse method
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //We take the name of the array of the JSON API and assign it as a JSONArray
                    JSONArray jsonArray = response.getJSONArray("hits");

                    //Then we iterate through each of the content that we have chosen inside of jsonArray
                    //and then add it to the ArrayList by calling the ContentUrl's constructor and putting the values there
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject hitsObject = jsonArray.getJSONObject(i);

                        String imageUrl = hitsObject.getString("webformatURL");
                        String userName = hitsObject.getString("user");
                        int likes = hitsObject.getInt("likes");
                        int favorites = hitsObject.getInt("favorites");
                        int downloads = hitsObject.getInt("downloads");
                        int comments = hitsObject.getInt("comments");
                        int views = hitsObject.getInt("views");

                        contentList.add(new ContentUrl(imageUrl, userName, likes, favorites, downloads, views, comments));
                    }

                    //Using the ContentAdapter class that is used to create
                    // the adapter for the recyclerView and the recyclerView it self,
                    //we call it by giving it the context and the arrayList  as parameters of the constructor
                    contentAdapter = new ContentAdapter(DisplayPhotosActivity.this, contentList);

                    //ContentAdapter is called by the recyclerView
                    recyclerView.setAdapter(contentAdapter);

                    //ContentAdapter calls the setOnItemClickListener from OnItemClickListener interface
                    //which is also created in the ContentAdapter class and passes the context
                    contentAdapter.setOnItemClickListener(DisplayPhotosActivity.this);

                } catch (NullPointerException ignore) {

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        //JSONObjectRequest is then added to the requestQueue
        //that makes the procedure of fetching data possible
        requestQueue.add(request);
    }

    /*
    onItemClick which is implemented to this activity is created from OnItemClickListener interface
    deals with the clickEvents of the items in the recyclerView using intent
     */
    @Override
    public void onItemClick(int position) {

        Intent detailIntent = new Intent(this, DetailActivity.class);
        ContentUrl clickedItem = contentList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_USERNAME, clickedItem.getUserName());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikes());
        detailIntent.putExtra(EXTRA_FAVORITES, clickedItem.getFavorites());
        detailIntent.putExtra(EXTRA_DOWNLOADS, clickedItem.getDownloads());
        detailIntent.putExtra(EXTRA_VIEWS, clickedItem.getViews());
        detailIntent.putExtra(EXTRA_COMMENTS, clickedItem.getComments());

        startActivity(detailIntent);
    }


}
