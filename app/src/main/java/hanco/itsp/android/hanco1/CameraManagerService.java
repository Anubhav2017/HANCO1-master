package hanco.itsp.android.hanco1;

import android.app.Service;
import android.content.Intent;
import android.hardware.camera2.CameraDevice;
import android.os.IBinder;
import android.hardware.camera2.CameraManager;

public class CameraManagerService extends Service {
    public CameraManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
         CameraManager cameraManager;
         CameraDevice frontCamera;
         CameraDevice backcamera;



    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
