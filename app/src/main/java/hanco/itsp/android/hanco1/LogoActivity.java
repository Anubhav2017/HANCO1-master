package hanco.itsp.android.hanco1;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.concurrent.ExecutionException;

import static hanco.itsp.android.hanco1.HomeActivity.IPAddress;
import static hanco.itsp.android.hanco1.HomeActivity.Port;

public class LogoActivity extends AppCompatActivity {
    Button animalButton;
    Button flowerButton;
    Button genderButton;
    Button logoButton;
    public static ImageView imageView;
    public static TextView textResponse;
    public static String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        response="random";

        animalButton=findViewById(R.id.animalButton);
        flowerButton=findViewById(R.id.flowerbutton);
        genderButton=findViewById(R.id.genderbutton);
        imageView=findViewById(R.id.logoview);

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

    }



    public class buttonClick{
        String message;
        buttonClick(String s){
            message=s;
            MyClientTask imageTask=new MyClientTask(IPAddress,Integer.parseInt(Port),"img");
            imageTask.execute();
            try {
                imageTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(response.equalsIgnoreCase("Image Uploaded")){
                DjangoUnchained abc=new DjangoUnchained (getApplicationContext());
                abc.execute("http://192.168.2.11:80909/static/images/input.jpg");
                try {
                    abc.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                MyClientTask myClientTask=new MyClientTask(IPAddress,Integer.parseInt(Port),message);
                myClientTask.execute();


            }
            else{
                MyClientTask myClientTask=new MyClientTask(IPAddress,Integer.parseInt(Port),"Image Uploaded message not received");
                myClientTask.execute();

            }
        }


    }

}


