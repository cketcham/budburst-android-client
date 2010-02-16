package edu.ucla.cens.budburst;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CapturePhoto extends Activity implements SurfaceHolder.Callback {
	private static final String TAG = "CapturePhoto";

	Camera mCamera;
	boolean mPreviewRunning = false;
	private boolean clicked = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		setContentView(R.layout.photo);
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			Log.d(TAG, "PICTURE CALLBACK: data.length = " + data.length);

			Date now = new Date();
			long nowLong = now.getTime();
			// String fname = Budburst.OBSERVATION_PATH + "/" + nowLong + ".jpg";
			//
			try {

				// File ld = new File(Budburst.OBSERVATION_PATH);
				// if (ld.exists()) {
				// if (!ld.isDirectory()) {
				//
				// // Should probably inform user ... hmm!
				// Log.d(TAG, "Failed to create pic directory");
				// CapturePhoto.this.finish();
				// }
				// } else {
				// ld.mkdir();
				// }
				//
				// Log.d(TAG, fname);

				// OutputStream os = new FileOutputStream(fname);
				// os.write(data, 0, data.length);
				// os.close();

				FileOutputStream stream = CapturePhoto.this.openFileOutput(nowLong + ".jpg", Context.MODE_PRIVATE);
				Bitmap myPic = BitmapFactory.decodeByteArray(data, 0, data.length);
				myPic.compress(Bitmap.CompressFormat.JPEG, 90, stream);
				stream.flush();
				stream.close();

			} catch (FileNotFoundException e) {
				Log.d(TAG, "Could not write picture");
			} catch (IOException e) {
				Log.d(TAG, "Could not write picture");
			}

			// setResult is used to send result back to the Activity
			// that started this one.
			setResult(RESULT_OK, (new Intent()).putExtra("image_id", nowLong));
			finish();

			// mCamera.startPreview();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CapturePhoto.this.finish();
			return false;
			// return super.onKeyDown(keyCode, event);
		}

		if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (!clicked) {
				// mCamera.takePicture(null, null, mPictureCallback);
				clicked = true;
				mCamera.autoFocus(mAutoFocusCallback);
				return true;
			}
		}

		return false;
	}

	Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {

		public void onAutoFocus(boolean success, Camera camera) {
			mCamera.takePicture(null, null, mPictureCallback);
		}

	};

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
		mCamera = Camera.open();
		// mCamera.startPreview();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.d(TAG, "surfaceChanged");

		// XXX stopPreview() will crash if preview is not running
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		p.setPreviewSize(w, h);
		p.setPictureSize(640, 480);
		mCamera.setParameters(p);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.startPreview();
		mPreviewRunning = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed");
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
}