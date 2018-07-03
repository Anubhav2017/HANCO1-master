package hanco.itsp.android.hanco1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ImageClientTask extends AsyncTask<Void, Void, Void> {

    private String dstAddress;
    private int dstPort;

    private String msgToServer;
    ImageView imageView;
    Bitmap bm;
    String response;
    TextView responseView;

    ImageClientTask(String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToServer = msgTo;
        responseView=LogoActivity.textResponse;
        responseView.setText("Hello World");
        imageView=LogoActivity.imageView;


    }

    @Override
    protected Void doInBackground(Void... arg0) {
        responseView.setText("in background");

        Socket socket = null;
        responseView.setText("socket created");

        DataInputStream dis = null;
        DataInputStream dis2 = null;


        try {
            socket = new Socket(dstAddress, dstPort);
            responseView.setText("dos created");

            dis= new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            responseView.setText("dis created");
            int size=409600;
            byte[] imageAr = new byte[size];
            dis.read(imageAr);

            responseView.setText(new String(imageAr, "UTF-8"));
            bm = Bitmap.createBitmap(640, 640, Bitmap.Config.ARGB_8888);
            ByteBuffer buffer = ByteBuffer.wrap(imageAr);
            bm.copyPixelsFromBuffer(buffer);

            if(bm!=null)
                responseView.setText("bitmap created");


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        responseView.setText("In post Execute");
        imageView.setImageBitmap(bm);
        String root=Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir=new File(root+"/ITSP");
        myDir.mkdir();
        String fname="LoL.jpg";
        File file=new File(myDir,fname);
        if(file.exists())file.delete();
        try{
            FileOutputStream fos=new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,90,fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }

}