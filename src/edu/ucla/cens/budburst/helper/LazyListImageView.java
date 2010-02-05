package edu.ucla.cens.budburst.helper;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LazyListImageView extends ImageView {
	private String mRemote;

	public LazyListImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LazyListImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setRemoteURI(String uri) {
		if (uri.startsWith("http")) {
			mRemote = uri;
		}
	}

	public String getRemoteURI() {
		return this.mRemote;
	}

}