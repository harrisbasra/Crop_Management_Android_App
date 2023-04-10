package com.blockchain.agritech;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.agritech.databinding.ActivityContractorViewTbidsBinding;
import com.blockchain.agritech.databinding.ActivityFarmerView2Binding;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Contractor_View_TBids extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
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
    private ActivityContractorViewTbidsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContractorViewTbidsBinding.inflate(getLayoutInflater());
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
        Date h = new Date();
        String A = String.valueOf(h.getDate());
        String B = String.valueOf(h.getMonth());
        String F = "Today,  "+A+"-"+B+"-"+"2022";
        TextView textVew = findViewById(R.id.textView5);
        TextView t220037 = findViewById(R.id.textView6);
        textVew.setText(F);

        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String Quantity = intent.getStringExtra("Quantity");
        String Location = intent.getStringExtra("Location");
        String Date = intent.getStringExtra("Date");
        String Price = intent.getStringExtra("Price");

        DBHelper2 db = new DBHelper2(this);
        List<String[]> ReqData = db.getDataByContractorNameQuantityAndDate(name, Quantity, Date);
        if(!(ReqData.size() ==0)) {
            String[] To_Be_Fed = new String[ReqData.size()];

            for (int i = 0; i < ReqData.size(); i++) {
                To_Be_Fed[i] = ReqData.get(i)[1] + " has bid " + ReqData.get(i)[2];
            }
            Set<String> set = new LinkedHashSet<>(Arrays.asList(To_Be_Fed));
            To_Be_Fed = set.toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    To_Be_Fed);

            binding.lvlv.setAdapter(adapter);
            binding.lvlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Contractor_View_TBids.this, Contractor_Approves.class);

                    i.putExtra("-", ReqData.get(position)[1]);
                    i.putExtra("Quantity", Quantity);
                    i.putExtra("Date", Date);
                    i.putExtra("Location", Location);
                    i.putExtra("Price", ReqData.get(position)[2]);



                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
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

    public static Vector<String> Cleaner(Vector<String> arr) {
        for (int i = 0; i < arr.size(); i++) {
            arr.set(i, arr.get(i).replace("-", " has bid PKR "));
        }
        return arr;
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