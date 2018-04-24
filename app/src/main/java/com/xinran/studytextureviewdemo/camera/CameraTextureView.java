package com.xinran.studytextureviewdemo.camera;



import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {
	private static final String TAG = CameraTextureView.class.getSimpleName();
	Context mContext;
	SurfaceTexture mSurface;
	public CameraTextureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.setSurfaceTextureListener(this);
	}
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureAvailable...");
		mSurface = surface;
//		CameraInterface.getInstance().doStartPreview(surface, 1.33f);
	}
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureDestroyed...");
		CameraUtils.getInstance().stopCamera();
		return true;
	}
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureSizeChanged...");
	}
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSurfaceTextureUpdated...");
		
	}
	
	/* ��Activity�ܵõ�TextureView��SurfaceTexture
	 * @see android.view.TextureView#getSurfaceTexture()
	 */
	public SurfaceTexture getSurfaceTexture(){
		return mSurface;
	}
}
