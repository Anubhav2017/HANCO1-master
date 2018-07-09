package hanco.itsp.android.hanco1;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClientTask extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = LogoActivity.response;
    String msgToServer;
    TextView textResponse;
    TextView textocr;
    TextView textView=OCRActivity.textView;


    MyClientTask(String addr, int port, String msgTo) {
        dstAddress = addr;
        dstPort = port;
        msgToServer = msgTo;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        textResponse=LogoActivity.textResponse;

        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        try{
            socket = new Socket(dstAddress, dstPort);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            response="No device detected";
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataOutputStream = new DataOutputStream(
                    socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            if(msgToServer != null){
                dataOutputStream.writeUTF(msgToServer);
            }

            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = dataInputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }


            response = byteArrayOutputStream.toString("UTF-8");


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

            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        if(msgToServer.equals("imgLogo")){
            textResponse.setText(response);
        }
        if(msgToServer.equals("imgOCR")){
            textView.setText(response);
        }
        else{
            textResponse.setText(response);
        }

        super.onPostExecute(result);
    }

}
