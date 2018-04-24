package com.xinran.studytextureviewdemo;

import android.os.Bundle;
import android.view.View;

import com.xinran.studytextureviewdemo.camera.CameraActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Event(value ={R.id.camera_preview,R.id.video_player} )
    private void onClickButton(View view){
         int id=view.getId();
         if(id==R.id.video_player){
             mStartActvity(LocalViewPlayerActivity.class);
         }else if(id==R.id.camera_preview){
             mStartActvity(CameraActivity.class);
         }
    }

}
