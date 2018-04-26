package com.xinran.studytextureviewdemo;

import android.app.Application;

import org.xutils.x;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by houqixin on 2018/4/24.
 */

public class TextureViewApp extends Application {
    private JCVideoPlayerStandard mVideoPlaying;
    private static TextureViewApp sApp;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        sApp=this;
    }

    public static TextureViewApp newInstance() {
        return sApp;
    }

    public JCVideoPlayerStandard getVideoPlayer() {
        return mVideoPlaying;
    }

    public void setVideoPlayer(JCVideoPlayerStandard mVideoPlaying) {
        this.mVideoPlaying = mVideoPlaying;
    }
}
