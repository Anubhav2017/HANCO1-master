package hanco.itsp.android.hanco1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.EntityIterator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.Locale;

import static hanco.itsp.android.hanco1.HomeActivity.IPAddress;
import static hanco.itsp.android.hanco1.HomeActivity.Port;
import static hanco.itsp.android.hanco1.LogoActivity.response;

public class OCRActivity extends AppCompatActivity {

    private static final String TAG=OCRActivity.class.getSimpleName();
    public static final String TESS_DATA = "/tessdata";
    String response;
    Button ocrUploadButton;
    Button ocrDownloadButton;
    Button ocrButton;
    Button cropButton;
    Button buttonSpeak;
    Uri outputFileDir;
    private TextToSpeech mTTS;
    private Button mButtonSpeak;

    public String result;

    public static TextView textView;
    private TessBaseAPI tessBaseAPI;
    public static ImageView imageView;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory() + "/Tess";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("en", "IN"));

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        setContentView(R.layout.activity_ocr);
        imageView=findViewById(R.id.ocrImageView);
        textView=findViewById(R.id.ocrText);
        response=LogoActivity.response;
        ocrUploadButton=findViewById(R.id.ocrupload);
        ocrDownloadButton=findViewById(R.id.ocrdownload);
        ocrButton=findViewById(R.id.startOCR);
        cropButton=findViewById(R.id.cropButton);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask ocrTask=new MyClientTask(IPAddress,Integer.parseInt(Port),"OCR");
                ocrTask.execute();

            }
        });
        ocrUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask ocrTask=new MyClientTask(IPAddress,Integer.parseInt(Port),"imgOCR");
                ocrTask.execute();
            }
        });
        ocrDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DjangoUnchained abc=new DjangoUnchained (getApplicationContext(),"OCR");
                String url="http://"+IPAddress+":8090/static/images/input.jpg";
                abc.execute(url);

            }
        });
        ocrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTessData();
                String imageFilePath = DATA_PATH + "/image/" + "ocr.jpg";
                outputFileDir = Uri.fromFile(new File(imageFilePath));
                startOCR(outputFileDir);


            }
        });

        mButtonSpeak = findViewById(R.id.button_speak2);


        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView myword = findViewById(R.id.ocrText);
                String words = myword.getText().toString();
                speakWords(words);

            }
        });
    }
    private void speakWords(String speech) {
        if(speech.equals(""))
        {
            speech = "Nothing to say yet";
        }
        mTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    private void prepareTessData() {

        try {
            File dir = new File(DATA_PATH + TESS_DATA);

            boolean x=false;


            if(!dir.exists())
                x=dir.mkdirs();

            if (x==false) textView.setText("False :/");

            String fileList[] = getAssets().list("");

            for(String fileName : fileList){
                String pathToDataFile = DATA_PATH + TESS_DATA + "/" + fileName;

                if(! (new File(pathToDataFile).exists() ))
                {
                    InputStream in = getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);

                    byte [] buff = new byte[1024];

                    int len;

                    while((len = in.read(buff)) > 0)
                    {
                        out.write(buff, 0, len);
                    }

                    in.close();
                    out.close();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startOCR(Uri imageUri)
    {


        try{

            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());

            result = this.getText(bitmap);

            String path=Environment.getExternalStorageDirectory().toString() + "/Tess";
            File file=new File(path, "result.txt");

            FileOutputStream stream= new FileOutputStream(file);
            try{
                stream.write(result.getBytes());
            } finally {
                stream.close();
            }

            textView.setText(result);

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    private String getText(Bitmap bitmap) {

        try{

            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        File tester= new File(DATA_PATH+TESS_DATA);
        if(tester.exists())
            textView.setText("EXISTS!");
        tessBaseAPI.init(DATA_PATH+"/", "eng", TessBaseAPI.OEM_DEFAULT);
        tessBaseAPI.setPageSegMode(1);
        textView.setText("Initialised!");
        if (bitmap==null) textView.setText(DATA_PATH);
        File temp= new File(DATA_PATH + "/image/" + "ocr.jpg");
        tessBaseAPI.setImage(temp);
        textView.setText("Image SET!");
        String retStr = "No Result";
        textView.setText("Processing...");

        try{
            retStr = tessBaseAPI.getUTF8Text();
            textView.setText("Done!");
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

        tessBaseAPI.end();
        return retStr;
    }

}
