package hanco.itsp.android.hanco1;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static CameraManager cameraManager;

    public static CameraCharacteristics frontCamerachars;

    public static CameraCharacteristics rearCamerachars;
    public static Size rearsize;
    public static Size frontsize;
    public Intent intentocr;
    public Intent intentlogo;
    public Intent intentcam;
    public Intent intentrpi;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int SPEECH_REQUEST_CODE = 0;
    public static String spokenText;
    public TextView speechText;
    public static String IPAddress;
    public static Socket socket;

    public static String Port;
    EditText portfield;
    EditText ipfield;
    Button connect;
    Button disconnect;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button ocrbutton=findViewById(R.id.home_ocr);
        Button logobutton=findViewById(R.id.home_logo);
        Button camerabutton=findViewById(R.id.home_camera);
        speechText=findViewById(R.id.speechtext);
        Button speakButton=findViewById(R.id.speakButton);
        connect=findViewById(R.id.connectbutton);
        portfield=findViewById(R.id.port);
        ipfield=findViewById(R.id.IpAddress);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPAddress=ipfield.getText().toString();
                Port=portfield.getText().toString();

            }
        });





        cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_PERMISSION);
        }
        try {
            frontCamerachars=cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[1]);
            StreamConfigurationMap frontconfigs=frontCamerachars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            frontsize=frontconfigs.getOutputSizes(ImageFormat.JPEG)[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            rearCamerachars=cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[0]);
            StreamConfigurationMap rearconfigs=rearCamerachars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            rearsize=rearconfigs.getOutputSizes(ImageFormat.JPEG)[0];


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        ocrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentocr=new Intent(HomeActivity.this,OCRActivity.class);
                startActivity(intentocr);
            }
        });
        logobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentlogo=new Intent(HomeActivity.this,LogoActivity.class);
                startActivity(intentlogo);
            }
        });
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentcam=new Intent(HomeActivity.this,CameraActivity.class);
                startActivity(intentcam);
            }
        });


        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                try{
                    startActivityForResult(intent, SPEECH_REQUEST_CODE);
                }   catch(ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(), "Speech not Working", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenText = results.get(0);}
        //intentocr=new Intent(HomeActivity.this,OCRActivity.class);
        //startActivity(intentocr);
        // Do something with spokenText

        Toast.makeText(HomeActivity.this,"Hello World",Toast.LENGTH_LONG);
        if(spokenText.equals("logo")){

            intentlogo=new Intent(HomeActivity.this,LogoActivity.class);
            startActivity(intentlogo);
        }
        else if(spokenText.equals("read")){
            Toast.makeText(HomeActivity.this,spokenText,Toast.LENGTH_LONG).show();
            intentocr=new Intent(HomeActivity.this,OCRActivity.class);
            startActivity(intentocr);
        }

        else if(spokenText.equals("camera")){
            intentcam=new Intent(HomeActivity.this,CameraActivity.class);
            startActivity(intentcam);
        }
        else{
            Toast.makeText(HomeActivity.this,"Not recognized",Toast.LENGTH_LONG).show();

        }
        speechText.setText(spokenText);

        super.onActivityResult(requestCode, resultCode, data);
    }


}
