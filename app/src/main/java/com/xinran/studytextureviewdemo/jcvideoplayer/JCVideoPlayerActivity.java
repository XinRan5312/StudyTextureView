package com.xinran.studytextureviewdemo.jcvideoplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.xinran.studytextureviewdemo.BaseActivity;
import com.xinran.studytextureviewdemo.LocalVideoFrament;
import com.xinran.studytextureviewdemo.R;
import com.xinran.studytextureviewdemo.TextureViewApp;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class JCVideoPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jc_player);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        createFragment();
    }

    /**
     * 创建一个Fragment
     */
    private void createFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        JCVideoPlayerFragment videoFragment = new JCVideoPlayerFragment();
        fragmentTransaction.add(R.id.fragment_container, videoFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (TextureViewApp.newInstance().getVideoPlayer() != null) {
            if (TextureViewApp.newInstance().getVideoPlayer().currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                TextureViewApp.newInstance().getVideoPlayer().startButton.performClick();
            } else if (TextureViewApp.newInstance().getVideoPlayer().currentState == JCVideoPlayer.CURRENT_STATE_PREPAREING) {
                JCVideoPlayer.releaseAllVideos();
            }
        }
    }
}
