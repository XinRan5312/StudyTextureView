package com.xinran.studytextureviewdemo;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinran.studytextureviewdemo.camera.CameraActivity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PermissionManager {
    private static RxPermissions mPermissions;
    private static PermissionManager sPermissionManager;
    private PermissionManager(){

    }
    private static class PermissionManagerGen{


        public static PermissionManager genPermissionManager(Activity activity){
            if(sPermissionManager==null){
                sPermissionManager=new PermissionManager();

            }
            mPermissions=new RxPermissions(activity);
            return sPermissionManager;
        }

    }
   public static PermissionManager newInstance(Activity activity){
       return PermissionManagerGen.genPermissionManager(activity);
   }
    /**
     * 是否需要动态获取权限：6.0及以上版本才有需要
     */

    public  boolean isDinomicPermission() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * 一次申请多个并且多个都同意才返回true，一个也是用这个接口
     *
     * @param permisions
     */
    public void requestArrayPermission(String[] permisions, final CameraActivity.OnRequestPermissionLisner permissionLisner) {

        mPermissions.request(permisions).subscribe(new PermissionObserver(permissionLisner));

    }

    /**
     * 点击某个View 获取某个权限
     *
     * @param view
     */
    public void onClickOnView(@NonNull View view, final CameraActivity.OnRequestPermissionLisner permissionLisner) {
        RxView.clicks(view)
                .compose(mPermissions.ensure(Manifest.permission.CAMERA))
                .subscribe(new PermissionObserver(permissionLisner));
    }

    /**
     * 一次申请多个并且严格分开那个授予了，那个没有授予权限，一个也是用这个接口
     *
     */
    public void requestArrayPermissionEach() {

        mPermissions.requestEach(Manifest.permission.CAMERA,
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
        private CameraActivity.OnRequestPermissionLisner permissionLisner;

        public PermissionObserver(CameraActivity.OnRequestPermissionLisner lisner) {
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

    private RxPermissions creatRxPermission(Activity activity) {
        if (mPermissions == null)
            mPermissions = new RxPermissions(activity);

        return mPermissions;
    }
}
