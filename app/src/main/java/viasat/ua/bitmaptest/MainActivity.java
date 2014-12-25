package viasat.ua.bitmaptest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.UnknownHostException;


public class MainActivity extends ActionBarActivity {
    Bitmap map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ln = (LinearLayout) findViewById(R.id.ln1);
        ImageView iv = (ImageView) findViewById(R.id.iv1);
        ImageView iv2 = (ImageView) findViewById(R.id.iv2);
        ImageAT dsp = new ImageAT(this,"https://pp.vk.me/c425822/v425822148/bb05/kSMcna7Z4wE.jpg",iv);
        ImageAT dsp2 = new ImageAT(this,"https://pp.vk.me/c618128/v618128148/1f570/rvZdNsqdpAs.jpg",iv2);

        dsp.execute();
        dsp2.execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}



