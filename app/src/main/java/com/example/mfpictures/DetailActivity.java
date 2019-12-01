package com.example.mfpictures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

/*
DetailActivity displays the content of the picture in a more detailed manner by also adding
the ability to share the picture with other users and download the picture to system files
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_USERNAME = "userName";
    public static final String EXTRA_LIKES = "likes";
    public static final String EXTRA_FAVORITES = "favorites";
    public static final String EXTRA_DOWNLOADS = "downloads";
    public static final String EXTRA_VIEWS = "views";
    public static final String EXTRA_COMMENTS = "comments";

    ImageView imageView;
    ImageView shareImageView;
    ImageView downloadImageView;

    //Bitmap is used for displaying digital images
    Bitmap bitmap;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 101;

    //This overridden method gives the ultimate permission whether the app is allowed to be given permission to  certain files
    //of the system.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Here we use the switch statement and we pass the requestCode as the parameter
        switch (requestCode) {
            //The WRITE_EXTERNAL_STORAGE_CODE case has an if statement that if the device is allowed
            //the permission then the data will allowed to be saved to the designated file.
            case WRITE_EXTERNAL_STORAGE_CODE: {
                //If we have data and data are in position and we are given permission to store this data
                //then we store the data
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission is enabled
                } else {
                    Toast.makeText(this, "Enable permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Here we get the reference for each of the corresponding views
        imageView = findViewById(R.id.image_view_detail);
        TextView textViewUser = findViewById(R.id.text_view_user_detail);
        TextView textViewLikes = findViewById(R.id.text_view_likes_detail);
        TextView textViewFavorites = findViewById(R.id.text_view_favorites_detail);
        TextView textViewDownloads = findViewById(R.id.text_view_downloads_detail);
        TextView textViewViews = findViewById(R.id.text_view_views_detail);
        TextView textViewComments = findViewById(R.id.text_view_comments_detail);
        downloadImageView = findViewById(R.id.img_view_download);
        shareImageView = findViewById(R.id.img_view_share);

        //We get the intent and the data from our MainActivity to this one
        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String userName = intent.getStringExtra(EXTRA_USERNAME);
        int likes = intent.getIntExtra(EXTRA_LIKES, 0);
        int favorites = intent.getIntExtra(EXTRA_FAVORITES, 1);
        int downloads = intent.getIntExtra(EXTRA_DOWNLOADS, 2);
        int views = intent.getIntExtra(EXTRA_VIEWS, 3);
        int comments = intent.getIntExtra(EXTRA_COMMENTS, 4);

        //And we use our references to set the data to these references so they can be displayed.
        Picasso
                .get()
                .load(imageUrl)
                .noFade()
                .fit()
                .centerInside()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        MaterialImageLoading.animate(imageView).setDuration(3000).start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
        textViewUser.setText(userName);
        textViewLikes.setText("" + likes);
        textViewFavorites.setText("" + favorites);
        textViewDownloads.setText("" + downloads);
        textViewViews.setText("" + views);
        textViewComments.setText("" + comments);

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //shareImage() method called in onCreate method.
                shareImage();
            }
        });

        downloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the version of SDK is greater or equal to the version of android, in this case M(MarshMellow)
                //then check for self permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    //Here we assume that permission from the package manager is denied to write data in external storage.
                    //If that happens then we request for permission.
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        //Here we request for permission from the device by passing the permission and the
                        //request code in the requestPermission constructor.
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                    } else {

                        //the saveImage() method is called in the onCreate method.
                        saveImage();
                    }
                }
            }
        });
    }

    //Method used for sharing images or data with other applications within the device
    public void shareImage() {

        //Get Bitmap attached to imageView
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        //URI that will contain all the necessary data for sharing with other apps.
        Uri photoUri;

        try {

            //getExternalCacheDir() is intended only for temporary files or cache files
            //that could be deleted by the system or the user to regain space.
            //In this case we use this method to cache an image from our app
            //that later we will use it to share it with other apps.
            File file = new File(getExternalCacheDir(), "myImage.png");

            //In this case we are writing data to the File object which means we gonna write the necessary info
            //to directory that is specified from the File object
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            //Here we  write a compressed version of our image,
            //by specifying the format of the file that we want to compress
            //, in this case .PNG , the quality of the image and the OutputStream where the image will be written
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            //Flushes this output stream and forces any buffered output bytes to be written out to the stream.
            fileOutputStream.flush();

            //Closes this output stream and releases any system resources associated with the stream.
            fileOutputStream.close();

            //FileProvider.getUriForFile(Context, Authority, File) returns a content URI for a given File.
            photoUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            //Intent used to share data to other apps on android device
            Intent shareIntent = new Intent();

            //Action specified by the intent what where we want to jump to
            shareIntent.setAction(Intent.ACTION_SEND);

            //EXTRA_STREAM is the key of the data that we want to share, in this case a URI called photoUri.
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);

            //Type of data we want to share
            shareIntent.setType("image/png");

            //Here we start the action that we want our app to perform.
            startActivity(Intent.createChooser(shareIntent, "Share image via"));

        } catch (NullPointerException ignore) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Method for saving the image to internal storage of the device
    private void saveImage() {

        //Get Bitmap attached to imageView
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();


        //SimpleDateFormat creates the current date that a certain file has been created
        //and we assign it to a string which means that the date will be the "Name" of the file
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(System.currentTimeMillis());

        //Here we name the file by giving the current time appended with a string file format
        String imgName = time + ".PNG";

        //Get the directory using getExternalStorageDirectory(deprecated in API 29) method and assign it to File object which will be used
        //to specify the directory of the files in the system of the phone.
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File directory = new File(path + "/MyPictures");

        //Creates the directory which has been specified above.
        directory.mkdirs();

        //The directory and the file name is assigned in the File object
        File file = new File(directory, imgName);

        //OutputStream is gonna be used to write data in the File stream.
        OutputStream stream;

        try {

            //FileOutPutStream is a subclass of OutputStream which basically does the same job
            //In this case we are writing data to the File object which means we gonna write the necessary info
            //to directory that is specified from the File object
            stream = new FileOutputStream(file);

            //Here we  write a compressed version of our image,
            //by specifying the format of the file that we want to compress
            //, in this case .PNG , the quality of the image and the OutputStream where the image will be written
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Flushes this output stream and forces any buffered output bytes to be written out to the stream.
            stream.flush();

            //Closes this output stream and releases any system resources associated with the stream.
            stream.close();

            //Shows that the image has been saved to the designated file
            Toast.makeText(DetailActivity.this, "Image Saved to Pictures", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
