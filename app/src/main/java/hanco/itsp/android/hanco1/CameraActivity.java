package hanco.itsp.android.hanco1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.Collections;


public class CameraActivity extends AppCompatActivity {
    ToggleButton toggleButton;
    Button picbutton;
    Button vidbutton;
    TextureView previewtexture;
    CameraManager cameraManager = HomeActivity.cameraManager;
    Size frontsize = HomeActivity.frontsize;
    Size rearsize = HomeActivity.rearsize;
    CameraDevice currentCamera;
    Boolean toggled = false;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private void openBackgroundThread() {
        handlerThread = new HandlerThread("CameraApp2");

        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    private void closeBackgroundThread() {
        handlerThread.quit();
        handler = null;
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            currentCamera = camera;
            String id = camera.getId();
            try {
                if (id == cameraManager.getCameraIdList()[0]) {
                    createPreviewSession(frontsize);

                } else {
                    createPreviewSession(rearsize);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();


        }
    };

    private void createPreviewSession(Size size) {
        final SurfaceTexture surfaceTexture = previewtexture.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
        final Surface surface = new Surface(surfaceTexture);
        try {
            currentCamera.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        CaptureRequest.Builder builder = currentCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        builder.addTarget(surface);
                        //int rotation = getWindowManager().getDefaultDisplay().getRotation();
                        //builder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

                        session.setRepeatingRequest(builder.build(), null, handler);
                        Log.d("3", "built");
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    ;
    HandlerThread handlerThread;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        toggleButton = findViewById(R.id.cameratoggle);
        picbutton = findViewById(R.id.takepicbutton);
        vidbutton = findViewById(R.id.takevidbutton);
        previewtexture = findViewById(R.id.camerapreviewtexture);
        toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentCamera.close();





                if (isChecked) {

                    try {

                        cameraManager.openCamera(cameraManager.getCameraIdList()[1], stateCallback, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    try {
                        cameraManager.openCamera(cameraManager.getCameraIdList()[0], stateCallback, handler);
                        Log.d("Test1", "got here");
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        });



        previewtexture.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openBackgroundThread();


                if (toggleButton.getText() == toggleButton.getTextOn()) {

                    try {
                        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                                    Manifest.permission.CAMERA
                            }, 1);
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        cameraManager.openCamera(cameraManager.getCameraIdList()[1], stateCallback, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }


                }
                else {
                    try {
                        cameraManager.openCamera(cameraManager.getCameraIdList()[0], stateCallback, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                currentCamera=null;
                closeBackgroundThread();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {



            }
        });
    }

}
