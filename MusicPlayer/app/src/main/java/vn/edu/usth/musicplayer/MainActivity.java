package vn.edu.usth.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.i("status", "Created");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        Log.i("status", "Started");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        Log.i("status", "Paused");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Log.i("status", "Resumed");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        
        Log.i("status", "Stopped");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        Log.i("status", "Destroyed");
    }
}
