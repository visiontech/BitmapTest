package viasat.ua.bitmaptest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    Bitmap map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // LinearLayout ln = (LinearLayout) findViewById(R.id.ln1);
        // ImageView iv = (ImageView) findViewById(R.id.iv1);
        DisplayImageTask dsp = new DisplayImageTask();
        dsp.execute();


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


    public class DisplayImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            ImageView iv = (ImageView) findViewById(R.id.iv1);
            DownloadImageFromPath("http://www.viasat.ua/assets/photos/18220/present_the-planet-earth.jpg?1368999286", iv);
            return null;
        }

        //sets bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {

        }


        public void DownloadImageFromPath(String path, final ImageView view3) {
            InputStream in = null;
            Bitmap bmp = null;
            int responseCode = -1;
            try {

                URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setDoInput(true);
                con.connect();
                //download info for cach
                Map<String, List<String>> map = con.getHeaderFields();
                /*for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    System.out.println("Key : " + entry.getKey() +
                            " ,Value : " + entry.getValue());
               }*/
                String CacheControl = map.get("Cache-Control").toString();
                String ExpiresDate = map.get("Expires").toString();
                String LastModifiedDate = map.get("Last-Modified").toString();
                System.out.println("CacheControl: " + CacheControl + "__" + "__ExpiresDate: " + ExpiresDate + "__LastModifiedDate: " + LastModifiedDate);
                System.out.println(getCacheDir());


                //////////header///
                responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //download
                    in = con.getInputStream();
                    bmp = BitmapFactory.decodeStream(in);
                    in.close();

                    final Bitmap finalBmp = bmp;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view3.setImageBitmap(finalBmp);
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }
        }


        public File getTempFile(Context context, String url) {
            File file=null;
            String fileName = Uri.parse(url).getLastPathSegment();
            System.out.println(fileName);
            try {
                file = File.createTempFile(fileName, null, context.getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
    }
}



