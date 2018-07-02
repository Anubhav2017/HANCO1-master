package hanco.itsp.android.hanco1;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class stream extends AppCompatActivity {
    Button connectButton;
    VideoView preview;
    EditText ipAddress;
    MediaController mediaController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        connectButton=findViewById(R.id.connectbutton);
        preview=findViewById(R.id.preview);
        ipAddress=findViewById(R.id.ipaddress);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=ipAddress.getText().toString();
                playstream(address);
            }
        });
    }

    private void playstream(String address) {
        Uri src=Uri.parse(address);
        if(src==null){
            Toast.makeText(stream.this,"src==NULL",Toast.LENGTH_SHORT).show();
        }
        else{
            preview.setVideoURI(src);
            mediaController=new MediaController(this);
            preview.setMediaController(mediaController);
            preview.start();
            Toast.makeText(stream.this,"preview started from"+src,Toast.LENGTH_SHORT);

        }
    }
}
