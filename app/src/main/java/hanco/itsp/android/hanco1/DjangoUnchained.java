package hanco.itsp.android.hanco1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public class DjangoUnchained extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    ProgressBar progressBar;
    ImageView imageView;
     String s;
    TextView textresponse;
    TextView textView;
    ImageView ocrView;




    public DjangoUnchained(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        textresponse=LogoActivity.textResponse;
        imageView=LogoActivity.imageView;
        textView=OCRActivity.textView;
        ocrView=OCRActivity.imageView;


    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(sUrl[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Django Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }

            input = connection.getInputStream();

            s=Environment.getExternalStorageState() + "/Tess/image/ocr.jpg";
            File f = new File(s);

            if(f.exists())
                f.delete();

            output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Tess/image/ocr.jpg");

            byte[] data = new byte[10485760];
            int count;

            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }

                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }


            if (connection != null)
                connection.disconnect();

        }

        return null;
    }

    protected void onPostExecute(String result) {
        mWakeLock.release();
        textresponse.setText("Image downloaded");
        File imgfile= new File(Environment.getExternalStorageDirectory() + "/Tess/image/ocr.jpg");
        Uri imguri= Uri.fromFile(imgfile);
        imageView.setImageURI(imguri);
        textView.setText("Image Download");
        ocrView.setImageURI(imguri);
        ocrView.setVisibility(View.VISIBLE);




    }


}
