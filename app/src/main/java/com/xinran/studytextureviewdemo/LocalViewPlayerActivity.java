package com.xinran.studytextureviewdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import org.xutils.view.annotation.ContentView;

/**
 * Created by houqixin on 2018/4/24.
 */
@ContentView(R.layout.activity_sinceresepak_layout)
public class LocalViewPlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragment();
    }

    /**
     * 创建一个Fragment
     */
    private void createFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LocalVideoFrament videoFragment = new LocalVideoFrament();
        fragmentTransaction.add(R.id.layout_container, videoFragment);
        fragmentTransaction.commit();
    }
}
