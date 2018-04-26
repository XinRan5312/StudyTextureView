package com.xinran.studytextureviewdemo.camera;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinran.studytextureviewdemo.BaseActivity;
import com.xinran.studytextureviewdemo.R;
import com.xinran.studytextureviewdemo.camera.util.DisplayUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 权限的处理：正常权限和动态权限
 * https://blog.csdn.net/wwdlss/article/details/52909098
 */
public class CameraActivity extends BaseActivity implements CameraUtils.CamOpenOverCallback {
    private static final String TAG = CameraActivity.class.getSimpleName();
    CameraTextureView textureView = null;
    ImageButton shutterBtn;
    float previewRate = -1f;
    private Thread mOpenThread;
    private RxPermissions mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOpenThread = new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {

                    CameraUtils.getInstance().openCamera(CameraActivity.this);
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer

                }

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
        previewRate = DisplayUtil.getScreenRate(this);
        textureView.setLayoutParams(params);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            mOpenThread.interrupt();
            mOpenThread = null;
            mOpenThread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {

                        CameraUtils.getInstance().openCamera(CameraActivity.this);
                    } else {
                        ActivityCompat.requestPermissions(CameraActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
                    }

                }
            };
            mOpenThread.start();
        }
    }

    /**
     * 是否需要动态获取权限：6.0及以上版本才有需要
     */

    public static boolean isDinomicPermission() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * 一次申请多个并且多个都同意才返回true，一个也是用这个接口
     *
     * @param permisions
     */
    private void requestArrayPermission(String[] permisions, final OnRequestPermissionLisner permissionLisner) {

        creatRxPermission().request(permisions).subscribe(new PermissionObserver(permissionLisner));

    }

    /**
     * 点击某个View 获取某个权限
     *
     * @param view
     */
    private void onClickOnView(@NonNull View view,final OnRequestPermissionLisner permissionLisner) {
        RxView.clicks(view)
                .compose(creatRxPermission().ensure(Manifest.permission.CAMERA))
                .subscribe(new PermissionObserver(permissionLisner));
    }

    /**
     * 一次申请多个并且严格分开那个授予了，那个没有授予权限，一个也是用这个接口
     *
     */
    private void requestArrayPermissionEach() {

        creatRxPermission().requestEach(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Permission permission) {
                       if(permission.name.equals(Manifest.permission.CAMERA)){

                           if(permission.granted){//给了

                           }else if(permission.shouldShowRequestPermissionRationale){

                               //明确禁止 不需要弹框提示给了
                           }else{
                               //没有授予权限  还是可以自己去设置给与

                           }

                       }else if(permission.name.equals(Manifest.permission.READ_PHONE_STATE)){

                           if(permission.granted){//给了

                           }else if(permission.shouldShowRequestPermissionRationale){

                               //明确禁止 不需要弹框提示给了
                           }else{
                               //没有授予权限  还是可以自己去设置给与

                           }
                       }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public interface OnRequestPermissionLisner {
        void onPermissionGranted(boolean isGranted);


        void onPermissionComplete();
    }

    private static class PermissionObserver implements Observer<Boolean> {
        private OnRequestPermissionLisner permissionLisner;

        public PermissionObserver(OnRequestPermissionLisner lisner) {
            this.permissionLisner = lisner;
        }

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (permissionLisner != null) {
                permissionLisner.onPermissionGranted(aBoolean);

            }
        }

        @Override
        public void onError(Throwable e) {
            if (permissionLisner != null) {

                permissionLisner.onPermissionGranted(false);
            }

        }

        @Override
        public void onComplete() {
            if (permissionLisner != null) {

                permissionLisner.onPermissionComplete();
            }
        }
    }

    private RxPermissions creatRxPermission() {
        if (mPermissions == null)
            mPermissions = new RxPermissions(this);

        return mPermissions;
    }
}

