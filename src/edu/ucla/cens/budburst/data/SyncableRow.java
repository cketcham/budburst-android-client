package edu.ucla.cens.budburst.data;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

public abstract class SyncableRow extends Row {
	public Boolean synced = false;

	public MultipartEntity uploadEntity() {
		MultipartEntity entity = new MultipartEntity();

		Field[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				entity.addPart(fields[i].getName(), new StringBody(fields[i].get(this).toString()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entity;
	}
}