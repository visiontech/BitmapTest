package viasat.ua.bitmaptest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by nshkarupa on 25.12.2014.
 */
public class ImageAT  extends AsyncTask<String, Void, Bitmap> {
    private Activity v;


        public ImageAT(Activity v) {
            this.v = v;
        }





    @Override
    protected Bitmap doInBackground(String... urls) {

            ImageView iv = (ImageView) v.findViewById(R.id.iv1);
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
                System.out.println(v.getCacheDir());


                //////////header///
                responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //download
                    in = con.getInputStream();
                    bmp = BitmapFactory.decodeStream(in);
                    in.close();

                    final Bitmap finalBmp = bmp;
                    v.runOnUiThread(new Runnable() {
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

