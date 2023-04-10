package com.blockchain.agritech;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.blockchain.agritech.databinding.ActivityFarmerView1Binding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import org.jetbrains.annotations.Nullable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;


public class Contractor_Transporter_View_2 extends AppCompatActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityFarmerView1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFarmerView1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        ConstraintLayout frameLayout = findViewById(R.id.cl);
        Glide.with(this)
                .load("https://images.unsplash.com/photo-1642413597408-183a09361cea?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=464&q=80")
                .placeholder(R.color.teal_200) // Placeholder image until the image is loaded
                .error(R.color.light_blue_600) // Error image if the image fails to load
                .into(new CustomViewTarget<ConstraintLayout, Drawable>(frameLayout) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Toast.makeText(Contractor_Transporter_View_2.this, "Load Failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        getView().setBackground(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        // handle resource cleared
                    }
                });

        Date h = new Date();
        String A = String.valueOf(h.getDate());
        String B = String.valueOf(h.getMonth());
        String F = "Today,  "+A+"-"+B+"-"+"2022";
        TextView textVew = findViewById(R.id.textView5);
        TextView t220037 = findViewById(R.id.textView6);
        textVew.setText(F);

        String Name = "" ;
        try {
            FileInputStream fin = openFileInput("WhoLoggedIn.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            Name = temp.toString();
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////
        String DataFromFile = "" ;
        try {
            FileInputStream fin = openFileInput("transport.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            DataFromFile = temp.toString();
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////////////

        Vector<String[]> data = GetVectorOutOf(DataFromFile); // assuming you have implemented this method

        Vector<String[]> cache = new Vector<String[]>(0); // assuming you have implemented this method

        ArrayList<String> filteredList = new ArrayList<>();
        for (int i=0;i<data.get(0).length;i++) {
            if (data.get(0)[i].equals(Name)) {
                filteredList.add(data.get(0)[i] + " must transport crop with Quantity " + data.get(1)[i] +
                        " from " + data.get(3)[i] + " on " +
                        data.get(2)[i] + " at \n PKR " + data.get(4)[i]);
            }
        }

        ListView lv = findViewById(R.id.lvlv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                filteredList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Contractor_Transporter_View_2.this, Contractor_View_TBids.class);

                i.putExtra("Name", extractInfo(filteredList.get(position),1));
                i.putExtra("Quantity", extractInfo(filteredList.get(position),2));
                i.putExtra("Location", extractInfo(filteredList.get(position),3));
                i.putExtra("Date", extractInfo(filteredList.get(position),4));
                i.putExtra("Price", extractInfo(filteredList.get(position), 5));



                startActivity(i);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    public String extractInfo(String input, int infoType) {
        String[] words = input.split(" ");
        String info = "";

        switch (infoType) {
            case 1: // Name
                info = words[0];
                break;
            case 2: // Quantity
                info = words[6];
                break;
            case 3: // Location
                info = words[8];
                break;
            case 4: // Date
                info = words[10];
                break;
            case 5: // Price
                info = words[words.length-1];
                break;
            default:
                Log.e("Invalid info type.", "");
                break;
        }

        return info;
    }
    public static String[] removeElement(String[] array, int index) {
        String[] newArray = new String[array.length - 1];
        for (int i = 0, k = 0; i < array.length; i++) {
            if (i == index) {
                continue;
            }
            newArray[k++] = array[i];
        }
        return newArray;
    }


    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }






    public Vector<String[]> GetVectorOutOf(String data) {
        Vector<String[]> columnData = new Vector<String[]>();
        String[] rows = data.split("\n");
        int numFields = rows[0].split("\\|").length;
        for (int i = 0; i < numFields; i++) {
            String[] fieldArray = new String[rows.length];
            for (int j = 0; j < rows.length; j++) {
                String[] fields = rows[j].split("\\|");
                fieldArray[j] = fields[i];
            }
            columnData.add(fieldArray);
        }
        return columnData;
    }


    public String LeftOverToString(Vector<String[]> data, int pos){
        String Returner = "";
        for(int i=7;i<data.size();i++){
            Returner+="|"+data.get(i)[pos];
        }
        return Returner;
    }



    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}