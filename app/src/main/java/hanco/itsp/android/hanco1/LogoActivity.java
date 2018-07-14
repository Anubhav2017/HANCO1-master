package hanco.itsp.android.hanco1;


import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static hanco.itsp.android.hanco1.HomeActivity.IPAddress;
import static hanco.itsp.android.hanco1.HomeActivity.Port;

public class LogoActivity extends AppCompatActivity {
    Button animalButton;
    Button flowerButton;
    Button genderButton;
    Button logoButton;
    Button downloadImage;
    Button sendImage;
    public static ImageView imageView;
    public static TextView textResponse;
    public static String response;
    private TextToSpeech mTTS;
    private Button mButtonSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        response="random";
        sendImage=findViewById(R.id.clickImage);

        animalButton=findViewById(R.id.animalButton);
        flowerButton=findViewById(R.id.flowerbutton);
        genderButton=findViewById(R.id.genderbutton);
        imageView=findViewById(R.id.logoview);
        downloadImage=findViewById(R.id.downloadImage);

        logoButton=findViewById(R.id.logo_logo);
        textResponse=findViewById(R.id.logoresponsetext);
        textResponse.setText("");
        animalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick click=new buttonClick("CvD");
            }
        });
        flowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick click=new buttonClick("flr");
            }
        });
        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick click=new buttonClick("gen");

            }
        });
        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick click=new buttonClick("lgr");

            }
        });
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask imageTask=new MyClientTask(IPAddress,Integer.parseInt(Port),"imgLogo");
                imageTask.execute();
                textResponse.setText("Image request sent");
            }
        });
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DjangoUnchained abc=new DjangoUnchained (getApplicationContext(),"Logo");
                String url="http://"+IPAddress+":8090/static/images/input.jpg";

                abc.execute(url);
            }
        });

        mButtonSpeak = findViewById(R.id.button_speak);


        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView myword = (TextView) findViewById(R.id.logoresponsetext);
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


    public class buttonClick{
        String message;
        buttonClick(String s){
            message=s;

                MyClientTask myClientTask=new MyClientTask(IPAddress,Integer.parseInt(Port),message);
                myClientTask.execute();

            }

    }

}


