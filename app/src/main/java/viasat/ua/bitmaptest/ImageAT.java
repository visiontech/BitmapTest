package viasat.ua.bitmaptest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by nshkarupa on 25.12.2014.
 */
public class ImageAT extends AsyncTask<String, Void, Bitmap> {
    private static final String LOG_TAG = null;
    private Activity v;
    private Uri url;
    private String CacheControl;
    private String ExpiresDate;
    private String LastModifiedDate;
    private Bitmap finalBmp;
    private ImageView iv;

    public ImageAT(Activity v, String url, ImageView iv) {
        this.v = v;
        this.url = Uri.parse(url);
        this.iv = iv;
    }


    @Override
    protected Bitmap doInBackground(String... urls) {


       finalBmp = putBitmapInDiskCache(url);
        return null;
    }

    //sets bitmap returned by doInBackground
    @Override
    protected void onPostExecute(Bitmap result) {
        v.runOnUiThread(new Runnable() {
            public void run() {
                    iv.setImageBitmap(finalBmp);
            }
        });
    }


   public Bitmap DownloadImageFromPath(String path) {

        InputStream in = null;
        Bitmap bmp = null;
        int responseCode = -1;
        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();

            //download info for catch
            Map<String, List<String>> map = con.getHeaderFields();
            CacheControl = map.get("Cache-Control").toString();
            ExpiresDate = map.get("Expires").toString();
            LastModifiedDate = map.get("Last-Modified").toString();
            System.out.println("CacheControl: " + CacheControl + "__" + "__ExpiresDate: " + ExpiresDate + "__LastModifiedDate: " + LastModifiedDate);
            System.out.println(v.getCacheDir());
            //

            responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
                finalBmp = bmp;
            }

        }
        catch (UnknownHostException e){
            Log.e("Exception", e.toString());
        }
        catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
        return finalBmp;
    }


    /**
     * Write bitmap associated with a url to disk cache
     */
    private Bitmap putBitmapInDiskCache(Uri url) {
        // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
        File sdCard = Environment.getExternalStorageDirectory();
        // img
        File cacheDir = new File(sdCard+"/Android/data/"+v.getPackageName(), "thumbnails");
        cacheDir.mkdir();
        // Create a path in that dir for a file, named by the default hash of the url
        File cacheFile = new File(cacheDir, "" + url.hashCode());


        FileInputStream fis = null;
        ArrayList<String> FileNames = getFileNames(GetFiles(sdCard + "/Android/data/" + v.getPackageName()));
        System.out.println(Arrays.toString(FileNames.toArray()));
        System.out.println(cacheDir.toString() + url.hashCode());
        //if exist
        if(FileNames.contains(String.valueOf(url.hashCode()))==false)
        {      Bitmap avatar = DownloadImageFromPath(url.toString());

            try {
                // Create a file at the file path, and open it for writing obtaining the output stream
                cacheFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(cacheFile);
                // Write the bitmap to the output stream (and thus the file) in PNG format (lossless compression)
                avatar.compress(Bitmap.CompressFormat.PNG, 100, fos);
                // Flush and close the output stream
                fos.flush();
                fos.close();
            } catch (Exception e) {
                // Log anything that might go wrong with IO to file
                Log.e(LOG_TAG, "Error when saving image to cache. ", e);
            }
        }
        // Open input stream to the cache file
        try {
            fis = new FileInputStream(cacheFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Read a bitmap from the file (which presumable contains bitmap in PNG format, since
        // that's how files are created)

        Bitmap bmp = BitmapFactory.decodeStream(fis);
        return bmp;
    }

    //смотрим имена файла в каталоге кеша
    public File[] GetFiles(String DirectoryPath) {
        File f = new File(DirectoryPath);
        f.mkdirs();
        File[] file = f.listFiles();
        return file;
    }

    public ArrayList<String> getFileNames(File[] file){
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (file.length == 0)
            return null;
        else {
            for (int i=0; i<file.length; i++)
                arrayFiles.add(file[i].getName());
        }
        return arrayFiles;
    }
}

