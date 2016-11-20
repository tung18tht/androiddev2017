package vn.edu.usth.musicplayer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        
                    case R.id.tab_playing:
                        
                    case R.id.tab_download:
                }
            }
        });
        
        Log.i("status", "Main Activity created");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        Log.i("status", "Main Activity started");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        Log.i("status", "Main Activity paused");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Log.i("status", "Main Activity resumed");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        
        Log.i("status", "Main Activity stopped");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        Log.i("status", "Main Activity destroyed");
    }
}
