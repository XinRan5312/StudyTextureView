package com.xinran.studytextureviewdemo.camera;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by houqixin on 2018/4/24.
 */

public class CameraPreIntentService extends IntentService implements CameraUtils.CamOpenOverCallback{
    public CameraPreIntentService(String name,CameraTextureView cameraTextureView,float rato){
        super(name);
    }
    public CameraPreIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CameraUtils.getInstance().openCamera(this);
    }

    @Override
    public void cameraHasOpened() {

    }
}
