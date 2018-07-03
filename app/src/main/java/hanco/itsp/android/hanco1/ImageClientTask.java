package hanco.itsp.android.hanco1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
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
    String result;
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
            dis=new DataInputStream(socket.getInputStream());
            byte[] base64=dis.readUTF().getBytes();

            byte[] arr=Base64.decode(base64,0, base64.length, Base64.NO_WRAP);
            responseView.setText(result);
            /*byte[] byteArray;
            byteArray= Base64.decode(b64,Base64.DEFAULT);
            bm=BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);*/

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
        imageView.setImageBitmap(bm);

        String root=Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir=new File(root+"/ITSP");
        myDir.mkdir();
        String fname="xD.jpg";
        File file=new File(myDir, fname);
        if(file.exists())
            file.delete();

        try{
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(Base64.decode(response, Base64.NO_WRAP));
        } catch (IOException e) {
            e.printStackTrace();

        }
        super.onPostExecute(result);
    }

}