package edu.ucla.cens.budburst.helper;

import android.os.Message;

public interface Downloadable {

	public Object consumeInputStream(Message msg);
	public void onDownloaded(Message msg, Download d);
}
