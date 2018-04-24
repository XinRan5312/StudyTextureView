package com.xinran.studytextureviewdemo;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xinran.studytextureviewdemo.adapter.VideoInfoAdapter;
import com.xinran.studytextureviewdemo.entity.Video;
import com.xinran.studytextureviewdemo.utils.Utils;
import com.xinran.studytextureviewdemo.utils.VideoProvider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by houqixin on 2018/4/24.
 */
@ContentView(R.layout.fragment_videoplay_layout)
public class LocalVideoFrament extends BaseFrament implements TextureView.SurfaceTextureListener {

    @ViewInject(R.id.id_textureview)
    private TextureView mTextureView;
    private Surface mSurface;
    private MediaPlayer mediaPlayer;

    //播放器是否准备好
    private boolean isPrepared;

    @ViewInject(R.id.listview)
    private ListView mListView;
    private List<Video> mVideos;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        //扫描本地视频
        scanVideo();

    }

    /**
     * 扫描本地视频
     */
    private void scanVideo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mVideos = new ArrayList<>();
                VideoProvider videoProvider = new VideoProvider(getActivity());
                mVideos = videoProvider.getList();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mVideos.size() > 0) {
                            setAdapter();
                        } else {
//                            showToast("木有扫描到视频!");
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 填充数据
     */
    private void setAdapter() {
        VideoInfoAdapter infoAdapter = new VideoInfoAdapter(getActivity(), mVideos);
        mListView.setAdapter(infoAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mVideos.get(position).getPath();
                Log.v("test", "Path:=" + path);
                if (path != null) {
                    changePlay(path);
                }
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        mTextureView.setSurfaceTextureListener(this);
        //设置16:9
        int screenWeight = Utils.getWindowWidth(getActivity());
        ViewGroup.LayoutParams layoutParams = mTextureView.getLayoutParams();
        layoutParams.height = screenWeight * 9 / 16;
        mTextureView.setLayoutParams(layoutParams);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        initMediaPlayer();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mTextureView = null;
        mSurface = null;
        mediaPlayer.stop();
        mediaPlayer.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //surface.updateTexImage();
    }

    /**
     * 初始化播放器
     */
    public void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setSurface(mSurface);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换播放
     *
     * @param path
     */
    public void changePlay(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放or暂停视频
     */
    public void play() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            } else {
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }
    }


    @Event(value = R.id.id_textureview)
    private void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.id_textureview:
                if (isPrepared) {
                    play();
                }
                break;
        }
    }

}
