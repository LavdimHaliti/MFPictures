package com.example.mfpictures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/*
MainActivity is used just for the splashScreen
 */
public class MainActivity extends AppCompatActivity {

    //Variable that determines the period of the splashScreen staying on the screen
    public static int TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        In order for the splashScreen to work we have to use Handler
        which will call a method called postDelayed that takes a Runnable() as a parameter
        which overrides the run method.
        Inside the method we use Intent to jump to the next activity
        and we assign the finish method inside to trigger the end of the splash screen
        by giving the time to be displayed at the end of the method
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                startActivity(intent);

                finish();
            }
        }, TIME_OUT);
    }
}
