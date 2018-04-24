package com.xinran.studytextureviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import org.xutils.x;

/**
 * Created by houqixin on 2018/4/24.
 */

public class BaseActivity extends FragmentActivity {
    private final static int DEFAULT_REQUEST_CODE=-1010;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    protected  void mStartActvity(Class<? extends BaseActivity> cls){
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }
    protected  void mStartActvity(Class<? extends BaseActivity> cls,Bundle bundle){
        mStartActvity(cls,bundle,DEFAULT_REQUEST_CODE);
    }
    protected  void mStartActvity(Class<? extends BaseActivity> cls,int reqCode){
        mStartActvity(cls,null,reqCode);

    }
    protected  void mStartActvity(Class<? extends BaseActivity> cls,Bundle bundle,int reqCode){
        Intent intent=new Intent(this,cls);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        if(reqCode!=DEFAULT_REQUEST_CODE){
            startActivityForResult(intent,reqCode);
        }else {
            startActivity(intent);
        }

    }

}
