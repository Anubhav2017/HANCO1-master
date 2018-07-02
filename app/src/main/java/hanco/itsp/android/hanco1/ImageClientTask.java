package hanco.itsp.android.hanco1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageClientTask extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;

    String msgToServer;
    ImageView imageView;
    Bitmap bm;
    String response;
    TextView responseView;

    ImageClientTask(String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToServer = msgTo;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        imageView=LogoActivity.imageView;
        responseView=LogoActivity.textResponse;
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        ObjectInputStream ois = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            if(msgToServer != null){
                dataOutputStream.writeUTF(msgToServer);
            }
            byte[] bytes;
            try {
                bytes = (byte[])ois.readObject();
                bm= BitmapFactory.decodeByteArray(bytes,0,bytes.length);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


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

            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (ois != null) {
                try {
                    ois.close();
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
        responseView.setText(response);

        super.onPostExecute(result);
    }

}