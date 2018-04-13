package com.dyf.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ReturnActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_return, menu);
        return true;
    }
}
