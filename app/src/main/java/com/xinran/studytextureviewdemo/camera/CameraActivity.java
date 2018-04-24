package com.xinran.studytextureviewdemo.camera;


import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import com.xinran.studytextureviewdemo.BaseActivity;
import com.xinran.studytextureviewdemo.R;
import com.xinran.studytextureviewdemo.camera.util.DisplayUtil;

public class CameraActivity extends BaseActivity implements CameraUtils.CamOpenOverCallback {
    private static final String TAG = CameraActivity.class.getSimpleName();
    CameraTextureView textureView = null;
    ImageButton shutterBtn;
    float previewRate = -1f;
    private Thread mOpenThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOpenThread = new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                CameraUtils.openCamera(CameraActivity.this);
            }
        };
        mOpenThread.start();
        setContentView(R.layout.activity_camera);
        initUI();
        initViewParams();
        textureView.setAlpha(1.0f);

        shutterBtn.setOnClickListener(new BtnListeners());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    private void initUI() {
        textureView = (CameraTextureView) findViewById(R.id.camera_textureview);
        shutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
    }

    private void initViewParams() {
        LayoutParams params = textureView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        textureView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);
        ;
        shutterBtn.setLayoutParams(p2);

    }

    @Override
    public void cameraHasOpened() {
        // TODO Auto-generated method stub
        SurfaceTexture surface = textureView.getSurfaceTexture();
        CameraUtils.getInstance().startPreviewWithSurfaceTexture(surface, previewRate);
        finishThread();
    }

    private void finishThread() {
        if (mOpenThread != null) {
            mOpenThread.interrupt();
            mOpenThread = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finishThread();
    }

    private class BtnListeners implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_shutter:
                    CameraUtils.getInstance().takePicture();
                    break;
                default:
                    break;
            }
        }

    }

}
